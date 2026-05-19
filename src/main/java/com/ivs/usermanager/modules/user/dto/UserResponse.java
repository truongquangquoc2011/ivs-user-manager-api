package com.ivs.usermanager.modules.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserResponse {

    private Integer id;

    private String email;

    private String fullname;

    private String phoneNumber;

    private String status;

    private List<UserGroupResponse> groups;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}