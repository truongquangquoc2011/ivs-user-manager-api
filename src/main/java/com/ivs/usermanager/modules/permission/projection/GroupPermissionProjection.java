package com.ivs.usermanager.modules.permission.projection;

public interface GroupPermissionProjection {

    Integer getFeatureId();

    String getFeatureCode();

    String getFeatureName();

    String getFeaturePath();

    Integer getCanView();

    Integer getCanEdit();
}