package com.ivs.usermanager.modules.profile;

import com.ivs.usermanager.modules.profile.dto.ProfileGroupResponse;
import com.ivs.usermanager.modules.profile.dto.ProfilePermissionResponse;
import com.ivs.usermanager.modules.profile.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ivs.usermanager.modules.profile.dto.UpdateProfileRequest;
import com.ivs.usermanager.modules.profile.dto.ChangePasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ivs.usermanager.common.service.CloudinaryService;
import com.ivs.usermanager.modules.profile.dto.AvatarUploadResponse;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

        private final ProfileRepository profileRepository;
        private final PasswordEncoder passwordEncoder;
        private final CloudinaryService cloudinaryService;

        /**
         * Retrieves profile information.
         *
         * @param email user email
         * @return profile details
         */
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
                                                // Prevent NullPointerException when database value is null
                                                .canView(Boolean.TRUE.equals(permission.getCanView()))
                                                .canEdit(Boolean.TRUE.equals(permission.getCanEdit()))
                                                .build())
                                .toList();

                return ProfileResponse.builder()
                                .id(profile.getId())
                                .email(profile.getEmail())
                                .fullname(profile.getFullname())
                                .phoneNumber(profile.getPhoneNumber())
                                .avatar(profile.getAvatar())
                                .status(profile.getStatus())
                                .groups(groups)
                                .permissions(permissions)
                                .build();
        }

        /**
         * Updates profile information.
         *
         * @param email   user email
         * @param request profile data
         * @return updated profile
         */
        public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {

                var user = profileRepository.findActiveEntityByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));

                if (request.getFullname() != null) {
                        user.setFullname(request.getFullname().trim());
                }

                if (request.getPhoneNumber() != null) {
                        user.setPhoneNumber(request.getPhoneNumber().trim());
                }

                if (request.getAvatar() != null) {
                        user.setAvatar(request.getAvatar().trim());
                }

                profileRepository.save(user);

                return getProfile(email);
        }

        /**
         * Changes the user password.
         *
         * @param email   user email
         * @param request password data
         */
        public void changePassword(String email, ChangePasswordRequest request) {

                var user = profileRepository.findActiveEntityByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));

                if (request.getOldPassword() == null || request.getOldPassword().isBlank()) {
                        throw new RuntimeException("Old password is required");
                }

                if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
                        throw new RuntimeException("New password is required");
                }

                if (request.getNewPassword().length() < 6) {
                        throw new RuntimeException("New password must be at least 6 characters");
                }

                if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                        throw new RuntimeException("Confirm password does not match");
                }
                // Compare raw password with encrypted password
                if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                        throw new RuntimeException("Old password is incorrect");
                }
                // Store password in encrypted form
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));

                profileRepository.save(user);
        }

        /**
         * Uploads a user avatar.
         *
         * @param email user email
         * @param file  avatar file
         * @return uploaded avatar information
         */
        public AvatarUploadResponse uploadAvatar(String email, MultipartFile file) {

                var user = profileRepository.findActiveEntityByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));
                // Upload file to Cloudinary and get public URL
                String avatarUrl = cloudinaryService.uploadAvatar(file);

                user.setAvatar(avatarUrl);

                profileRepository.save(user);

                return AvatarUploadResponse.builder()
                                .avatar(avatarUrl)
                                .build();
        }
}