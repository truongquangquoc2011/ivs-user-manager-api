package com.ivs.usermanager.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /* User email used as the principal for authentication */
    private String email;
    
    /* Plain text password provided by the user */
    private String password;
}