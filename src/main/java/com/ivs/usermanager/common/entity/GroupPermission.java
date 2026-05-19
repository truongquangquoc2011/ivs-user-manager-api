package com.ivs.usermanager.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "group_permissions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"group_id", "feature_id"})
    }
)
@Getter
@Setter
public class GroupPermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    @Column(name = "can_view")
    private Boolean canView = false;

    @Column(name = "can_edit")
    private Boolean canEdit = false;
}