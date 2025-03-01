package hu._ig.crm.crm4ig.repository;

import hu._ig.crm.crm4ig.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
