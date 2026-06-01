package com.ivs.usermanager.modules.profile;

import com.ivs.usermanager.modules.profile.dto.ProfileGroupResponse;
import com.ivs.usermanager.modules.profile.dto.ProfilePermissionResponse;
import com.ivs.usermanager.modules.profile.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileResponse getProfile(String email) {

        var profile = profileRepository.findProfileByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        var groups = profileRepository.findGroupsByEmail(email)
                .stream()
                .map(group -> ProfileGroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .isActive(group.getIsActive())
                        .build())
                .toList();

        var permissions = profileRepository.findPermissionsByEmail(email)
                .stream()
                .map(permission -> ProfilePermissionResponse.builder()
                        .featureCode(permission.getFeatureCode())
                        .featureName(permission.getFeatureName())
                        .featurePath(permission.getFeaturePath())
                        .canView(Boolean.TRUE.equals(permission.getCanView()))
                        .canEdit(Boolean.TRUE.equals(permission.getCanEdit()))
                        .build())
                .toList();

        return ProfileResponse.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .fullname(profile.getFullname())
                .phoneNumber(profile.getPhoneNumber())
                .status(profile.getStatus())
                .groups(groups)
                .permissions(permissions)
                .build();
    }
}