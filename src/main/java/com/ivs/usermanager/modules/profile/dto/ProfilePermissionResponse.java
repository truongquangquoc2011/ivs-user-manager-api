package com.ivs.usermanager.modules.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilePermissionResponse {

    private String featureCode;

    private String featureName;

    private String featurePath;

    private Boolean canView;

    private Boolean canEdit;
}