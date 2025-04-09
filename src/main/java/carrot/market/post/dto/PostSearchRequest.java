package carrot.market.post.dto;

import carrot.market.post.entity.PostSortType;
import carrot.market.post.entity.TradeStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PostSearchRequest {
    private String title;
    private PostSortType postSortType;
    private Long categoryId;
    private TradeStatus tradeStatus;
}
