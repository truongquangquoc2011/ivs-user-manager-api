package com.ivs.usermanager.modules.role;

import com.ivs.usermanager.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /* Find role by its name (e.g., 'ADMIN', 'USER') */
    Optional<Role> findByName(String name);
}