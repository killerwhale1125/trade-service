package carrot.market.post.controller;

import carrot.market.member.entity.Member;
import carrot.market.member.service.MemberService;
import carrot.market.post.dto.PostRequestDto;
import carrot.market.post.dto.PostResponseDto;
import carrot.market.post.entity.Post;
import carrot.market.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static carrot.market.common.HttpStatusResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    /**
     * 게시물 등록
     */
    @PostMapping
    public ResponseEntity<HttpStatus> createPost(@RequestBody @Valid PostRequestDto postRequest, Authentication authentication) {

        postService.createNewPost(postRequest, authentication.getName());

        return RESPONSE_OK;
    }

    /**
     * 게시물 상세 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> findPost(@PathVariable Long postId) {
        Post post = postService.findPostById(postId);

        return ResponseEntity.ok(PostResponseDto.of(post));
    }

    /**
     * 게시물 업데이트
     */
    @PutMapping("/{postId}")
    public ResponseEntity<HttpStatus> updatePost(@Valid @RequestBody PostRequestDto postRequest,
                                                 @PathVariable Long postId,
                                                 Authentication authentication) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        Post post = postService.findPostById(postId);

        postService.updatePost(post, postRequest, member);

        return RESPONSE_OK;
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long postId, Authentication authentication) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        Post post = postService.findPostById(postId);

        postService.removePost(post, member);

        return RESPONSE_OK;
    }
}
