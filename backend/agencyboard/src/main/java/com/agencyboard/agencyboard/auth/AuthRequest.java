package com.agencyboard.agencyboard.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}