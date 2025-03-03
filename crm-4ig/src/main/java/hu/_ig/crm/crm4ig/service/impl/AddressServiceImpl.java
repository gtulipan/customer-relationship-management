package hu._ig.crm.crm4ig.service.impl;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.exception.AddressException;
import hu._ig.crm.crm4ig.exception.PartnerException;
import hu._ig.crm.crm4ig.mapper.AddressMapper;
import hu._ig.crm.crm4ig.mapper.PartnerMapper;
import hu._ig.crm.crm4ig.model.AddressDto;
import hu._ig.crm.crm4ig.model.AddressExportDto;
import hu._ig.crm.crm4ig.repository.AddressRepository;
import hu._ig.crm.crm4ig.repository.PartnerRepository;
import hu._ig.crm.crm4ig.service.AddressService;
import hu._ig.crm.crm4ig.utils.AddressExporter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static hu._ig.crm.crm4ig.constants.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final PartnerRepository partnerRepository;
    private static final AddressMapper addressMapper = AddressMapper.INSTANCE;
    private static final PartnerMapper partnerMapper = PartnerMapper.INSTANCE;

    @Override
    public AddressDto getAddressById(UUID addressId) {
        return addressRepository.findById(addressId).map(addressMapper::toAddressDto).orElseThrow(() -> new AddressException(String.format(ADDRESS_NOT_EXIST_WITH_ID, addressId)));
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        log.debug("Address entities found: {}", addresses.size());
        if (addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return addresses.stream().map(addressMapper::toAddressDto).toList();
    }

    @Transactional
    @Override
    public AddressDto saveNewAddressToPartner(AddressDto addressDto, UUID partnerId) {
        Partner partner =partnerRepository.findById(partnerId).orElseThrow(() -> new PartnerException(String.format(THERE_IS_NO_PARTNER_FOR_THE_NEW_ADDRESS, partnerId)));
        Address newAddress = addressMapper.toAddress(addressDto);
        newAddress.setPartner(partner);
        Address savedAddress = addressRepository.save(newAddress);
        log.debug("Address entity saved, ID: {}", savedAddress.getId());
        return addressMapper.toAddressDto(savedAddress);
    }

    @Transactional
    @Override
    public AddressDto updateAddress(UUID addressId, AddressDto addressDto) {
        if (addressDto.getPartnerId() == null) {
            throw new AddressException(ADDRESS_WITHOUT_A_PARTNER_CANNOT_BE_SAVED);
        }
        partnerRepository.findById(addressDto.getPartnerId())
                .orElseThrow(() -> new PartnerException(String.format(PARTNER_NOT_EXIST_WITH_ID, addressDto.getPartnerId())));
        return addressRepository.findById(addressId).map(address -> {
            address.setCountry(addressDto.getCountry());
            address.setCity(addressDto.getCity());
            address.setStreet(addressDto.getStreet());
            address.setHouseNumber(addressDto.getHouseNumber());
            address.setFloor(addressDto.getFloor());
            address.setDoor(addressDto.getDoor());
            Address updatedAddress = addressRepository.save(address);
            log.debug("Address updated with ID: {}", addressId);
            return addressMapper.toAddressDto(updatedAddress);
        }).orElseThrow(() -> new AddressException(String.format(ADDRESS_NOT_EXIST_WITH_ID, addressId)));
    }

    @Override
    public void deleteAddressById(UUID addressId) {
        addressRepository.deleteById(addressId);
        log.debug("Address deleted with ID: {}", addressId);
    }

    @Override
    public List<AddressExportDto> exportAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return AddressExporter.exportAddresses(addresses);
    }

    @Override
    public byte[] exportAddressesToPdf() {
        List<Address> addresses = addressRepository.findAll();
        log.debug("Address entities found to PDF export: {}", addresses.size());
        return AddressExporter.exportAddressesToPdf(addresses);
    }
}
