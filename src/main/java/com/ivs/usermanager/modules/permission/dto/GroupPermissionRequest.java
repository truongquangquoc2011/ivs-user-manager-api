package com.ivs.usermanager.modules.permission.dto;

import lombok.Data;

@Data
public class GroupPermissionRequest {

    private Integer featureId;

    private Boolean canView;

    private Boolean canEdit;
}