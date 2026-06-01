package com.ivs.usermanager.modules.permission.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupPermissionResponse {

    private Integer featureId;

    private String featureCode;

    private String featureName;

    private String featurePath;

    private Boolean canView;

    private Boolean canEdit;
}