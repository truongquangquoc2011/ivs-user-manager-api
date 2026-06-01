package com.ivs.usermanager.modules.profile;

import com.ivs.usermanager.common.dto.ApiResponse;
import com.ivs.usermanager.modules.profile.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .success(true)
                        .message("Get profile successfully")
                        .data(profileService.getProfile(email))
                        .build()
        );
    }
}