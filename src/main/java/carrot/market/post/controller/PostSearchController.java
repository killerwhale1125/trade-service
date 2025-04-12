package carrot.market.post.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.member.entity.Member;
import carrot.market.member.service.MemberServiceImpl;
import carrot.market.post.dto.AddressRequestDto;
import carrot.market.post.dto.PostPageResponseDto;
import carrot.market.post.dto.PostSearchRequest;
import carrot.market.post.entity.PostSortType;
import carrot.market.post.service.PostSearchService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post-search")
public class PostSearchController {

    private final PostSearchService postSearchService;
    private final MemberServiceImpl memberServiceImpl;

    /**
     * 회원 주소 기반 필터링 조회 API
     */
    @GetMapping
    public BaseResponse<PostPageResponseDto> getPosts(PostSearchRequest postSearchRequest, Authentication authentication, Pageable pageable) {
        Member member = memberServiceImpl.findMemberByEmail(authentication.getName());
        return new BaseResponse<>(postSearchService.getPosts(postSearchRequest, member, pageable));
    }

    /**
     * 서브쿼리 최적화 테스트 API
     */
    @GetMapping("filter")
    public BaseResponse<PostPageResponseDto> getPosts(PostSearchRequest postSearchRequest, AddressRequestDto addressRequest, Pageable pageable) {
        return new BaseResponse<>(postSearchService.getPosts(postSearchRequest, addressRequest, pageable));
    }

    /**
     * 커버링 인덱스 적용 테스트 API
     */
    @GetMapping("covering")
    public BaseResponse<PostPageResponseDto> getPostsCovering(PostSearchRequest postSearchRequest, AddressRequestDto addressRequest, Pageable pageable) {
        return new BaseResponse<>(postSearchService.getPostsCovering(postSearchRequest, addressRequest, pageable));
    }

    /**
     * 원본 API ( 성능 최적화 안됨 )
     */
    @GetMapping("as-is")
    public BaseResponse<PostPageResponseDto> asis(PostSearchRequest postSearchRequest, AddressRequestDto addressRequest, Pageable pageable) {
        return new BaseResponse<>(postSearchService.asis(postSearchRequest, addressRequest, pageable));
    }

    /**
     * 검색한 주소에 관련하여 주소 근처 게시물 리스트 조회 ( 경도 위도 값 사용 )
     * 주소값을 받기 때문에 AOP 검증 X
     */
    @GetMapping("/address")
    public BaseResponse<PostPageResponseDto> getPostsByAddress(AddressRequestDto addressRequest, Pageable pageable, PostSortType postSortType) {
        return new BaseResponse<>(postSearchService.findAllByAddress(addressRequest, postSortType, pageable));
    }

    /**
     * 선택한 카테고리를 통하여 관련한 회원 주소 근처 게시물 검색
     */
    @GetMapping("/categories")
    public BaseResponse<PostPageResponseDto> getPostsByCategory(
            @RequestParam("category") @NotEmpty String category,
            Authentication authentication,
            Pageable pageable) {
        Member member = memberServiceImpl.findMemberByEmail(authentication.getName());
        return new BaseResponse<>(postSearchService.findAllByCategory(category, member, pageable));
    }
}
