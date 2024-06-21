package carrot.market.image.entity;

import carrot.market.common.BaseTimeEntity;
import carrot.market.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "IMAGE_ID")
    private Long id;

    @Column(name = "IMAGE_NAME")
    private String name;

    @Column(name = "IMAGE_URL")
    private String url;

    @Column(name = "IS_REMOVED")
    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public Image(String name, String url, Post post) {
        this.name = name;
        this.url = url;
        this.post = post;
    }

}
