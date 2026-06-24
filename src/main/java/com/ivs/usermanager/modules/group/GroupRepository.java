package com.ivs.usermanager.modules.group;

import com.ivs.usermanager.common.entity.Group;
import com.ivs.usermanager.modules.group.projection.GroupProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    /**
     * Retrieves all active groups.
     *
     * @return list of groups
     */
    @Query(value = """
            SELECT
                g.id AS id,
                g.name AS name,
                g.description AS description,
                g.is_active AS isActive,
                g.created_at AS createdAt,
                g.updated_at AS updatedAt
            FROM `groups` g
            WHERE g.deleted_at IS NULL
            ORDER BY g.id DESC
            """, nativeQuery = true)
    List<GroupProjection> findAllGroups();

    /**
     * Retrieves a group by ID.
     *
     * @param id group ID
     * @return group details
     */
    @Query(value = """
            SELECT
                g.id AS id,
                g.name AS name,
                g.description AS description,
                g.is_active AS isActive,
                g.created_at AS createdAt,
                g.updated_at AS updatedAt
            FROM `groups` g
            WHERE g.id = :id
              AND g.deleted_at IS NULL
            LIMIT 1
            """, nativeQuery = true)
    Optional<GroupProjection> findGroupById(@Param("id") Integer id);

    /**
     * Counts groups with the given name.
     *
     * @param name group name
     * @return number of matching groups
     */
    @Query(value = """
            SELECT COUNT(1)
            FROM `groups` g
            WHERE g.name = :name
              AND g.deleted_at IS NULL
            """, nativeQuery = true)
    Long countByName(@Param("name") String name);

    /**
     * Retrieves an active group entity by ID.
     *
     * @param id group ID
     * @return group entity
     */
    @Query(value = """
            SELECT *
            FROM `groups` g
            WHERE g.id = :id
              AND g.deleted_at IS NULL
            LIMIT 1
            """, nativeQuery = true)
    Optional<Group> findActiveEntityById(@Param("id") Integer id);
}