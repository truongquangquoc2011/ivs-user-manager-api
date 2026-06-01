package com.ivs.usermanager.modules.group;

import com.ivs.usermanager.common.decorator.RequirePermission;
import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.common.enums.PermissionAction;
import com.ivs.usermanager.modules.group.dto.GroupRequest;
import com.ivs.usermanager.modules.group.dto.GroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ivs.usermanager.modules.group.dto.GroupUserResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

        private final GroupService groupService;

        @GetMapping
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.VIEW)
        public ResponseEntity<ApiResponse<List<GroupResponse>>> getAllGroups() {

                return ResponseEntity.ok(
                                ApiResponse.<List<GroupResponse>>builder()
                                                .success(true)
                                                .message("Get groups successfully")
                                                .data(groupService.getAllGroups())
                                                .build());
        }

        @PostMapping
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<GroupResponse>> createGroup(
                        @RequestBody GroupRequest request) {

                return ResponseEntity.ok(
                                ApiResponse.<GroupResponse>builder()
                                                .success(true)
                                                .message("Create group successfully")
                                                .data(groupService.createGroup(request))
                                                .build());
        }

        @PutMapping("/{id}")
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<GroupResponse>> updateGroup(
                        @PathVariable Integer id,
                        @RequestBody GroupRequest request) {

                return ResponseEntity.ok(
                                ApiResponse.<GroupResponse>builder()
                                                .success(true)
                                                .message("Update group successfully")
                                                .data(groupService.updateGroup(id, request))
                                                .build());
        }

        @DeleteMapping("/{id}")
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<Object>> deleteGroup(
                        @PathVariable Integer id) {

                groupService.deleteGroup(id);

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Delete group successfully")
                                                .build());
        }

        @GetMapping("/{groupId}/users")
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.VIEW)
        public ResponseEntity<ApiResponse<List<GroupUserResponse>>> getUsersInGroup(
                        @PathVariable Integer groupId) {
                return ResponseEntity.ok(
                                ApiResponse.<List<GroupUserResponse>>builder()
                                                .success(true)
                                                .message("Get users in group successfully")
                                                .data(groupService.getUsersInGroup(groupId))
                                                .build());
        }

        @PostMapping("/{groupId}/users/{userId}")
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<Object>> addUserToGroup(
                        @PathVariable Integer groupId,
                        @PathVariable Integer userId) {
                groupService.addUserToGroup(groupId, userId);

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Add user to group successfully")
                                                .build());
        }

        @DeleteMapping("/{groupId}/users/{userId}")
        @RequirePermission(feature = "GROUP_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<Object>> removeUserFromGroup(
                        @PathVariable Integer groupId,
                        @PathVariable Integer userId) {
                groupService.removeUserFromGroup(groupId, userId);

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Remove user from group successfully")
                                                .build());
        }
}