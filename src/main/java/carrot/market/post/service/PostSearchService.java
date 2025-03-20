package carrot.market.post.service;

import carrot.market.common.annotation.AreaInfoRequired;
import carrot.market.member.entity.Member;
import carrot.market.member.repository.MemberRepository;
import carrot.market.post.dto.AddressRequestDto;
import carrot.market.post.dto.PostPageResponseDto;
import carrot.market.post.dto.PostResponseDto;
import carrot.market.post.entity.Address;
import carrot.market.post.entity.Post;
import carrot.market.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static carrot.market.config.CacheKeyConfig.POST;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final PostJpaRepository postJpaRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원 주소에 관련하여 게시물 리스트 조회
     * @AreaInfoRequired 를 통하여 메소드 호출 전 AOP로 회원 주소 유효성 검증
     * @Cacheable을 통하여 Redis에 아래 설정에 맞는 캐싱된 값이 존재 할 경우 메소드를 실행하지 않고 캐싱된 값을 반환한다.
     */
    @AreaInfoRequired
    @Cacheable(
            // 파라미터로 받은 Member의 주소 정보를 조합하여 key 지정
            key = "#member.getAddress().state + '.' + #member.getAddress().city + '.' + #member.getAddress().town", 
            value = POST,   // 캐시 이름 지정
            cacheManager = "redisCacheManager",
            condition = "#pageable.pageNumber == 0" // page가 0일 경우만 캐시 사용
    )
    public PostPageResponseDto findAllByMemberAddress(Member member, Pageable pageable) {
        Address address = member.getAddress();
        Page<Post> posts
                = postJpaRepository.findAllByMemberAddress(address.getState(), address.getCity(), address.getTown(), pageable);
        return getPostPageRes(posts, pageable);
    }

    /**
     * 검색한 주소에 관련하여 게시물 리스트 조회
     * 주소값을 받기 때문에 AOP 검증 X
     */
    @Cacheable(
            key = "#addressRequest.getState() + '.' + #addressRequest.getCity() + '.' + #addressRequest.getTown()",
            value = POST,
            cacheManager = "redisCacheManager",
            condition = "#pageable.pageNumber == 0"
    )
    public PostPageResponseDto findAllByAddress(AddressRequestDto addressRequest, Pageable pageable) {
        Page<Post> posts = postJpaRepository
                .findAllByMemberAddress(addressRequest.getState(), addressRequest.getCity(), addressRequest.getTown(), pageable);
        return getPostPageRes(posts, pageable);
    }

    /**
     * 선택한 카테고리를 통하여 관련한 회원 주소 근처 게시물 검색
     */
    @AreaInfoRequired
    @Cacheable(
            key = "#member.getAddress().getState() + '.' + #member.getAddress().getCity() + '.' + #member.getAddress().getTown() + '.' + #category",
            value = POST,
            cacheManager = "redisCacheManager",
            condition = "#pageable.pageNumber == 0"
    )
    public PostPageResponseDto findAllByCategory(String category, Member member, Pageable pageable) {
        Address address = member.getAddress();
        Page<Post> posts = postJpaRepository.findAllByCategory(category, address.getState(), address.getCity(), address.getTown(), pageable);
        return getPostPageRes(posts, pageable);
    }

    private PostPageResponseDto getPostPageRes(Page<Post> posts, Pageable pageable) {
        List<PostResponseDto> response = posts.getContent()
                .stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());

        return PostPageResponseDto.builder()
                .totalPage(posts.getTotalPages())
                .currentPage(pageable.getPageNumber())
                .postResponses(response)
                .build();
    }
}
