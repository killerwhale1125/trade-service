package carrot.market.post.entity;

import lombok.Getter;

@Getter
public enum PostSortType {
    PRICE_ASC("ASC"), PRICE_DESC("DESC");

    PostSortType(String sortType) {
        this.sortType = sortType;
    }

    private final String sortType;
}
