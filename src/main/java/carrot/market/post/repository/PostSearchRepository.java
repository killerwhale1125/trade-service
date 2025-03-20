package carrot.market.post.repository;

import carrot.market.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostSearchRepository {

    Page<Post> findAllByMemberAddress(String state, String city, String town, Pageable pageable);

    Page<Post> findAllByCategory(String category, String state, String city, String town, Pageable pageable);
}
