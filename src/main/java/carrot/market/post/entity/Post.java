package carrot.market.post.entity;

import carrot.market.common.BaseTimeEntity;
import carrot.market.member.entity.Member;
import carrot.market.post.dto.PostRequestDto;
import jakarta.persistence.*;
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
    @Column(name = "POST_ID")
    private Long id;

    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member author;

    @Lob
    private String content;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    @Column(name = "IS_REMOVED")
    private Boolean removed = false;

    @Builder
    public Post(String title, TradeStatus status, Member author,
                String content, Address address, Location location) {
        this.title = title;
        this.status = status;
        this.author = author;
        this.content = content;
        this.address = address;
        this.location = location;
    }

    @Builder
    public Post(Long id, String title, Category category, TradeStatus status,
                Member author, String content, Address address, Location location,
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
}
