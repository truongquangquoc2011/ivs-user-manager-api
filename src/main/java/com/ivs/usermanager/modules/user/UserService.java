package com.ivs.usermanager.modules.user;

import com.ivs.usermanager.modules.user.dto.UserGroupResponse;
import com.ivs.usermanager.modules.user.dto.UserResponse;
import com.ivs.usermanager.modules.user.projection.UserGroupProjection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.common.entity.UserGroup;
import com.ivs.usermanager.common.enums.UserStatus;
import com.ivs.usermanager.modules.group.GroupRepository;
import com.ivs.usermanager.modules.group.UserGroupRepository;
import com.ivs.usermanager.modules.user.dto.UserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ivs.usermanager.common.dto.PaginationResponse;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all users.
     *
     * @return list of users
     */
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

    /**
     * Retrieves a user by ID.
     *
     * @param id user ID
     * @return user details
     */
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

    /**
     * Retrieves groups of a user.
     *
     * @param userId user ID
     * @return list of groups
     */
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

    /**
     * Creates a user.
     *
     * @param request user data
     * @return created user
     */
    public UserResponse createUser(UserRequest request) {

        if (userRepository.countByEmail(request.getEmail()) > 0) {
            throw new RuntimeException("Email already exists");
        }

        var user = new User();

        user.setEmail(request.getEmail());

        // Store password in encrypted form
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

    /**
     * Updates a user.
     *
     * @param id user ID
     * @param request user data
     * @return updated user
     */
    public UserResponse updateUser(Integer id, UserRequest request) {

        var user = userRepository.findActiveEntityById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {

            // Exclude current user when checking duplicate email
            if (userRepository.countByEmailExceptId(request.getEmail(), id) > 0) {
                throw new RuntimeException("Email already exists");
            }

            user.setEmail(request.getEmail());
        }

        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus()));
        }

        if (request.getPassword() != null) {
            // Store password in encrypted form
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        if (request.getGroupIds() != null) {
            syncUserGroups(user, request.getGroupIds());
        }

        return getUserById(user.getId());
    }

    /**
     * Deletes a user.
     *
     * @param id user ID
     */
    public void deleteUser(Integer id) {
        var user = userRepository.findActiveEntityById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDeletedAt(LocalDateTime.now());

        // Make email unique after soft delete
        user.setEmail(user.getEmail() + "_deleted_" + System.currentTimeMillis());

        userRepository.save(user);
    }

    /**
     * Synchronizes user group.
     *
     * @param user user entity
     * @param groupIds group IDs
     */
    private void syncUserGroups(User user, List<Integer> groupIds) {

        if (groupIds == null || groupIds.isEmpty()) {
            return;
        }

        // Only one group is supported
        Integer newGroupId = groupIds.get(0);

        List<UserGroupProjection> currentGroups = userRepository.findGroupsByUserId(user.getId());

        boolean sameGroup = currentGroups.stream()
                .anyMatch(group -> group.getId().equals(newGroupId));

        if (sameGroup) {
            return;
        }

        // Remove old group relationships before assigning a new one
        userGroupRepository.softDeleteAllByUserId(user.getId());

        var group = groupRepository.findById(newGroupId)
                .orElseThrow(() -> new RuntimeException("Group not found: " + newGroupId));

        var oldUserGroup = userGroupRepository.findAnyByUserIdAndGroupId(
                user.getId(),
                newGroupId);

        if (oldUserGroup.isPresent()) {

            // Restore soft-deleted relationship
            var userGroup = oldUserGroup.get();
            userGroup.setDeletedAt(null);
            userGroupRepository.save(userGroup);
            return;
        }

        var userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);

        userGroupRepository.save(userGroup);
    }

    /**
     * Retrieves users with pagination.
     *
     * @param skip number of records to skip
     * @param take number of records to retrieve
     * @return paginated users
     */
    public PaginationResponse<UserResponse> getAllUsersPaging(Integer skip, Integer take) {

        if (skip == null || skip < 0) {
            skip = 0;
        }

        if (take == null || take <= 0) {
            take = 10;
        }

        // Limit page size to prevent large queries
        if (take > 100) {
            take = 100;
        }

        var items = userRepository.findAllUsersPaging(skip, take)
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

        return PaginationResponse.<UserResponse>builder()
                .items(items)
                .total(userRepository.countAllUsers())
                .skip(skip)
                .take(take)
                .build();
    }
}