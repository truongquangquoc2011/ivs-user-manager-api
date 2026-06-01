package com.ivs.usermanager.modules.feature;

import com.ivs.usermanager.common.entity.Feature;
import com.ivs.usermanager.modules.feature.projection.FeatureProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {

    @Query(value = """
        SELECT
            f.id AS id,
            f.code AS code,
            f.name AS name,
            f.path AS path,
            f.is_active AS isActive,
            f.created_at AS createdAt,
            f.updated_at AS updatedAt
        FROM features f
        WHERE f.deleted_at IS NULL
        ORDER BY f.id DESC
        """, nativeQuery = true)
    List<FeatureProjection> findAllFeatures();

    @Query(value = """
        SELECT
            f.id AS id,
            f.code AS code,
            f.name AS name,
            f.path AS path,
            f.is_active AS isActive,
            f.created_at AS createdAt,
            f.updated_at AS updatedAt
        FROM features f
        WHERE f.id = :id
          AND f.deleted_at IS NULL
        LIMIT 1
        """, nativeQuery = true)
    Optional<FeatureProjection> findFeatureById(@Param("id") Integer id);

    @Query(value = """
        SELECT *
        FROM features f
        WHERE f.id = :id
          AND f.deleted_at IS NULL
        LIMIT 1
        """, nativeQuery = true)
    Optional<Feature> findActiveEntityById(@Param("id") Integer id);

    @Query(value = """
        SELECT COUNT(1)
        FROM features f
        WHERE f.code = :code
          AND f.deleted_at IS NULL
        """, nativeQuery = true)
    Long countByCode(@Param("code") String code);

    @Query(value = """
        SELECT COUNT(1)
        FROM features f
        WHERE f.code = :code
          AND f.id <> :id
          AND f.deleted_at IS NULL
        """, nativeQuery = true)
    Long countByCodeExceptId(
            @Param("code") String code,
            @Param("id") Integer id
    );
}