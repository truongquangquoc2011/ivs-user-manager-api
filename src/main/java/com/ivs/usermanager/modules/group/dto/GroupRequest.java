package com.ivs.usermanager.modules.group.dto;

import lombok.Data;

@Data
public class GroupRequest {

    private String name;

    private String description;

    private Boolean isActive;
}