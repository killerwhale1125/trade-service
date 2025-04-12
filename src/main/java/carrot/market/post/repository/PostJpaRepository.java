package carrot.market.post.repository;

import carrot.market.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostSearchRepository {

    Optional<Post> findPostById(Long postId);

    /**
     * 지역, 카테고리 조건으로 필터링 후 가격 내림차순 정렬.
     * FORCE INDEX(idx_filter_post) 를 사용해 인덱스 기반 최적화 수행.
     */
    @Query(value = """
            SELECT p.post_id,
                p.title,
                m.nickname,
                m.email,
                p.content,
                p.created_time,
                p.modified_time,
                p.status,
                c.category_name,
                p.state,
                p.city,
                p.town,
                p.price,
                p.stock,
                p.item_name
            FROM post p FORCE INDEX (idx_filter_post)
            JOIN member m ON m.member_id = p.member_id 
            JOIN category c ON c.category_id = p.category_id
            WHERE p.category_id = :categoryId
                  AND p.state = :state 
                  AND p.city = :city 
                  AND p.town = :town 
            ORDER BY p.price ASC
            LIMIT :offset, :pageSize
            """,
            nativeQuery = true)
    List<Object[]> findByPriceAscCovering(@Param("state") String state,
                                  @Param("city") String city,
                                  @Param("town") String town,
                                  @Param("categoryId") Long categoryId,
                                  @Param("offset") int offset,
                                  @Param("pageSize") int pageSize);

    /**
     * 지역, 카테고리 조건으로 필터링 후 가격 내림차순 정렬.
     * FORCE INDEX(idx_filter_post) 를 사용해 인덱스 기반 최적화 수행.
     */
    @Query(value = """
            SELECT p.post_id,
                p.title,
                m.nickname,
                m.email,
                p.content,
                p.created_time,
                p.modified_time,
                p.status,
                c.category_name,
                p.state,
                p.city,
                p.town,
                p.price,
                p.stock,
                p.item_name
            FROM post p FORCE INDEX (idx_filter_post)
            JOIN member m ON m.member_id = p.member_id 
            JOIN category c ON c.category_id = p.category_id
            WHERE p.category_id = :categoryId
                  AND p.state = :state 
                  AND p.city = :city 
                  AND p.town = :town 
            ORDER BY p.price DESC
            LIMIT :offset, :pageSize
            """,
            nativeQuery = true)
    List<Object[]> findByPriceDescCovering(@Param("state") String state,
                                        @Param("city") String city,
                                        @Param("town") String town,
                                        @Param("categoryId") Long categoryId,
                                        @Param("offset") int offset,
                                        @Param("pageSize") int pageSize);

    /**
     * 지역, 카테고리 조건으로 필터링 후 가격 내림차순 정렬.
     * SubQuery 최적화 사용
     */
    @Query(value = """
            SELECT p.post_id,
                p.title,
                m.nickname,
                m.email,
                p.content,
                p.created_time,
                p.modified_time,
                p.status,
                c.category_name,
                p.state,
                p.city,
                p.town,
                p.price,
                p.stock,
                p.item_name
            FROM (
                SELECT p1.post_id 
                FROM post p1 
                WHERE p1.category_id = :categoryId
                  AND p1.state = :state 
                  AND p1.city = :city 
                  AND p1.town = :town 
                ORDER BY p1.price ASC LIMIT :offset, :pageSize
            ) AS filtered 
            JOIN post p ON filtered.post_id = p.post_id 
            JOIN member m ON m.member_id = p.member_id 
            JOIN category c ON c.category_id = p.category_id
            """,
        nativeQuery = true)
    List<Object[]> findByPriceAscSubQuery(@Param("state") String state,
                                  @Param("city") String city,
                                  @Param("town") String town,
                                  @Param("categoryId") Long categoryId,
                                  @Param("offset") int offset,
                                  @Param("pageSize") int pageSize);

    /**
     * 지역, 카테고리 조건으로 필터링 후 가격 내림차순 정렬.
     * SubQuery 최적화 사용
     */
    @Query(value = """
            SELECT p.post_id,
                p.title,
                m.nickname,
                m.email,
                p.content,
                p.created_time,
                p.modified_time,
                p.status,
                c.category_name,
                p.state,
                p.city,
                p.town,
                p.price,
                p.stock,
                p.item_name
            FROM (
                SELECT p1.post_id 
                FROM post p1 
                WHERE p1.category_id = :categoryId 
                  AND p1.state = :state 
                  AND p1.city = :city 
                  AND p1.town = :town 
                ORDER BY p1.price DESC LIMIT :offset, :pageSize
            ) AS filtered 
            JOIN post p ON filtered.post_id = p.post_id 
            JOIN member m ON m.member_id = p.member_id 
            JOIN category c ON c.category_id = p.category_id
            """,
            nativeQuery = true)
    List<Object[]> findByPriceDescSubQuery(@Param("state") String state,
                                         @Param("city") String city,
                                         @Param("town") String town,
                                         @Param("categoryId") Long categoryId,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize);

    @Query(value = """
            SELECT COUNT(p1.post_id) 
            FROM post p1 
            WHERE p1.state = :state 
              AND p1.city = :city 
              AND p1.town = :town 
              AND p1.category_id = :categoryId 
            """,
            nativeQuery = true)
    long countByCondition(@Param("state") String state,
                          @Param("city") String city,
                          @Param("town") String town,
                          @Param("categoryId") Long categoryId);
}
