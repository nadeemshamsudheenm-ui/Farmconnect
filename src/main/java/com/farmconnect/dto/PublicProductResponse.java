package com.farmconnect.dto;

import com.farmconnect.entity.ProductCategory;
import com.farmconnect.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Public catalog projection with STRICT privacy protection.
 * Farmer personal name, phone number, and physical address are NEVER exposed to buyers.
 */
public class PublicProductResponse {
    private Long id;
    private String name;
    private ProductCategory category;
    private String description;
    private String unit;
    private BigDecimal pricePerUnit;
    private BigDecimal stockQuantity;
    private LocalDate harvestDate;
    private Boolean isOrganicCertified;
    private String imageUrl;
    private ProductStatus status;
    private String anonymizedRegion;
    private String trustBadge;

    public PublicProductResponse() {
    }

    public PublicProductResponse(Long id, String name, ProductCategory category, String description,
                                 String unit, BigDecimal pricePerUnit, BigDecimal stockQuantity,
                                 LocalDate harvestDate, Boolean isOrganicCertified, String imageUrl,
                                 ProductStatus status, String anonymizedRegion, String trustBadge) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.stockQuantity = stockQuantity;
        this.harvestDate = harvestDate;
        this.isOrganicCertified = isOrganicCertified;
        this.imageUrl = imageUrl;
        this.status = status;
        this.anonymizedRegion = anonymizedRegion;
        this.trustBadge = trustBadge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public Boolean getIsOrganicCertified() {
        return isOrganicCertified;
    }

    public void setIsOrganicCertified(Boolean organicCertified) {
        isOrganicCertified = organicCertified;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getAnonymizedRegion() {
        return anonymizedRegion;
    }

    public void setAnonymizedRegion(String anonymizedRegion) {
        this.anonymizedRegion = anonymizedRegion;
    }

    public String getTrustBadge() {
        return trustBadge;
    }

    public void setTrustBadge(String trustBadge) {
        this.trustBadge = trustBadge;
    }
}
