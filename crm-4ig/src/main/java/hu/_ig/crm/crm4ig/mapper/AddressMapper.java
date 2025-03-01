package hu._ig.crm.crm4ig.mapper;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.model.AddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateMapper.class})
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    AddressDto toAddressDto(Address address);
    Address toAddress(AddressDto addressDto);
}
