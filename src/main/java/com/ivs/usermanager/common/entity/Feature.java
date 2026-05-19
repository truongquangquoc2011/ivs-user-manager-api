package com.ivs.usermanager.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "features")
@Getter
@Setter
public class Feature extends BaseEntity {

    @Column(unique = true, nullable = false, length = 255)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 500)
    private String path;

    @Column(name = "is_active")
    private Boolean isActive = true;
}