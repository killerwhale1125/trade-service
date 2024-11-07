package carrot.market.member.domain;

import carrot.market.post.entity.Address;
import carrot.market.post.entity.Location;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LocationAddressRequestDto {

    private String state;
    private String city;
    private String town;

    private Double longitude;
    private Double latitude;

    public Address toAddress() {
        return Address.builder()
                .state(state)
                .city(city)
                .town(town)
                .build();
    }

    public Location toLocation() {
        return Location.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
