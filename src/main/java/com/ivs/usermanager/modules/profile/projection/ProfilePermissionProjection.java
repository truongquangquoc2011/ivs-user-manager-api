package com.ivs.usermanager.modules.profile.projection;

public interface ProfilePermissionProjection {

    String getFeatureCode();

    String getFeatureName();

    String getFeaturePath();

    Boolean getCanView();

    Boolean getCanEdit();
}