package com.ivs.usermanager.modules.auth;

import com.ivs.usermanager.common.decorator.IsPublic;
import com.ivs.usermanager.common.decorator.RequirePermission;
import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.common.enums.PermissionAction;
import com.ivs.usermanager.modules.auth.dto.AuthResponse;
import com.ivs.usermanager.modules.auth.dto.LoginRequest;
import com.ivs.usermanager.modules.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * * Registers a new user. * * @param request registration request data
     * * @return success response
     */
    @PostMapping("/register")
    @RequirePermission(feature = "USER_CREATION", action = PermissionAction.EDIT)
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(
                ApiResponse.<Object>builder()
                        .message("User registered successfully")
                        .success(true)
                        .build());
    }

    /**
     * * Authenticates a user and returns an access token.
     * * @param request login
     * credentials * @return authentication response
     */
    @PostMapping("/login")
    @IsPublic
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .message("Login successful")
                        .data(response)
                        .success(true)
                        .build());
    }
}