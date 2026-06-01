package com.ivs.usermanager.modules.feature.dto;

import lombok.Data;

@Data
public class FeatureRequest {

    private String code;

    private String name;

    private String path;

    private Boolean isActive;
}