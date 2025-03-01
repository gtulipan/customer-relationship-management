package hu._ig.crm.crm4ig.mapper;

import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.model.PartnerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateMapper.class})
public interface PartnerMapper {
    PartnerMapper INSTANCE = Mappers.getMapper(PartnerMapper.class);
    PartnerDto toPartnerDto(Partner partner);
    Partner toPartner(PartnerDto partnerDto);
}