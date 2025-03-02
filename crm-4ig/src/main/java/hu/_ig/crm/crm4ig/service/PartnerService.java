package hu._ig.crm.crm4ig.service;

import hu._ig.crm.crm4ig.model.PartnerDto;
import hu._ig.crm.crm4ig.model.PartnerExportDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface PartnerService {
    PartnerDto getPartnerById(UUID partnerId);

    List<PartnerDto> getAllPartners();

    PartnerDto saveNewPartner(PartnerDto partnerDto);

    PartnerDto updatePartner(UUID partnerId, PartnerDto partnerDto);

    void deletePartnerById(UUID partnerId);

    List<PartnerDto> findPartnersByAddress(String country, String city, String street, String houseNumber);

    List<PartnerExportDto> exportPartnersWithAddresses();

    byte[] exportPartnersToPdf();

    void importPartnersFromCsv(InputStream csvInputStream);
}
