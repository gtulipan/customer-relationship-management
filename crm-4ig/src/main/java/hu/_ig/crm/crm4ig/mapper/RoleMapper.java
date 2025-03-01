package hu._ig.crm.crm4ig.mapper;

import hu._ig.crm.crm4ig.domain.RoleEntity;
import hu._ig.crm.crm4ig.domain.UserEntity;
import hu._ig.crm.crm4ig.model.RoleDto;
import hu._ig.crm.crm4ig.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDto toRoleDto(RoleEntity roleEntity);
    RoleEntity toRoleEntity(RoleDto roleDto);
}
