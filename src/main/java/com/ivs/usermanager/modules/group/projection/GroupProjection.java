package com.ivs.usermanager.modules.group.projection;

import java.time.LocalDateTime;

public interface GroupProjection {

    Integer getId();

    String getName();

    String getDescription();

    Boolean getIsActive();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}