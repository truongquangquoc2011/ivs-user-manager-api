package com.ivs.usermanager.modules.feature;

import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.modules.feature.dto.FeatureRequest;
import com.ivs.usermanager.modules.feature.dto.FeatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ivs.usermanager.common.decorator.RequirePermission;
import com.ivs.usermanager.common.enums.PermissionAction;
import java.util.List;

@RestController
@RequestMapping("/api/v1/features")
@RequiredArgsConstructor
public class FeatureController {

        private final FeatureService featureService;

        /**
         * Retrieves all features.
         *
         * @return list of features
         */
        @GetMapping
        @RequirePermission(feature = "PERMISSION_MANAGEMENT", action = PermissionAction.VIEW)
        public ResponseEntity<ApiResponse<List<FeatureResponse>>> getAllFeatures() {
                return ResponseEntity.ok(
                                ApiResponse.<List<FeatureResponse>>builder()
                                                .success(true)
                                                .message("Get features successfully")
                                                .data(featureService.getAllFeatures())
                                                .build());
        }

        /**
         * Retrieves a feature by ID.
         *
         * @param id feature ID
         * @return feature details
         */
        @GetMapping("/{id}")
        @RequirePermission(feature = "PERMISSION_MANAGEMENT", action = PermissionAction.VIEW)
        public ResponseEntity<ApiResponse<FeatureResponse>> getFeatureById(
                        @PathVariable Integer id) {
                return ResponseEntity.ok(
                                ApiResponse.<FeatureResponse>builder()
                                                .success(true)
                                                .message("Get feature successfully")
                                                .data(featureService.getFeatureById(id))
                                                .build());
        }

        /**
         * Creates a new feature.
         *
         * @param request feature data
         * @return created feature
         */
        @PostMapping
        @RequirePermission(feature = "PERMISSION_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<FeatureResponse>> createFeature(
                        @RequestBody FeatureRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.<FeatureResponse>builder()
                                                .success(true)
                                                .message("Create feature successfully")
                                                .data(featureService.createFeature(request))
                                                .build());
        }

        /**
         * Updates a feature by ID.
         *
         * @param id      feature ID
         * @param request updated feature data
         * @return updated feature
         */
        @PutMapping("/{id}")
        @RequirePermission(feature = "PERMISSION_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<FeatureResponse>> updateFeature(
                        @PathVariable Integer id,
                        @RequestBody FeatureRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.<FeatureResponse>builder()
                                                .success(true)
                                                .message("Update feature successfully")
                                                .data(featureService.updateFeature(id, request))
                                                .build());
        }

        /**
         * Deletes a feature by ID.
         *
         * @param id feature ID
         * @return operation result
         */
        @DeleteMapping("/{id}")
        @RequirePermission(feature = "PERMISSION_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<Object>> deleteFeature(
                        @PathVariable Integer id) {
                featureService.deleteFeature(id);

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Delete feature successfully")
                                                .build());
        }
}