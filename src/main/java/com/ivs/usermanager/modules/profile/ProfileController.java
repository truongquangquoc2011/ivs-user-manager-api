package com.ivs.usermanager.modules.profile;

import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.modules.profile.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.ivs.usermanager.modules.profile.dto.UpdateProfileRequest;
import com.ivs.usermanager.modules.profile.dto.ChangePasswordRequest;
import com.ivs.usermanager.modules.profile.dto.AvatarUploadResponse;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
            Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .success(true)
                        .message("Get profile successfully")
                        .data(profileService.getProfile(email))
                        .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .success(true)
                        .message("Update profile successfully")
                        .data(profileService.updateProfile(email, request))
                        .build());
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {
        String email = authentication.getName();

        profileService.changePassword(email, request);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Change password successfully")
                        .build());
    }

    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<AvatarUploadResponse>> uploadAvatar(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<AvatarUploadResponse>builder()
                        .success(true)
                        .message("Upload avatar successfully")
                        .data(profileService.uploadAvatar(email, file))
                        .build());
    }
}