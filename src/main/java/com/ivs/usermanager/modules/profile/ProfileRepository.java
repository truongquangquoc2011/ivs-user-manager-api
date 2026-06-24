package com.ivs.usermanager.modules.profile;

import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.modules.profile.projection.ProfileGroupProjection;
import com.ivs.usermanager.modules.profile.projection.ProfilePermissionProjection;
import com.ivs.usermanager.modules.profile.projection.ProfileProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for profile data access.
 */
public interface ProfileRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves profile information by email.
     *
     * @param email user email
     * @return profile details
     */
    @Query(value = """
            SELECT
                u.id AS id,
                u.email AS email,
                u.fullname AS fullname,
                u.phone_number AS phoneNumber,
                u.avatar AS avatar,
                u.status AS status
            FROM users u
            WHERE u.email = :email
              AND u.deleted_at IS NULL
            LIMIT 1
            """, nativeQuery = true)
    Optional<ProfileProjection> findProfileByEmail(@Param("email") String email);

    /**
     * Retrieves an active user entity by email.
     *
     * @param email user email
     * @return user entity
     */
    @Query(value = """
            SELECT *
            FROM users u
            WHERE u.email = :email
              AND u.deleted_at IS NULL
            LIMIT 1
            """, nativeQuery = true)
    Optional<User> findActiveEntityByEmail(@Param("email") String email);

    /**
     * Retrieves groups of a user.
     *
     * @param email user email
     * @return list of groups
     */
    @Query(value = """
            SELECT
                g.id AS id,
                g.name AS name,
                g.description AS description,
                g.is_active AS isActive
            FROM users u
            INNER JOIN user_groups ug
                ON ug.user_id = u.id
                AND ug.deleted_at IS NULL
            INNER JOIN `groups` g
                ON g.id = ug.group_id
                AND g.deleted_at IS NULL
                AND g.is_active = true
            WHERE u.email = :email
              AND u.deleted_at IS NULL
            ORDER BY g.id ASC
            """, nativeQuery = true)
    List<ProfileGroupProjection> findGroupsByEmail(@Param("email") String email);

    /**
     * Retrieves permissions of a user.
     *
     * @param email user email
     * @return list of permissions
     */
    @Query(value = """
            SELECT
                f.code AS featureCode,
                f.name AS featureName,
                f.path AS featurePath,
                MAX(gp.can_view) AS canView,
                MAX(gp.can_edit) AS canEdit
            FROM users u
            INNER JOIN user_groups ug
                ON ug.user_id = u.id
                AND ug.deleted_at IS NULL
            INNER JOIN `groups` g
                ON g.id = ug.group_id
                AND g.deleted_at IS NULL
                AND g.is_active = true
            INNER JOIN group_permissions gp
                ON gp.group_id = g.id
            INNER JOIN features f
                ON f.id = gp.feature_id
                AND f.deleted_at IS NULL
                AND f.is_active = true
            WHERE u.email = :email
              AND u.deleted_at IS NULL
            GROUP BY f.code, f.name, f.path
            ORDER BY f.code ASC
            """, nativeQuery = true)
    List<ProfilePermissionProjection> findPermissionsByEmail(@Param("email") String email);
}