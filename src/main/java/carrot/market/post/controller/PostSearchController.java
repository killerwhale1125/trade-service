package carrot.market.post.controller;

import carrot.market.member.entity.MemberEntity;
import carrot.market.member.service.MemberServiceImpl;
import carrot.market.post.dto.AddressRequestDto;
import carrot.market.post.dto.PostPageResponseDto;
import carrot.market.post.service.PostSearchService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class PostSearchController {

    private final PostSearchService postSearchService;
    private final MemberServiceImpl memberServiceImpl;

    /**
     * 회원의 주소 정보를 이용하여 해당 주소를 가진 게시물들을 페이지네이션 처리하여 응답
     */
    @GetMapping
    public ResponseEntity<PostPageResponseDto> getPosts(Authentication authentication, Pageable pageable) {
        MemberEntity memberEntity = memberServiceImpl.findMemberByEmail(authentication.getName());
        PostPageResponseDto page = postSearchService.findAllByMemberAddress(memberEntity, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * 검색한 주소에 관련하여 주소 근처 게시물 리스트 조회
     * 주소값을 받기 때문에 AOP 검증 X
     */
    @GetMapping("/address")
    public ResponseEntity<PostPageResponseDto> getPostsByAddress(AddressRequestDto addressRequest, Pageable pageable) {
        PostPageResponseDto page = postSearchService.findAllByAddress(addressRequest, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * 선택한 카테고리를 통하여 관련한 회원 주소 근처 게시물 검색
     */
    @GetMapping("/categories")
    public ResponseEntity<PostPageResponseDto> getPostsByCategory(@RequestParam("category") @NotEmpty String category,
                                                                    Authentication authentication,
                                                                    Pageable pageable) {
        MemberEntity memberEntity = memberServiceImpl.findMemberByEmail(authentication.getName());
        PostPageResponseDto page = postSearchService.findAllByCategory(category, memberEntity, pageable);

        return ResponseEntity.ok(page);
    }
}
