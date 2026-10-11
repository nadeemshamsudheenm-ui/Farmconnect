package com.farmconnect.dto;

import com.farmconnect.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductCreateUpdateRequest {

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Product category is required")
    private ProductCategory category;

    private String description;

    @NotBlank(message = "Unit of measure is required (e.g. kg, crate)")
    private String unit;

    @NotNull(message = "Price per unit is required")
    @Positive(message = "Price per unit must be greater than zero")
    private BigDecimal pricePerUnit;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity must be non-negative")
    private BigDecimal stockQuantity;

    private LocalDate harvestDate;

    private Boolean isOrganicCertified = false;

    private String imageUrl;

    public ProductCreateUpdateRequest() {
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
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
}
