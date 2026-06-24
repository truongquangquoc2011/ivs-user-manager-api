package com.ivs.usermanager.modules.permission;

import com.ivs.usermanager.common.enums.PermissionAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCheckerService {

    private final PermissionCheckRepository permissionCheckRepository;

    /**
     * Checks whether a user has the specified permission.
     *
     * @param email user email
     * @param featureCode feature code
     * @param action permission action
     * @return true if the user has permission, otherwise false
     */
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