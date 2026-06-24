package com.ivs.usermanager.modules.permission;

import com.ivs.usermanager.common.entity.GroupPermission;
import com.ivs.usermanager.modules.permission.projection.GroupPermissionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for group permission data access.
 */
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, Integer> {

    /**
     * Retrieves permissions by group ID.
     *
     * @param groupId group ID
     * @return list of permissions
     */
    @Query(value = """
            SELECT
                f.id AS featureId,
                f.code AS featureCode,
                f.name AS featureName,
                f.path AS featurePath,
                IFNULL(gp.can_view, 0) AS canView,
                IFNULL(gp.can_edit, 0) AS canEdit
            FROM features f
            LEFT JOIN group_permissions gp
                ON gp.feature_id = f.id
                AND gp.group_id = :groupId
                AND gp.deleted_at IS NULL
            WHERE f.deleted_at IS NULL
              AND f.is_active = true
            ORDER BY f.id ASC
            """, nativeQuery = true)
    List<GroupPermissionProjection> findPermissionsByGroupId(
            @Param("groupId") Integer groupId);

    /**
     * Deletes all permissions of a group.
     *
     * @param groupId group ID
     */
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM group_permissions
            WHERE group_id = :groupId
            """, nativeQuery = true)
    void deleteAllByGroupId(@Param("groupId") Integer groupId);
}