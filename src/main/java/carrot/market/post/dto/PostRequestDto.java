package carrot.market.post.dto;

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
    @NotEmpty
    private String itemName;
    @NotEmpty
    private int price;
    @NotEmpty
    private int stock;

}
