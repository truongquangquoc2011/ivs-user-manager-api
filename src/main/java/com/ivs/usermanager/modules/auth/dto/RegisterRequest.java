package com.ivs.usermanager.modules.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullname;
    private String phoneNumber;
    private String role;
}