package com.farmconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FarmerRegistrationRequest {

    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Full legal name is required (stored confidentially)")
    private String fullName;

    @NotBlank(message = "Contact phone number is required (stored confidentially)")
    private String phone;

    private String idProof;

    private String farmName;

    private String regionCode;

    private String exactAddress;

    private Integer farmingExperienceYears;

    private Boolean isOrganicCertified = false;

    private String bio;

    public FarmerRegistrationRequest() {
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

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getExactAddress() {
        return exactAddress;
    }

    public void setExactAddress(String exactAddress) {
        this.exactAddress = exactAddress;
    }

    public Integer getFarmingExperienceYears() {
        return farmingExperienceYears;
    }

    public void setFarmingExperienceYears(Integer farmingExperienceYears) {
        this.farmingExperienceYears = farmingExperienceYears;
    }

    public Boolean getIsOrganicCertified() {
        return isOrganicCertified;
    }

    public void setIsOrganicCertified(Boolean organicCertified) {
        isOrganicCertified = organicCertified;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
