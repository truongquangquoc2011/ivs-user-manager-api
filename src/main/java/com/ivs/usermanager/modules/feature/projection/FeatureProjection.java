package com.ivs.usermanager.modules.feature.projection;

import java.time.LocalDateTime;

public interface FeatureProjection {

    Integer getId();

    String getCode();

    String getName();

    String getPath();

    Boolean getIsActive();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}