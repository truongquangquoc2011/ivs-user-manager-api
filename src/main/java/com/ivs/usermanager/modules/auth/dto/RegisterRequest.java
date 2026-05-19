package com.ivs.usermanager.modules.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegisterRequest {

    private String email;

    private String password;

    private String fullname;

    private String phoneNumber;

    /*
     * Danh sách group user thuộc vào
     * VD: ["Admin", "HR"]
     */
    private List<String> groups;
}