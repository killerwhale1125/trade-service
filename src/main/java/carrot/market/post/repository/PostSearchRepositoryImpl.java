package carrot.market.post.repository;

import carrot.market.post.dto.PostSearchRequest;
import carrot.market.post.entity.Address;
import carrot.market.post.entity.Post;
import carrot.market.post.entity.PostSortType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static carrot.market.member.entity.QMember.member;
import static carrot.market.post.entity.QCategory.category;
import static carrot.market.post.entity.QPost.post;
import static io.micrometer.common.util.StringUtils.isEmpty;

@Slf4j
@Repository
public class PostSearchRepositoryImpl implements PostSearchRepository {

    private final JPAQueryFactory queryFactory;

    public PostSearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 통합 필터링 조회 API
     */
    @Override
    public Page<Post> getPosts(PostSearchRequest postSearchRequest, Address address, Pageable pageable) {
        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .join(post.author, member).fetchJoin()
                .join(post.category, category).fetchJoin()
                .where(addressCondition(address.getState(), address.getCity(), address.getTown())
                                .and(post.category.id.eq(postSearchRequest.getCategoryId()))
                        .and(post.status.eq(postSearchRequest.getTradeStatus())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        switch (postSearchRequest.getPostSortType()) {
            case PRICE_ASC -> query.orderBy(post.price.asc());
            case PRICE_DESC -> query.orderBy(post.price.desc());
        }

        List<Post> contents = query.fetch();

        JPAQuery<Post> countQuery = queryFactory.selectFrom(post)
                .join(post.category, category)
                .where(addressCondition(address.getState(), address.getCity(), address.getTown())
                        .and(post.category.id.eq(postSearchRequest.getCategoryId()))
                        .and(post.status.eq(postSearchRequest.getTradeStatus())));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    /**
     * Querydsl 의존성을 위해 QueryProjection 사용 X
     */
    @Override
    public Page<Post> findAllByMemberAddress(String state, String city, String town, PostSortType postSortType, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .join(post.author, member).fetchJoin()
                .join(post.category, category).fetchJoin()
                .where(addressCondition(state, city, town))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        switch (postSortType) {
            case PRICE_DESC -> query.orderBy(post.price.desc());
            default -> query.orderBy(post.price.asc());
        }

        List<Post> contents = query.fetch();

        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(addressCondition(state, city, town))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        /**
         * count 쿼리 최적화
         */
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<Post> findAllByCategory(String categoryName, String state, String city, String town, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
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
        return isEmpty(state) ? Expressions.TRUE : post.address.state.eq(state);
    }

    private BooleanExpression cityCondition(String city) {
        return isEmpty(city) ? Expressions.TRUE : post.address.city.eq(city);
    }

    private BooleanExpression townCondition(String town) {
        return isEmpty(town) ? Expressions.TRUE : post.address.town.eq(town);
    }

    private BooleanExpression categoryNameCondition(String categoryName) {
        return isEmpty(categoryName) ? Expressions.TRUE : post.category.categoryName.eq(categoryName);
    }
}
