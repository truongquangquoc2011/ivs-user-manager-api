package com.ivs.usermanager.modules.profile.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String fullname;

    private String phoneNumber;

    private String avatar;
}