package com.ivs.usermanager.modules.profile.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileResponse {

    private Integer id;

    private String email;

    private String fullname;

    private String phoneNumber;

    private String status;

    private List<ProfileGroupResponse> groups;

    private List<ProfilePermissionResponse> permissions;
}