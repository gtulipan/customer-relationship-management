package hu._ig.crm.crm4ig.service;

import hu._ig.crm.crm4ig.model.AddressDto;
import hu._ig.crm.crm4ig.model.AddressExportDto;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressDto getAddressById(UUID addressId);

    List<AddressDto> getAllAddresses();

    AddressDto saveNewAddressToPartner(AddressDto addressDto, UUID partnerId);

    AddressDto updateAddress(UUID addressId, AddressDto addressDto);

    void deleteAddressById(UUID addressId);

    List<AddressExportDto> exportAddresses();

    byte[] exportAddressesToPdf();
}
