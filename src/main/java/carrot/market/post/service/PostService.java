package carrot.market.post.service;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.entity.Member;
import carrot.market.member.repository.MemberRepository;
import carrot.market.post.dto.PostRequestDto;
import carrot.market.post.dto.PostResponseDto;
import carrot.market.post.entity.Category;
import carrot.market.post.entity.Post;
import carrot.market.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static carrot.market.common.baseutil.BaseResponseStatus.*;
import static carrot.market.config.CacheKeyConfig.POST;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final MemberRepository memberRepository;
    private final CategoryService categoryService;

    public PostResponseDto findPostById(Long postId) {
        Post post = postJpaRepository.findPostById(postId).orElseThrow(() -> new BaseException(NOT_EXISTED_POST));
        return PostResponseDto.of(post);
    }

    /**
     * 게시물 생성
     * 게시물 생성 시 카테고리는 필수적으로 입력해야하기 때문에 캐싱 적용
     */
    @Transactional
    public void create(PostRequestDto postRequest, String email) {
        Member member = memberRepository.findByEmail(email);

        /** 카테고리 캐시 조회 및 저장 **/
        Category category = categoryService.findCategoryByName(postRequest.getCategory());

        Post post = Post.create(postRequest, member, category);
        postJpaRepository.save(post);
    }

    /**
     * 게시물 업데이트
     * @CacheEvict -> 캐시 데이터 제거
     */
    @Caching(evict = {
            @CacheEvict(
                    key = "#member.getAddress().state + '.' + #member.getAddress().city + '.' + #member.getAddress().town",
                    value = POST
            ),
            @CacheEvict(
                    key = "#member.getAddress().state + '.' + #member.getAddress().city + '.' + #member.getAddress().town + '.' + #postRequest.category",
                    value = POST
            )
    })
    @Transactional
    public void update(PostRequestDto postRequest, Long postId, String email) {
        Member member = memberRepository.findByEmail(email);
        Post post = postJpaRepository.findPostById(postId).orElseThrow(() -> new BaseException(NOT_EXISTED_POST));

        if(checkAuth(post, member)) {
            Category category = categoryService.findCategoryByName(postRequest.getCategory());
            /* Dirty Checking */
            post.update(postRequest, category);
        }
    }

    @Caching(evict = {
            @CacheEvict(
                    key = "#member.getAddress().state + '.' + #member.getAddress().city + '.' + #member.getAddress().town",
                    value = POST
            ),
            @CacheEvict(
                    key = "#member.getAddress().state + '.' + #member.getAddress().city + '.' + #member.getAddress().town + '.' + #postRequest.category",
                    value = POST
            )
    })
    @Transactional
    public void remove(Long postId, String email) {
        Member member = memberRepository.findByEmail(email);
        Post post = postJpaRepository.findPostById(postId).orElseThrow(() -> new BaseException(NOT_EXISTED_POST));
        if (checkAuth(post, member)) {
            post.removePost();
        }
    }

    private boolean checkAuth(Post post, Member member) {
        if(post.getAuthor().getId() != member.getId())
            throw new BaseException(AUTHORIZATION_FAIL);
        return true;
    }
}
