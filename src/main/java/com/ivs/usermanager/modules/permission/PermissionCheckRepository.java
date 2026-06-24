package com.ivs.usermanager.modules.permission;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface PermissionCheckRepository extends Repository<com.ivs.usermanager.common.entity.User, Integer> {

    /**
     * Counts user permissions by feature and action.
     *
     * @param email user email
     * @param featureCode feature code
     * @param action permission action
     * @return number of matching permissions
     */
    @Query(value = """
        SELECT COUNT(1)
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
          AND f.code = :featureCode
          AND (
                (:action = 'VIEW' AND gp.can_view = true)
                OR
                (:action = 'EDIT' AND gp.can_edit = true)
          )
        """, nativeQuery = true)
    Long countUserPermission(
            @Param("email") String email,
            @Param("featureCode") String featureCode,
            @Param("action") String action
    );
}