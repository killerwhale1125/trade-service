package carrot.market.post.dto;

import carrot.market.post.entity.Address;
import carrot.market.post.entity.Location;
import carrot.market.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String author;
    private String email;
    private String content;

    private String status;
    private String category;

    private Address address;
    private Location location;

    private int price;
    private int stock;
    private String itemName;

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;


    public static PostResponseDto of(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor().getNickname())
                .email(post.getAuthor().getEmail())
                .content(post.getContent())
                .createdTime(post.getCreatedTime())
                .modifiedTime(post.getModifiedTime())
                .status(post.getStatus().getTradeStatus())
                .category(post.getCategory().getCategoryName())
                .address(post.getAddress())
                .location(post.getLocation())
                .price(post.getPrice())
                .stock(post.getStock())
                .itemName(post.getItemName())
                .build();
    }
}
