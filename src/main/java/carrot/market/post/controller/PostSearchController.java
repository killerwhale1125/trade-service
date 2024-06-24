package carrot.market.post.controller;

import carrot.market.member.entity.Member;
import carrot.market.member.service.MemberService;
import carrot.market.post.dto.AddressRequestDto;
import carrot.market.post.dto.PostPageResponseDto;
import carrot.market.post.service.PostSearchService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class PostSearchController {

    private final PostSearchService postSearchService;
    private final MemberService memberService;

    /**
     * 회원의 주소 정보를 이용하여 해당 주소를 가진 게시물들을 페이지네이션 처리하여 응답
     */
    @GetMapping
    public ResponseEntity<PostPageResponseDto> getPosts(Authentication authentication, Pageable pageable) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        postSearchService.findAllByMemberAddress(member, pageable);
        return null;
    }

    @GetMapping("/address")
    public ResponseEntity<PostPageResponseDto> getPostsByAddress(AddressRequestDto addressRequest, Pageable pageable) {
        postSearchService.findAllByAddress(addressRequest, pageable);
        return null;
    }

    @GetMapping("/categories")
    public ResponseEntity<PostPageResponseDto> getTradePostsByCategory(@RequestParam("category") @NotEmpty String category,
                                                                    Authentication authentication,
                                                                    Pageable pageable) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        PostPageResponseDto page = postSearchService.findAllByCategory(category, member, pageable);

        return ResponseEntity.ok(page);
    }
}
