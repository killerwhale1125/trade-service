package carrot.market.post.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Min(value = 100, message = "가격은 100원 이상이여야 합니다.")
    private int price;

    @NotNull
    @Min(value = 1, message = "재고는 1개 이상이여야 합니다.")
    private int stock;
}
