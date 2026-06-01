package com.ivs.usermanager.modules.user;

import com.ivs.usermanager.modules.user.dto.UserGroupResponse;
import com.ivs.usermanager.modules.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.common.entity.UserGroup;
import com.ivs.usermanager.common.enums.UserStatus;
import com.ivs.usermanager.modules.group.GroupRepository;
import com.ivs.usermanager.modules.group.UserGroupRepository;
import com.ivs.usermanager.modules.user.dto.UserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllUsers()
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullname(user.getFullname())
                        .phoneNumber(user.getPhoneNumber())
                        .status(user.getStatus())
                        .groups(getGroupsByUserId(user.getId()))
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build())
                .toList();
    }

    public UserResponse getUserById(Integer id) {
        var user = userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .groups(getGroupsByUserId(user.getId()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private List<UserGroupResponse> getGroupsByUserId(Integer userId) {
        return userRepository.findGroupsByUserId(userId)
                .stream()
                .map(group -> UserGroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .isActive(group.getIsActive())
                        .build())
                .toList();
    }

    public UserResponse createUser(UserRequest request) {

        if (userRepository.countByEmail(request.getEmail()) > 0) {
            throw new RuntimeException("Email already exists");
        }

        var user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(
                request.getStatus() != null
                        ? UserStatus.valueOf(request.getStatus())
                        : UserStatus.ACTIVE);

        userRepository.save(user);

        syncUserGroups(user, request.getGroupIds());

        return getUserById(user.getId());
    }

    public UserResponse updateUser(Integer id, UserRequest request) {

        var user = userRepository.findActiveEntityById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRepository.countByEmailExceptId(request.getEmail(), id) > 0) {
            throw new RuntimeException("Email already exists");
        }

        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        user.setPhoneNumber(request.getPhoneNumber());

        if (request.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus()));
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        syncUserGroups(user, request.getGroupIds());

        return getUserById(user.getId());
    }

    public void deleteUser(Integer id) {
    var user = userRepository.findActiveEntityById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setDeletedAt(LocalDateTime.now());

    user.setEmail(user.getEmail() + "_deleted_" + System.currentTimeMillis());

    userRepository.save(user);
}

    private void syncUserGroups(User user, List<Integer> groupIds) {

        if (groupIds == null) {
            return;
        }

        // Xóa mềm toàn bộ group cũ của user
        userGroupRepository.softDeleteAllByUserId(user.getId());

        // Thêm lại các group mới
        for (Integer groupId : groupIds) {

            var group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Group not found: " + groupId));

            var userGroup = new UserGroup();
            userGroup.setUser(user);
            userGroup.setGroup(group);

            userGroupRepository.save(userGroup);
        }
    }
}