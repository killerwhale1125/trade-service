package carrot.market.post.dto;

import carrot.market.post.entity.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressRequestDto {
    @NotEmpty
    private String state;

    @NotEmpty
    private String city;

    @NotEmpty
    private String town;

    public AddressRequestDto(String state, String city, String town) {
        this.state = state;
        this.city = city;
        this.town = town;
    }

    public static Address toEntity(AddressRequestDto address) {
        return Address.builder()
                .state(address.getState())
                .city(address.getCity())
                .town(address.getTown())
                .build();
    }
}
