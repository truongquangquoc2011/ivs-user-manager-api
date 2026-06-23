package com.ivs.usermanager.modules.auth;

import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.common.entity.UserGroup;
import com.ivs.usermanager.common.enums.UserStatus;
import com.ivs.usermanager.modules.auth.dto.AuthResponse;
import com.ivs.usermanager.modules.auth.dto.LoginRequest;
import com.ivs.usermanager.modules.auth.dto.RegisterRequest;
import com.ivs.usermanager.modules.group.GroupRepository;
import com.ivs.usermanager.modules.group.UserGroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public String register(RegisterRequest request) {

        if (authRepository.countActiveEmail(request.getEmail()) > 0) {
            throw new RuntimeException("Email is already taken");
        }

        if (request.getGroupId() == null) {
            throw new RuntimeException("Group is required");
        }

        var user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(UserStatus.ACTIVE);

        var savedUser = authRepository.save(user);

        var group = groupRepository.findActiveEntityById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found: " + request.getGroupId()));

        var userGroup = new UserGroup();

        userGroup.setUser(savedUser);
        userGroup.setGroup(group);

        userGroupRepository.save(userGroup);

        return "User registered successfully";
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = authRepository.findAuthUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!UserStatus.ACTIVE.name().equals(user.getStatus())) {
            throw new RuntimeException("User is not active");
        }

        var jwtToken = jwtService.generateToken(new HashMap<>(), user.getEmail());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .build();
    }
}