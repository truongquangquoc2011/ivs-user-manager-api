package com.ivs.usermanager.modules.auth;

import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.modules.auth.projection.AuthUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Integer> {
    /**
     * * Retrieves authentication information for a user by email. * * @param email
     * user email * @return authentication data if the user exists
     */
    @Query(value = """
            SELECT
                u.id AS id,
                u.email AS email,
                u.password AS password,
                u.fullname AS fullname,
                u.phone_number AS phoneNumber,
                u.status AS status
            FROM users u
            WHERE u.email = :email
              AND u.deleted_at IS NULL
            LIMIT 1
            """, nativeQuery = true)
    Optional<AuthUserProjection> findAuthUserByEmail(@Param("email") String email);

    /**
     * * Counts active users with the given email address. * * @param email user
     * email * @return number of active users with the specified email
     */
    @Query(value = """
            SELECT COUNT(1)
            FROM users u
            WHERE u.email = :email
              AND u.deleted_at IS NULL
            """, nativeQuery = true)
    Long countActiveEmail(@Param("email") String email);
}