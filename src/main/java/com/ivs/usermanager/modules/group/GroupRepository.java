package com.ivs.usermanager.modules.group;

import com.ivs.usermanager.common.entity.Group;
import com.ivs.usermanager.modules.group.projection.GroupProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(value = """
        SELECT
            g.id AS id,
            g.name AS name,
            g.description AS description,
            g.is_active AS isActive,
            g.created_at AS createdAt,
            g.updated_at AS updatedAt
        FROM user_groups_master g
        WHERE g.deleted_at IS NULL
        ORDER BY g.id DESC
        """, nativeQuery = true)
    List<GroupProjection> findAllGroups();

    @Query(value = """
        SELECT
            g.id AS id,
            g.name AS name,
            g.description AS description,
            g.is_active AS isActive,
            g.created_at AS createdAt,
            g.updated_at AS updatedAt
        FROM user_groups_master g
        WHERE g.id = :id
          AND g.deleted_at IS NULL
        LIMIT 1
        """, nativeQuery = true)
    Optional<GroupProjection> findGroupById(@Param("id") Integer id);

    @Query(value = """
        SELECT COUNT(1)
        FROM user_groups_master g
        WHERE g.name = :name
          AND g.deleted_at IS NULL
        """, nativeQuery = true)
    Long countByName(@Param("name") String name);
}