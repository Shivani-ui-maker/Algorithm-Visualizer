package com.algo.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request body for guest onboarding with a desired username only.
 */
public class GuestOnboardRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters long")
    @Pattern(
        regexp = "^[A-Za-z0-9._]+$",
        message = "Username can contain only letters, numbers, underscores, and dots"
    )
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}