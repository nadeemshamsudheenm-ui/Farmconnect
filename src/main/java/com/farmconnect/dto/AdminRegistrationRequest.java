package com.farmconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdminRegistrationRequest {

    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Platform Administrator full name is required")
    private String fullName;

    @NotBlank(message = "Contact phone number is required")
    private String phone;

    @NotBlank(message = "Admin authorization code is required")
    private String adminCode;

    public AdminRegistrationRequest() {
    }

    public AdminRegistrationRequest(String fullName, String phone, String password, String adminCode, String email) {
        this.fullName = fullName;
        this.phone = phone;
        this.password = password;
        this.adminCode = adminCode;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }
}
