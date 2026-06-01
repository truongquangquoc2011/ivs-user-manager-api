package com.ivs.usermanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivs.usermanager.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handleUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.<Object>builder()
                        .success(false)
                        .message("Unauthorized: Please login first")
                        .data(null)
                        .build()
        ));
    }

    public void handleForbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.<Object>builder()
                        .success(false)
                        .message("Forbidden: You do not have permission to access this resource")
                        .data(null)
                        .build()
        ));
    }
}