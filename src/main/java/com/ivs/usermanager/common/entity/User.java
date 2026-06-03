package com.ivs.usermanager.common.entity;

import com.ivs.usermanager.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 500)
    private String password;

    @Column(length = 500)
    private String fullname;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserStatus status = UserStatus.INACTIVE;
}