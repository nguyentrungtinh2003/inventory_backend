package com.TrungTinhBackend.inventory_backend.dto;

import com.TrungTinhBackend.inventory_backend.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    private String token;
    private UserRole role;
    private String expirationTime;
}
