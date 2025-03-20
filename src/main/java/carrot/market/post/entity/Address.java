package carrot.market.post.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String state;
    private String city;
    private String town;

    public static Address create(String state, String city, String town) {
        return new Address(state, city, town);
    }
}
