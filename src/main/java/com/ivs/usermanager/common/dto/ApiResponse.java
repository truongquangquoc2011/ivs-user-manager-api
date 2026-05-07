package com.ivs.usermanager.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    /* Descriptive message for the client */
    private String message;
    
    /* Payload containing the actual data (optional) */
    private T data;
    
    /* Success status flag */
    private boolean success;
}