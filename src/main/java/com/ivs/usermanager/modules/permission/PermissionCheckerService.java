package com.ivs.usermanager.modules.permission;

import com.ivs.usermanager.common.enums.PermissionAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCheckerService {

    private final PermissionCheckRepository permissionCheckRepository;

    public boolean hasPermission(
            String email,
            String featureCode,
            PermissionAction action
    ) {
        return permissionCheckRepository.countUserPermission(
                email,
                featureCode,
                action.name()
        ) > 0;
    }
}