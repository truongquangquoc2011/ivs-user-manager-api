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

    /**
     * Retrieves the current user's profile.
     *
     * @param authentication authenticated user
     * @return profile details
     */
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

    /**
     * Updates the current user's profile.
     *
     * @param authentication authenticated user
     * @param request profile data
     * @return updated profile
     */
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

    /**
     * Changes the current user's password.
     *
     * @param authentication authenticated user
     * @param request password data
     * @return operation result
     */
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

    /**
     * Uploads a user avatar.
     *
     * @param authentication authenticated user
     * @param file avatar file
     * @return uploaded avatar information
     */
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