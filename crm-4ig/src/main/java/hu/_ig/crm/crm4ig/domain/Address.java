package hu._ig.crm.crm4ig.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @Size(min = 1, max = 255)
    @Column(name = "country", nullable = false)
    private String country;

    @Size(min = 1, max = 255)
    @Column(name = "city", nullable = false)
    private String city;

    @Size(min = 1, max = 255)
    @Column(name = "street", nullable = false)
    private String street;

    @Size(min = 1, max = 10)
    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Size(min = 1, max = 3)
    @Column(name = "floor")
    private String floor;

    @Size(min = 1, max = 3)
    @Column(name = "door")
    private String door;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Version
    @Column(name = "version")
    private int version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Partner partner;
}
