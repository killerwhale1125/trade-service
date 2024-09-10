package carrot.market.favorite.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.favorite.dto.request.FavoriteListResponseDto;
import carrot.market.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PutMapping("/{postId}")
    public BaseResponse<Void> addFavorite(Authentication authentication, @PathVariable Long postId) {
        favoriteService.addFavorite(authentication.getName(), postId);
        return new BaseResponse<>();
    }

    @GetMapping("/{postId}")
    public BaseResponse<List<FavoriteListResponseDto>> findFavoriteList(@PathVariable Long postId) {
        return new BaseResponse<>(favoriteService.findFavoriteList(postId));
    }
}
