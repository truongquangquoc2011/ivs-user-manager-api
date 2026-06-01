package com.ivs.usermanager.common.decorator;

import com.ivs.usermanager.common.enums.PermissionAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    String feature();

    PermissionAction action();
}