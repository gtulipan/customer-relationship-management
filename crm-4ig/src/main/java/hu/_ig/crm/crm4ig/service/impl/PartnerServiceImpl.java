package hu._ig.crm.crm4ig.service.impl;

import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.exception.PartnerException;
import hu._ig.crm.crm4ig.mapper.AddressMapper;
import hu._ig.crm.crm4ig.mapper.PartnerMapper;
import hu._ig.crm.crm4ig.model.PartnerDto;
import hu._ig.crm.crm4ig.model.PartnerExportDto;
import hu._ig.crm.crm4ig.repository.PartnerRepository;
import hu._ig.crm.crm4ig.service.PartnerService;
import hu._ig.crm.crm4ig.specification.PartnerSpecification;
import hu._ig.crm.crm4ig.utils.PartnerExporter;
import hu._ig.crm.crm4ig.utils.PartnerImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu._ig.crm.crm4ig.constants.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private static final PartnerMapper partnerMapper = PartnerMapper.INSTANCE;
    private static final AddressMapper addressMapper = AddressMapper.INSTANCE;

    @Override
    public PartnerDto getPartnerById(UUID partnerId) {
        return partnerRepository.findById(partnerId).map(partnerMapper::toPartnerDto).orElseThrow(() -> new PartnerException(String.format(PARTNER_NOT_EXIST_WITH_ID, partnerId)));
    }

    @Override
    public List<PartnerDto> getAllPartners() {
        List<Partner> partners = partnerRepository.findAll();
        log.debug("Partner entities found: {}", partners.size());
        if (partners.isEmpty()) {
            return new ArrayList<>();
        }
        return partners.stream().map(partnerMapper::toPartnerDto).toList();
    }

    @Override
    public PartnerDto saveNewPartner(PartnerDto partnerDto) {
        Partner newPartner = partnerMapper.toPartner(partnerDto);
        Partner savedPartner = partnerRepository.save(newPartner);
        return partnerMapper.toPartnerDto(savedPartner);
    }

    @Override
    public PartnerDto updatePartner(UUID partnerId, PartnerDto partnerDto) {
        return partnerRepository.findById(partnerId).map(partner -> {
            partner.setName(partnerDto.getName());
            partner.setEmail(partnerDto.getEmail());
            if (partnerDto.getAddresses() != null) {
                partner.setAddresses(partnerDto.getAddresses().stream().map(addressMapper::toAddress).collect(Collectors.toSet()));
            }
            Partner updatedPartner = partnerRepository.save(partner);
            log.debug("Partner updated with ID: {}", partnerId);
            return partnerMapper.toPartnerDto(updatedPartner);
        }).orElseThrow(() -> new PartnerException(String.format(PARTNER_NOT_EXIST_WITH_ID, partnerId)));
    }

    @Override
    public void deletePartnerById(UUID partnerId) {
        partnerRepository.deleteById(partnerId);
        log.debug("Partner deleted with ID: {}", partnerId);
    }

    @Override
    public List<PartnerDto> findPartnersByAddress(String country, String city, String street, String houseNumber) {
        List<Partner> partners = partnerRepository.findAll(PartnerSpecification.getPartnersByAddress(country, city, street, houseNumber));
        log.debug("Partner entities found by address: {}", partners.size());
        if (partners.isEmpty()) {
            return new ArrayList<>();
        }
        return partners.stream().map(partnerMapper::toPartnerDto).toList();
    }

    @Override
    public List<PartnerExportDto> exportPartnersWithAddresses() {
        List<Partner> partners = partnerRepository.findAll();
        log.debug("Partner entities found to export: {}", partners.size());
        return PartnerExporter.exportPartnersWithAddresses(partners);
    }

    @Override
    public byte[] exportPartnersToPdf() {
        List<Partner> partners = partnerRepository.findAll();
        log.debug("Partner entities found to PDF export: {}", partners.size());
        return PartnerExporter.exportPartnersToPdf(partners);
    }

    @Override
    public void importPartnersFromCsv(InputStream csvInputStream) {
        PartnerImporter.importPartnersFromCsv(csvInputStream, partnerRepository);
    }
}
