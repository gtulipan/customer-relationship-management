package hu._ig.crm.crm4ig.model;

import lombok.Data;

@Data
public class PartnerExportDto {
    private String partnerName;
    private String partnerEmail;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
}
