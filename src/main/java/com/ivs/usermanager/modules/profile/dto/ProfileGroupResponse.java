package com.ivs.usermanager.modules.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileGroupResponse {

    private Integer id;

    private String name;

    private String description;

    private Boolean isActive;
}