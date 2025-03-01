package hu._ig.crm.crm4ig.model;

import hu._ig.crm.crm4ig.domain.UserEntity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    private UUID id;

    @Size(min = 1, max = 255)
    private String roleName;

    private int version;
    private UserDto user;
}
