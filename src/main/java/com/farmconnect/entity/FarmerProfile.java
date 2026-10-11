package com.farmconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "farmer_profiles")
public class FarmerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String farmName;

    /**
     * Anonymized regional identifier exposed publicly instead of the farmer's physical address.
     * e.g., "Region-North-Plateau-3"
     */
    @Column(nullable = false)
    private String anonymizedRegionCode;

    /**
     * Strictly Private PII: Exact street address, village, or GPS coordinates.
     * NEVER returned to public buyers or unmasked APIs.
     */
    @Column(nullable = false)
    private String exactAddress;

    private Integer farmingExperienceYears;

    @Column(nullable = false)
    private Boolean isOrganicCertified = false;

    @Column(length = 1000)
    private String bio;

    public FarmerProfile() {
    }

    public FarmerProfile(User user, String farmName, String anonymizedRegionCode, String exactAddress, Integer farmingExperienceYears, Boolean isOrganicCertified, String bio) {
        this.user = user;
        this.farmName = farmName;
        this.anonymizedRegionCode = anonymizedRegionCode;
        this.exactAddress = exactAddress;
        this.farmingExperienceYears = farmingExperienceYears;
        this.isOrganicCertified = isOrganicCertified != null ? isOrganicCertified : false;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
