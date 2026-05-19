package com.ivs.usermanager.modules.group.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUserResponse {

    private Integer id;

    private String email;

    private String fullname;

    private String phoneNumber;

    private String status;
}