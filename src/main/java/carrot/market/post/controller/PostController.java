package carrot.market.post.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.member.service.MemberServiceImpl;
import carrot.market.post.dto.PostRequestDto;
import carrot.market.post.dto.PostResponseDto;
import carrot.market.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final MemberServiceImpl memberServiceImpl;

    /**
     * 게시물 등록
     */
    @PostMapping
    public BaseResponse<Void> createPost(@RequestBody @Valid PostRequestDto postRequest, Authentication authentication) {
        postService.create(postRequest, authentication.getName());
        return new BaseResponse<>();
    }

    /**
     * 게시물 상세 조회
     */
    @GetMapping("/{postId}")
    public BaseResponse<PostResponseDto> findPost(@PathVariable Long postId) {
        return new BaseResponse<>(postService.findPostById(postId));
    }

    /**
     * 게시물 업데이트
     */
    @PutMapping("/{postId}")
    public BaseResponse<Void> updatePost(
            @Valid @RequestBody PostRequestDto postRequest,
             @PathVariable Long postId,
             Authentication authentication) {
        postService.update(postRequest, postId, authentication.getName());
        return new BaseResponse<>();
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/{postId}")
    public BaseResponse<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        postService.remove(postId, authentication.getName());
        return new BaseResponse<>();
    }

    @PostMapping("redis-test")
    public BaseResponse<Void> redisTest() {
        memberServiceImpl.redisTest();
        return new BaseResponse<>();
    }
}
