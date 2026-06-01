package com.ivs.usermanager.modules.permission;

import com.ivs.usermanager.common.decorator.RequirePermission;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final PermissionCheckerService permissionCheckerService;

    @Before("@annotation(requirePermission)")
    public void checkPermission(RequirePermission requirePermission) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated");
        }

        String email = authentication.getName();

        boolean allowed = permissionCheckerService.hasPermission(
                email,
                requirePermission.feature(),
                requirePermission.action()
        );

        if (!allowed) {
            throw new AccessDeniedException("You do not have permission");
        }
    }
}