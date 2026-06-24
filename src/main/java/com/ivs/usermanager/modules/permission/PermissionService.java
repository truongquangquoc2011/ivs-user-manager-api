package com.ivs.usermanager.modules.permission;

import com.ivs.usermanager.common.entity.GroupPermission;
import com.ivs.usermanager.modules.feature.FeatureRepository;
import com.ivs.usermanager.modules.group.GroupRepository;
import com.ivs.usermanager.modules.permission.dto.GroupPermissionRequest;
import com.ivs.usermanager.modules.permission.dto.GroupPermissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final GroupRepository groupRepository;
    private final FeatureRepository featureRepository;
    private final GroupPermissionRepository groupPermissionRepository;

    /**
     * Retrieves permissions of a group.
     *
     * @param groupId group ID
     * @return list of permissions
     */
    public List<GroupPermissionResponse> getGroupPermissions(Integer groupId) {

        groupRepository.findActiveEntityById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return groupPermissionRepository.findPermissionsByGroupId(groupId)
                .stream()
                .map(permission -> GroupPermissionResponse.builder()
                        .featureId(permission.getFeatureId())
                        .featureCode(permission.getFeatureCode())
                        .featureName(permission.getFeatureName())
                        .featurePath(permission.getFeaturePath())
                        .canView(permission.getCanView() != null && permission.getCanView() == 1)
                        .canEdit(permission.getCanEdit() != null && permission.getCanEdit() == 1)
                        .build())
                .toList();
    }

    /**
     * Updates permissions of a group.
     *
     * @param groupId group ID
     * @param requests permission data
     */
    public void updateGroupPermissions(
            Integer groupId,
            List<GroupPermissionRequest> requests
    ) {

        var group = groupRepository.findActiveEntityById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        groupPermissionRepository.deleteAllByGroupId(groupId);

        for (GroupPermissionRequest request : requests) {

            var feature = featureRepository.findActiveEntityById(request.getFeatureId())
                    .orElseThrow(() -> new RuntimeException(
                            "Feature not found: " + request.getFeatureId()
                    ));

            var permission = new GroupPermission();

            permission.setGroup(group);
            permission.setFeature(feature);
            permission.setCanView(
                    request.getCanView() != null ? request.getCanView() : false
            );
            permission.setCanEdit(
                    request.getCanEdit() != null ? request.getCanEdit() : false
            );

            groupPermissionRepository.save(permission);
        }
    }
}