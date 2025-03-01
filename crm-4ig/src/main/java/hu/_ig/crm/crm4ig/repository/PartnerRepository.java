package hu._ig.crm.crm4ig.repository;

import hu._ig.crm.crm4ig.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartnerRepository extends JpaRepository<Partner, UUID> {
}
