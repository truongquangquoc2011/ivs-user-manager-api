package com.ivs.usermanager.modules.group;

import com.ivs.usermanager.common.entity.Group;
import com.ivs.usermanager.modules.group.dto.GroupRequest;
import com.ivs.usermanager.modules.group.dto.GroupResponse;
import com.ivs.usermanager.common.entity.User;
import com.ivs.usermanager.common.entity.UserGroup;
import com.ivs.usermanager.modules.group.dto.GroupUserResponse;
import com.ivs.usermanager.modules.auth.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserGroupRepository userGroupRepository;
    private final AuthRepository authRepository;
    private final GroupRepository groupRepository;

    /**
     * Retrieves all groups.
     *
     * @return list of groups
     */
    public List<GroupResponse> getAllGroups() {

        return groupRepository.findAllGroups()
                .stream()
                .map(group -> GroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .isActive(group.getIsActive())
                        .createdAt(group.getCreatedAt())
                        .updatedAt(group.getUpdatedAt())
                        .build())
                .toList();
    }

    /**
     * Creates a new group.
     *
     * @param request group data
     * @return created group
     */
    public GroupResponse createGroup(GroupRequest request) {

        if (groupRepository.countByName(request.getName()) > 0) {
            throw new RuntimeException("Group name already exists");
        }

        var group = new Group();

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setIsActive(
                request.getIsActive() != null
                        ? request.getIsActive()
                        : true);

        groupRepository.save(group);

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .isActive(group.getIsActive())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }

    /**
     * Updates a group by ID.
     *
     * @param id group ID
     * @param request updated group data
     * @return updated group
     */
    public GroupResponse updateGroup(Integer id, GroupRequest request) {

        var group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setName(request.getName());
        group.setDescription(request.getDescription());

        if (request.getIsActive() != null) {
            group.setIsActive(request.getIsActive());
        }

        groupRepository.save(group);

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .isActive(group.getIsActive())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }

    /**
     * Deletes a group by ID.
     *
     * @param id group ID
     */
    public void deleteGroup(Integer id) {

        var group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setDeletedAt(LocalDateTime.now());

        groupRepository.save(group);
    }

    /**
     * Retrieves users in a group.
     *
     * @param groupId group ID
     * @return list of users
     */
    public List<GroupUserResponse> getUsersInGroup(Integer groupId) {

    groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

    return userGroupRepository.findUsersByGroupId(groupId)
            .stream()
            .map(user -> GroupUserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullname(user.getFullname())
                    .phoneNumber(user.getPhoneNumber())
                    .status(user.getStatus())
                    .build())
            .toList();
}

    /**
     * Adds a user to a group.
     *
     * @param groupId group ID
     * @param userId user ID
     */
public void addUserToGroup(Integer groupId, Integer userId) {

    var group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

    var user = authRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

         if (userGroupRepository.countActiveUserInGroup(groupId, userId) > 0) {
        throw new RuntimeException("User already exists in this group");
    }

    var userGroup = new UserGroup();
    userGroup.setGroup(group);
    userGroup.setUser(user);

    userGroupRepository.save(userGroup);
}

    /**
     * Removes a user from a group.
     *
     * @param groupId group ID
     * @param userId user ID
     */
    public void removeUserFromGroup(Integer groupId, Integer userId) {

    groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

    authRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (userGroupRepository.countActiveUserInGroup(groupId, userId) == 0) {
        throw new RuntimeException("User is not in this group");
    }

    userGroupRepository.softDeleteUserFromGroup(groupId, userId);
}
}