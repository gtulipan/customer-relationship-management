package hu._ig.crm.crm4ig.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"user\"")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @Size(min = 1, max = 255)
    @NotNull(message = "Name cannot be NULL")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 1, max = 255)
    @NotNull(message = "Username cannot be NULL")
    @Column(name = "username", nullable = false)
    private String username;

    @Size(min = 5, max = 50)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Please enter a valid email!")
    @NotNull(message = "Email cannot be NULL")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull(message = "Password cannot be NULL")
    @Column(name = "password")
    @Size(min = 1, max = 255)
    private String password;

    @Version
    @Column(name = "version")
    private int version;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleEntity> roles;
}
