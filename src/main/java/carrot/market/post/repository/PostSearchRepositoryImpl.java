package carrot.market.post.repository;

import carrot.market.post.entity.Address;
import carrot.market.post.entity.Post;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static carrot.market.post.entity.QPost.post;
import static io.micrometer.common.util.StringUtils.isEmpty;

@Repository
public class PostSearchRepositoryImpl implements PostSearchRepository {

    private final JPAQueryFactory queryFactory;

    public PostSearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * Querydsl 의존성을 위해 QueryProjection 사용 X
     */
    @Override
    public Page<Post> findAllByMemberAddress(String state, String city, String town, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .innerJoin(post.author).fetchJoin()
                .where(addressCondition(state, city, town))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Post> contents = query.fetch();

        /**
         * count 쿼리 최적화
         */
        return PageableExecutionUtils.getPage(contents, pageable,
                query::fetchCount);
    }

    @Override
    public Page<Post> findAllByCategory(String categoryName, String state, String city, String town, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .innerJoin(post.author).fetchJoin()
                .where(addressCondition(state, city, town).and(categoryNameCondition(categoryName)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Post> contents = query.fetch();

        /**
         * count 쿼리 최적화
         */
        return PageableExecutionUtils.getPage(contents, pageable,
                query::fetchCount);
    }

    private BooleanExpression addressCondition(String state, String city, String town) {
        return stateCondition(state)
                .and(cityCondition(city))
                .and(townCondition(town));
    }

    private BooleanExpression stateCondition(String state) {
        return isEmpty(state) ? null : post.address.state.eq(state);
    }

    private BooleanExpression cityCondition(String city) {
        return isEmpty(city) ? null : post.address.city.eq(city);
    }

    private BooleanExpression townCondition(String town) {
        return isEmpty(town) ? null : post.address.town.eq(town);
    }

    private BooleanExpression categoryNameCondition(String categoryName) {
        return isEmpty(categoryName) ? null : post.category.categoryName.eq(categoryName);
    }
}
