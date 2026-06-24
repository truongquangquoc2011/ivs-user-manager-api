package com.ivs.usermanager.modules.feature;

import com.ivs.usermanager.common.entity.Feature;
import com.ivs.usermanager.modules.feature.dto.FeatureRequest;
import com.ivs.usermanager.modules.feature.dto.FeatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for feature management.
 */
@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;

    /**
     * Retrieves all features.
     *
     * @return list of features
     */
    public List<FeatureResponse> getAllFeatures() {
        return featureRepository.findAllFeatures()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves a feature by ID.
     *
     * @param id feature ID
     * @return feature details
     */
    public FeatureResponse getFeatureById(Integer id) {
        var feature = featureRepository.findFeatureById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        return toResponse(feature);
    }

    /**
     * Creates a new feature.
     *
     * @param request feature data
     * @return created feature
     */
    public FeatureResponse createFeature(FeatureRequest request) {
        if (featureRepository.countByCode(request.getCode()) > 0) {
            throw new RuntimeException("Feature code already exists");
        }

        var feature = new Feature();
        feature.setCode(request.getCode());
        feature.setName(request.getName());
        feature.setPath(request.getPath());
        feature.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        featureRepository.save(feature);

        return getFeatureById(feature.getId());
    }

    /**
     * Updates a feature by ID.
     *
     * @param id feature ID
     * @param request updated feature data
     * @return updated feature
     */
    public FeatureResponse updateFeature(Integer id, FeatureRequest request) {
        var feature = featureRepository.findActiveEntityById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        if (featureRepository.countByCodeExceptId(request.getCode(), id) > 0) {
            throw new RuntimeException("Feature code already exists");
        }

        feature.setCode(request.getCode());
        feature.setName(request.getName());
        feature.setPath(request.getPath());

        if (request.getIsActive() != null) {
            feature.setIsActive(request.getIsActive());
        }

        featureRepository.save(feature);

        return getFeatureById(feature.getId());
    }

    /**
     * Deletes a feature by ID.
     *
     * @param id feature ID
     */
    public void deleteFeature(Integer id) {
        var feature = featureRepository.findActiveEntityById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        feature.setDeletedAt(LocalDateTime.now());

        feature.setCode(feature.getCode() + "_deleted_" + System.currentTimeMillis());

        featureRepository.save(feature);
    }

    /**
     * Converts a feature projection to a response object.
     *
     * @param feature feature projection
     * @return feature response
     */
    private FeatureResponse toResponse(com.ivs.usermanager.modules.feature.projection.FeatureProjection feature) {
        return FeatureResponse.builder()
                .id(feature.getId())
                .code(feature.getCode())
                .name(feature.getName())
                .path(feature.getPath())
                .isActive(feature.getIsActive())
                .createdAt(feature.getCreatedAt())
                .updatedAt(feature.getUpdatedAt())
                .build();
    }
}