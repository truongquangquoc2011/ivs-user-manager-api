package com.ivs.usermanager.common.entity;

import com.ivs.usermanager.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 500)
    private String password;

    @Column(length = 500)
    private String fullname;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.INACTIVE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}