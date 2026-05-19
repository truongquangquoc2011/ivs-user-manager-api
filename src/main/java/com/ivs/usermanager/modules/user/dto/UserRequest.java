package com.ivs.usermanager.modules.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    private String email;

    private String password;

    private String fullname;

    private String phoneNumber;

    private String status;

    private List<Integer> groupIds;
}