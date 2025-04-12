package carrot.market.post.dto;

import carrot.market.post.entity.Address;
import carrot.market.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
    private double location;

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
                .price(post.getPrice())
                .stock(post.getStock())
                .itemName(post.getItemName())
                .build();
    }

    public static PostResponseDto of(Object[] posts) {
        return PostResponseDto.builder()
                .id((Long) posts[0])
                .title((String) posts[1])
                .email((String) posts[3])
                .content((String) posts[4])
                .createdTime(((Timestamp) posts[5]).toLocalDateTime())
                .modifiedTime(((Timestamp) posts[6]).toLocalDateTime())
                .status((String) posts[7])
                .category((String) posts[8])
                .address(new Address((String) posts[9], (String) posts[10], (String) posts[11]))
                .price((int) posts[12])
                .stock((int) posts[13])
                .itemName((String) posts[14])
                .build();
    }
}
