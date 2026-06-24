package com.ivs.usermanager.modules.user;

import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.modules.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ivs.usermanager.modules.user.dto.UserRequest;
import java.util.List;
import com.ivs.usermanager.common.decorator.RequirePermission;
import com.ivs.usermanager.common.enums.PermissionAction;
import com.ivs.usermanager.common.dto.PaginationResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        /**
         * Retrieves users with pagination.
         *
         * @param skip number of records to skip
         * @param take number of records to retrieve
         * @return paginated user list
         */
        @GetMapping
        @RequirePermission(feature = "USER_MANAGEMENT", action = PermissionAction.VIEW)
        public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllUsers(
                        @RequestParam(defaultValue = "0") Integer skip,
                        @RequestParam(defaultValue = "10") Integer take) {
                return ResponseEntity.ok(
                                ApiResponse.<PaginationResponse<UserResponse>>builder()
                                                .success(true)
                                                .message("Get users successfully")
                                                .data(userService.getAllUsersPaging(skip, take))
                                                .build());
        }

        /**
         * Retrieves a user by ID.
         *
         * @param id user ID
         * @return user details
         */
        @GetMapping("/{id}")
        @RequirePermission(feature = "USER_MANAGEMENT", action = PermissionAction.VIEW)
        public ResponseEntity<ApiResponse<UserResponse>> getUserById(
                        @PathVariable Integer id) {
                return ResponseEntity.ok(
                                ApiResponse.<UserResponse>builder()
                                                .success(true)
                                                .message("Get user successfully")
                                                .data(userService.getUserById(id))
                                                .build());
        }

        // @PostMapping
        // @RequirePermission(feature = "USER_MANAGEMENT", action =
        // PermissionAction.EDIT)
        // public ResponseEntity<ApiResponse<UserResponse>> createUser(
        // @RequestBody UserRequest request
        // ) {
        // return ResponseEntity.ok(
        // ApiResponse.<UserResponse>builder()
        // .success(true)
        // .message("Create user successfully")
        // .data(userService.createUser(request))
        // .build()
        // );
        // }

        /**
         * Updates a user.
         *
         * @param id user ID
         * @param request user data
         * @return updated user
         */
        @PutMapping("/{id}")
        @RequirePermission(feature = "USER_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<UserResponse>> updateUser(
                        @PathVariable Integer id,
                        @RequestBody UserRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.<UserResponse>builder()
                                                .success(true)
                                                .message("Update user successfully")
                                                .data(userService.updateUser(id, request))
                                                .build());
        }

        /**
         * Deletes a user.
         *
         * @param id user ID
         * @return operation result
         */
        @DeleteMapping("/{id}")
        @RequirePermission(feature = "USER_MANAGEMENT", action = PermissionAction.EDIT)
        public ResponseEntity<ApiResponse<Object>> deleteUser(
                        @PathVariable Integer id) {
                userService.deleteUser(id);

                return ResponseEntity.ok(
                                ApiResponse.builder()
                                                .success(true)
                                                .message("Delete user successfully")
                                                .build());
        }

}