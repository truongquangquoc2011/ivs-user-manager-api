package com.ivs.usermanager.modules.auth;

import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.common.enums.UserStatus;
import com.ivs.usermanager.modules.auth.dto.AuthResponse;
import com.ivs.usermanager.modules.auth.dto.LoginRequest;
import com.ivs.usermanager.modules.auth.dto.RegisterRequest;
import com.ivs.usermanager.modules.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request) {
        /* Check if email already exists */
        if (authRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        /* 1. CRITICAL: Validate if the role exists in the database */
        var role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Error: Role '" + request.getRole() + "' does not exist in the database"));

        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setStatus(UserStatus.ACTIVE);
        
        /* 2. Map the validated role to the user entity */
        user.setRole(role);

        authRepository.save(user);
        return "User registered successfully with role: " + role.getName();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        /* 3. Extract role name and put it into JWT extra claims */
        Map<String, Object> extraClaims = new HashMap<>();
        if (user.getRole() != null) {
            extraClaims.put("role", user.getRole().getName());
        }

        var jwtToken = jwtService.generateToken(extraClaims, user.getEmail());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .build();
    }
}