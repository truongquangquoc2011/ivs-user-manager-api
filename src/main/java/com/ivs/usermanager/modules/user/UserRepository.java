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

        /**
         * Retrieves all users.
         *
         * @return list of users
         */
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

        /**
         * Retrieves a user by ID.
         *
         * @param id user ID
         * @return user details
         */
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

        /**
         * Retrieves groups of a user.
         *
         * @param userId user ID
         * @return list of groups
         */
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

        /**
         * Counts users by email.
         *
         * @param email user email
         * @return number of matching users
         */
        @Query(value = """
                        SELECT COUNT(1)
                        FROM users u
                        WHERE u.email = :email
                          AND u.deleted_at IS NULL
                        """, nativeQuery = true)
        Long countByEmail(@Param("email") String email);

        /**
         * Counts users by email excluding the specified ID.
         *
         * @param email user email
         * @param id user ID
         * @return number of matching users
         */
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

        /**
         * Retrieves an active user entity by ID.
         *
         * @param id user ID
         * @return user entity
         */
        @Query(value = """
                        SELECT *
                        FROM users u
                        WHERE u.id = :id
                          AND u.deleted_at IS NULL
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<User> findActiveEntityById(@Param("id") Integer id);

        /**
         * Retrieves users with pagination.
         *
         * @param skip number of records to skip
         * @param take number of records to retrieve
         * @return paginated user list
         */
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

        /**
         * Counts all active users.
         *
         * @return total number of users
         */
        @Query(value = """
                        SELECT COUNT(1)
                        FROM users u
                        WHERE u.deleted_at IS NULL
                        """, nativeQuery = true)
        Long countAllUsers();

}