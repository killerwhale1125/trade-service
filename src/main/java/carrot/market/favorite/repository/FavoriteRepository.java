package carrot.market.favorite.repository;

import carrot.market.favorite.dto.request.FavoriteListResponseDto;
import carrot.market.favorite.entity.Favorite;
import carrot.market.member.entity.Member;
import carrot.market.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findFavoriteByMemberAndPost(Member member, Post post);

    @Query("select carrot.market.favorite.dto.request.FavoriteListResponseDto(m.email, m.nickname)" +
            " from Favorite f" +
            " join f.member m" +
            " join f.post p" +
            " where p.id = :postId")
    List<FavoriteListResponseDto> findFavoriteMemberListByPostId(@Param("postId") Long postId);
}
