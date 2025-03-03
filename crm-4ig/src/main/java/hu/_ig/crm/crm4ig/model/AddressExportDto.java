package hu._ig.crm.crm4ig.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressExportDto {
    private String partnerName;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String floor;
    private String door;
}
