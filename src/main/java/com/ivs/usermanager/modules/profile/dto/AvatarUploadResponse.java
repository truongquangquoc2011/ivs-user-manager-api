package com.ivs.usermanager.modules.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvatarUploadResponse {

    private String avatar;
}