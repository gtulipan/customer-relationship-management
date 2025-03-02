package hu._ig.crm.crm4ig.model;

import hu._ig.crm.crm4ig.domain.RoleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    @Size(min = 1, max = 255)
    @NotNull(message = "Name cannot be NULL")
    private String name;

    @Size(min = 1, max = 255)
    @NotNull(message = "Username cannot be NULL")
    private String username;

    @Size(min = 5, max = 50)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Please enter a valid email!")
    private String email;

    @NotNull(message = "Password cannot be NULL")
    @Size(min = 1, max = 255)
    private String password;

    private int version;
    private Set<RoleDto> roles;
}
