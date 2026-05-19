package com.ivs.usermanager.modules.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGroupResponse {

    private Integer id;

    private String name;

    private String description;

    private Boolean isActive;
}