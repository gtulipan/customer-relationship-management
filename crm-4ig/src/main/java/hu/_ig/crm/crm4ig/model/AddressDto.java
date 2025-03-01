package hu._ig.crm.crm4ig.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private UUID id;

    @Size(min = 1, max = 255)
    private String country;

    @Size(min = 1, max = 255)
    private String city;

    @Size(min = 1, max = 255)
    private String street;

    @Size(min = 1, max = 10)
    private String houseNumber;

    @Size(min = 1, max = 3)
    private String floor;

    @Size(min = 1, max = 3)
    private String door;

    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private int version;
    private PartnerDto partner;
}
