package com.farmconnect.dto;

import com.farmconnect.entity.Role;

public class AuthResponse {
    private Long userId;
    private String email;
    private String fullName;
    private Role role;
    private Long farmerProfileId;
    private String farmName;
    private String anonymizedRegionCode;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(Long userId, String email, String fullName, Role role,
                        Long farmerProfileId, String farmName, String anonymizedRegionCode, String message) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.farmerProfileId = farmerProfileId;
        this.farmName = farmName;
        this.anonymizedRegionCode = anonymizedRegionCode;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getFarmerProfileId() {
        return farmerProfileId;
    }

    public void setFarmerProfileId(Long farmerProfileId) {
        this.farmerProfileId = farmerProfileId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getAnonymizedRegionCode() {
        return anonymizedRegionCode;
    }

    public void setAnonymizedRegionCode(String anonymizedRegionCode) {
        this.anonymizedRegionCode = anonymizedRegionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
