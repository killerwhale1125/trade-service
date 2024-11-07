package carrot.market.post.entity;

import carrot.market.common.BaseTimeEntity;
import carrot.market.member.entity.MemberEntity;
import carrot.market.post.dto.PostRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private MemberEntity author;

    @Lob
    private String content;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    @Column(name = "IS_REMOVED")
    private Boolean removed = false;

    @NotNull
    private int viewCount;
    @NotNull
    private int commentCount;
    @NotNull
    private int favoriteCount;

    @Builder
    public Post(String title, TradeStatus status, MemberEntity author,
                String content, Address address, Location location) {
        this.title = title;
        this.status = status;
        this.author = author;
        this.content = content;
        this.address = address;
        this.location = location;
        this.viewCount = 0;
        this.commentCount = 0;
        this.favoriteCount = 0;
    }

    @Builder
    public Post(Long id, String title, Category category, TradeStatus status,
                MemberEntity author, String content, Address address, Location location,
                LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.status = status;
        this.author = author;
        this.content = content;
        this.address = address;
        this.location = location;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public void addCategory(Category category) {
        this.category = category;
        // 양방향 연결관계 설정
        category.getPosts().add(this);
    }

    public void updatePost(PostRequestDto postRequest) {
        this.title = postRequest.getTitle();
        this.content = postRequest.getContent();
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
