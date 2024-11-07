package carrot.market.post.dto;

import carrot.market.member.entity.MemberEntity;
import carrot.market.post.entity.Post;
import carrot.market.post.entity.TradeStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotEmpty
    @Length(max = 100, message = "제목은 최대 100글자를 넘을 수 없습니다.")
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String category;

    public Post toEntity(MemberEntity memberEntity) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .author(memberEntity)
                .address(memberEntity.getAddress())
                .location(memberEntity.getLocation())
                .status(TradeStatus.SALE)
                .build();
    }

    public Post toEntity(MemberEntity memberEntity, String title, String content) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .author(memberEntity)
                .address(memberEntity.getAddress())
                .location(memberEntity.getLocation())
                .status(TradeStatus.SALE)
                .build();
    }
}
