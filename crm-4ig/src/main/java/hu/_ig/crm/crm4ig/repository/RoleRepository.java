package hu._ig.crm.crm4ig.repository;

import hu._ig.crm.crm4ig.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
}
