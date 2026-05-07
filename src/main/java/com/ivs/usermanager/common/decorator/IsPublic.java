package com.ivs.usermanager.common.decorator;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
/* Allow all users to access the annotated method or class without authentication */
@PreAuthorize("permitAll()") 
public @interface IsPublic {
}