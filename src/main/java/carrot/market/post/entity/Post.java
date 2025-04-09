package carrot.market.post.entity;

import carrot.market.common.BaseTimeEntity;
import carrot.market.member.entity.Member;
import carrot.market.post.dto.PostRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @Lob
    private String content;

    private int price;
    private int stock;
    private String itemName;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    @Column(name = "IS_REMOVED")
    private boolean removed = false;

    @NotNull
    private int viewCount;
    @NotNull
    private int commentCount;
    @NotNull
    private int favoriteCount;

    public static Post create(PostRequestDto postRequest, Member member, Category category) {
        return Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(member)
                .price(postRequest.getPrice())
                .stock(postRequest.getStock())
                .itemName(postRequest.getItemName())
                .address(member.getAddress())
                .status(TradeStatus.SALE)
                .category(category)
                .build();
    }

    public void update(PostRequestDto postRequest, Category category) {
        this.title = postRequest.getTitle();
        this.content = postRequest.getContent();
        this.category = category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void removePost() {
        this.removed = true;
    }

    public void addFavoriteCount() {
        this.favoriteCount++;
    }

    public void removeFavoriteCount() {
        this.favoriteCount--;
    }
}
