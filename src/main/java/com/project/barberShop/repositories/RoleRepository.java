package com.project.barberShop.repositories;

import com.project.barberShop.models.ERole;
import com.project.barberShop.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(ERole roleName);
}
