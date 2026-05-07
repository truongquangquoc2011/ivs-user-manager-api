package com.ivs.usermanager.modules.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    /* This endpoint requires a valid JWT token in the Authorization header */
    @GetMapping("/profile")
    public ResponseEntity<String> getMyProfile() {
        return ResponseEntity.ok("If you see this, your JWT token is valid!");
    }
}