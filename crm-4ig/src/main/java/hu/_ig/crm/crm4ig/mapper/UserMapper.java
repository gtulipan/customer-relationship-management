package hu._ig.crm.crm4ig.mapper;

import hu._ig.crm.crm4ig.domain.UserEntity;
import hu._ig.crm.crm4ig.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto toUserDto(UserEntity userEntity);
    UserEntity toUserEntity(UserDto userDto);
}
