package carrot.market.post.dto;

import carrot.market.member.entity.Member;
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

    public Post toEntity(Member member) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .author(member)
                .address(member.getAddress())
                .location(member.getLocation())
                .status(TradeStatus.SALE)
                .build();
    }

    public Post toEntity(Member member, String title, String content) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .author(member)
                .address(member.getAddress())
                .location(member.getLocation())
                .status(TradeStatus.SALE)
                .build();
    }
}
