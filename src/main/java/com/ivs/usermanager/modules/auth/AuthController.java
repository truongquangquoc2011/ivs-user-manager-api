package com.ivs.usermanager.modules.auth;

import com.ivs.usermanager.common.decorator.IsPublic;
import com.ivs.usermanager.common.dto.ApiResponse;
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

    @PostMapping("/register")
    @IsPublic
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(
            ApiResponse.builder()
                .message("User registered successfully")
                .success(true)
                .build()
        );
    }

    @PostMapping("/login")
    @IsPublic
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
            ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .data(response)
                .success(true)
                .build()
        );
    }
}