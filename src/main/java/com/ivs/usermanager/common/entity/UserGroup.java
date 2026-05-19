package com.ivs.usermanager.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "user_groups",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "group_id"})
    }
)
@Getter
@Setter
public class UserGroup extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}