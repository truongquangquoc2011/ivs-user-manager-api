package com.ivs.usermanager.modules.auth.projection;

public interface AuthUserProjection {

    Integer getId();

    String getEmail();

    String getPassword();

    String getFullname();

    String getPhoneNumber();

    String getStatus();
}