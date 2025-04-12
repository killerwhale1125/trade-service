package carrot.market.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostPageResponseDto {

    private int totalPage;
    private boolean hasNext;
    private int currentPage;
    private List<PostResponseDto> postResponses = new ArrayList<>();
    private List<Object[]> pages;

    @Builder
    public PostPageResponseDto(int totalPage, int currentPage, List<PostResponseDto> postResponses, List<Object[]> pages, boolean hasNext) {
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.postResponses = postResponses;
        this.pages = pages;
        this.hasNext = hasNext;
    }
}
