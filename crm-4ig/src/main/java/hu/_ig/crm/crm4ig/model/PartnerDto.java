package hu._ig.crm.crm4ig.model;

import hu._ig.crm.crm4ig.domain.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerDto {

    private UUID id;

    @Size(min = 1, max = 255)
    @NotBlank
    private String name;

    @Size(min = 5, max = 50)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Please enter a valid email!")
    private String email;

    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private int version;
    private Set<Address> addresses;
}
