package com.ivs.usermanager.modules.group;

import com.ivs.usermanager.common.entity.UserGroup;
import com.ivs.usermanager.modules.group.projection.GroupUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    @Query(value = """
            SELECT
                u.id AS id,
                u.email AS email,
                u.fullname AS fullname,
                u.phone_number AS phoneNumber,
                u.status AS status
            FROM user_groups ug
            INNER JOIN users u
                ON ug.user_id = u.id
                AND u.deleted_at IS NULL
            WHERE ug.group_id = :groupId
              AND ug.deleted_at IS NULL
            ORDER BY u.id DESC
            """, nativeQuery = true)
    List<GroupUserProjection> findUsersByGroupId(@Param("groupId") Integer groupId);

    @Query(value = """
            SELECT COUNT(1)
            FROM user_groups ug
            WHERE ug.group_id = :groupId
              AND ug.user_id = :userId
              AND ug.deleted_at IS NULL
            """, nativeQuery = true)
    Long countActiveUserInGroup(
            @Param("groupId") Integer groupId,
            @Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE user_groups ug
            SET ug.deleted_at = NOW()
            WHERE ug.group_id = :groupId
              AND ug.user_id = :userId
              AND ug.deleted_at IS NULL
            """, nativeQuery = true)
    void softDeleteUserFromGroup(
            @Param("groupId") Integer groupId,
            @Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE user_groups ug
            SET ug.deleted_at = NOW()
            WHERE ug.user_id = :userId
              AND ug.deleted_at IS NULL
            """, nativeQuery = true)
    void softDeleteAllByUserId(@Param("userId") Integer userId);
}