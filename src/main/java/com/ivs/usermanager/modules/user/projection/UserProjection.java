package com.ivs.usermanager.modules.user.projection;

import java.time.LocalDateTime;

public interface UserProjection {

    Integer getId();

    String getEmail();

    String getFullname();

    String getPhoneNumber();

    String getStatus();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}