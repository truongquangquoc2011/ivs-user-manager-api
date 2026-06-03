package com.ivs.usermanager.modules.user;

import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.modules.user.projection.UserGroupProjection;
import com.ivs.usermanager.modules.user.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

        @Query(value = """
                        SELECT
                            u.id AS id,
                            u.email AS email,
                            u.fullname AS fullname,
                            u.phone_number AS phoneNumber,
                            u.status AS status,
                            u.created_at AS createdAt,
                            u.updated_at AS updatedAt
                        FROM users u
                        WHERE u.deleted_at IS NULL
                        ORDER BY u.id DESC
                        """, nativeQuery = true)
        List<UserProjection> findAllUsers();

        @Query(value = """
                        SELECT
                            u.id AS id,
                            u.email AS email,
                            u.fullname AS fullname,
                            u.phone_number AS phoneNumber,
                            u.status AS status,
                            u.created_at AS createdAt,
                            u.updated_at AS updatedAt
                        FROM users u
                        WHERE u.id = :id
                          AND u.deleted_at IS NULL
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<UserProjection> findUserById(@Param("id") Integer id);

        @Query(value = """
                        SELECT
                            g.id AS id,
                            g.name AS name,
                            g.description AS description,
                            g.is_active AS isActive
                        FROM user_groups ug
                        INNER JOIN `groups` g
                            ON ug.group_id = g.id
                            AND g.deleted_at IS NULL
                        WHERE ug.user_id = :userId
                          AND ug.deleted_at IS NULL
                        ORDER BY g.id DESC
                        """, nativeQuery = true)
        List<UserGroupProjection> findGroupsByUserId(@Param("userId") Integer userId);

        @Query(value = """
                        SELECT COUNT(1)
                        FROM users u
                        WHERE u.email = :email
                          AND u.deleted_at IS NULL
                        """, nativeQuery = true)
        Long countByEmail(@Param("email") String email);

        @Query(value = """
                        SELECT COUNT(1)
                        FROM users u
                        WHERE u.email = :email
                          AND u.id <> :id
                          AND u.deleted_at IS NULL
                        """, nativeQuery = true)
        Long countByEmailExceptId(
                        @Param("email") String email,
                        @Param("id") Integer id);

        @Query(value = """
                        SELECT *
                        FROM users u
                        WHERE u.id = :id
                          AND u.deleted_at IS NULL
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<User> findActiveEntityById(@Param("id") Integer id);

        @Query(value = """
                        SELECT
                            u.id AS id,
                            u.email AS email,
                            u.fullname AS fullname,
                            u.phone_number AS phoneNumber,
                            u.status AS status,
                            u.created_at AS createdAt,
                            u.updated_at AS updatedAt
                        FROM users u
                        WHERE u.deleted_at IS NULL
                        ORDER BY u.id DESC
                        LIMIT :take OFFSET :skip
                        """, nativeQuery = true)
        List<UserProjection> findAllUsersPaging(
                        @Param("skip") Integer skip,
                        @Param("take") Integer take);

        @Query(value = """
                        SELECT COUNT(1)
                        FROM users u
                        WHERE u.deleted_at IS NULL
                        """, nativeQuery = true)
        Long countAllUsers();

}