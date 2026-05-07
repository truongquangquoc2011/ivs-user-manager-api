package com.ivs.usermanager.modules.auth;

import com.ivs.usermanager.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {
    /* Find user by email for authentication purposes */
    Optional<User> findByEmail(String email);

    /* Check if email already exists in the system */
    boolean existsByEmail(String email);
}