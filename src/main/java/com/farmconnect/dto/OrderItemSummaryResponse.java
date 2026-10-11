package com.farmconnect.dto;

import com.farmconnect.entity.OrderItemStatus;

import java.math.BigDecimal;

public class OrderItemSummaryResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal subtotal;
    private OrderItemStatus itemStatus;

    public OrderItemSummaryResponse() {
    }

    public OrderItemSummaryResponse(Long id, Long productId, String productName, String unit,
                                    BigDecimal unitPrice, BigDecimal quantity, BigDecimal subtotal,
                                    OrderItemStatus itemStatus) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.itemStatus = itemStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public OrderItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(OrderItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }
}
