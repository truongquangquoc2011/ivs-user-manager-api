package com.ivs.usermanager.modules.user;

import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.modules.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ivs.usermanager.modules.user.dto.UserRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("Get users successfully")
                        .data(userService.getAllUsers())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Get user successfully")
                        .data(userService.getUserById(id))
                        .build()
        );
    }
    @PostMapping
public ResponseEntity<ApiResponse<UserResponse>> createUser(
        @RequestBody UserRequest request
) {
    return ResponseEntity.ok(
            ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("Create user successfully")
                    .data(userService.createUser(request))
                    .build()
    );
}

@PutMapping("/{id}")
public ResponseEntity<ApiResponse<UserResponse>> updateUser(
        @PathVariable Integer id,
        @RequestBody UserRequest request
) {
    return ResponseEntity.ok(
            ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("Update user successfully")
                    .data(userService.updateUser(id, request))
                    .build()
    );
}

@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<Object>> deleteUser(
        @PathVariable Integer id
) {
    userService.deleteUser(id);

    return ResponseEntity.ok(
            ApiResponse.builder()
                    .success(true)
                    .message("Delete user successfully")
                    .build()
    );
}
}