package carrot.market.favorite.service;

import carrot.market.common.baseutil.BaseException;
import carrot.market.favorite.dto.request.FavoriteListResponseDto;
import carrot.market.favorite.entity.Favorite;
import carrot.market.favorite.repository.FavoriteRepository;
import carrot.market.member.entity.Member;
import carrot.market.member.repository.MemberRepository;
import carrot.market.post.entity.Post;
import carrot.market.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static carrot.market.common.baseutil.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final PostRepository boardRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void addFavorite(String email, Long boardId) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        Post post = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_EXISTED_POST));
        favoriteRepository.findFavoriteByMemberAndPost(member, post).ifPresentOrElse(
                favorite -> {
                    post.removeFavoriteCount();
                    favoriteRepository.delete(favorite);
                },
                () -> {
                    post.addFavoriteCount();
                    favoriteRepository.save(Favorite.builder().post(post).member(member).build());
                }
        );
    }

    public List<FavoriteListResponseDto> findFavoriteList(Long postId) {
        return favoriteRepository.findFavoriteMemberListByPostId(postId);
    }
}
