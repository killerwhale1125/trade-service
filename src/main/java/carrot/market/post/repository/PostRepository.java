package carrot.market.post.repository;

import carrot.market.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostSearchRepository {

    Optional<Post> findPostById(Long postId);
}
