package com.ivs.usermanager.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter @Setter
public class Role extends BaseEntity {
    @Column(unique = true, nullable = false, length = 500)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Một Role có nhiều User
    @OneToMany(mappedBy = "role")
    private Set<User> users;
}