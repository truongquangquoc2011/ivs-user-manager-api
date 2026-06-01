package com.ivs.usermanager.modules.permission;

import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.modules.permission.dto.GroupPermissionRequest;
import com.ivs.usermanager.modules.permission.dto.GroupPermissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ivs.usermanager.common.decorator.RequirePermission;
import com.ivs.usermanager.common.enums.PermissionAction;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @RequirePermission(feature = "PERMISSION_MANAGEMENT", action = PermissionAction.VIEW)
    public ResponseEntity<ApiResponse<List<GroupPermissionResponse>>> getGroupPermissions(
            @PathVariable Integer groupId) {
        return ResponseEntity.ok(
                ApiResponse.<List<GroupPermissionResponse>>builder()
                        .success(true)
                        .message("Get group permissions successfully")
                        .data(permissionService.getGroupPermissions(groupId))
                        .build());
    }

    @PutMapping
     @RequirePermission(
            feature = "PERMISSION_MANAGEMENT",
            action = PermissionAction.EDIT
    )
    public ResponseEntity<ApiResponse<Object>> updateGroupPermissions(
            @PathVariable Integer groupId,
            @RequestBody List<GroupPermissionRequest> requests) {
        permissionService.updateGroupPermissions(groupId, requests);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Update group permissions successfully")
                        .build());
    }
}