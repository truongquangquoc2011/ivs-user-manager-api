package com.ivs.usermanager.modules.feature.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeatureResponse {

    private Integer id;

    private String code;

    private String name;

    private String path;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}