package carrot.market.post.service;

import carrot.market.exception.CategoryNotFoundException;
import carrot.market.post.entity.Category;
import carrot.market.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static carrot.market.config.CacheKeyConfig.CATEGORY;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * @Cacheable를 사용하여 메서드의 반환값을 CATEGORY라는 캐시에 저장
     * #categoryName key값을 통하여 캐시 조회 후 동일한 입력값이 요청될 경우 캐시의 값 반환
     * #categoryName은 매개변수 categoryName을 통하여 동적으로 변경되는 key 값
     * 카테고리는 중복되는 값이 많은 요청으로 인하여 캐시에 적합
     */
    @Cacheable(key = "#categoryName", value = CATEGORY, cacheManager = "redisCacheManager", cacheNames = CATEGORY)
    public Category findCategoryByName(String categoryName) {
        return categoryRepository.findCategoryByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));
    }
}
