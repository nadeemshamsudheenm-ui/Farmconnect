package com.farmconnect.dto;

import com.farmconnect.entity.OrderItemStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FarmerOrderItemResponse {
    private Long orderItemId;
    private Long orderId;
    private String orderTrackingNumber;
    private LocalDateTime orderDate;
    private Long productId;
    private String productName;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private OrderItemStatus itemStatus;
    private String deliveryAddress;
    private String deliveryContactNumber;
    private String notes;

    public FarmerOrderItemResponse() {
    }

    public FarmerOrderItemResponse(Long orderItemId, Long orderId, String orderTrackingNumber,
                                   LocalDateTime orderDate, Long productId, String productName,
                                   BigDecimal quantity, String unit, BigDecimal unitPrice,
                                   BigDecimal subtotal, OrderItemStatus itemStatus,
                                   String deliveryAddress, String deliveryContactNumber, String notes) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderDate = orderDate;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.itemStatus = itemStatus;
        this.deliveryAddress = deliveryAddress;
        this.deliveryContactNumber = deliveryContactNumber;
        this.notes = notes;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }

    public void setOrderTrackingNumber(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryContactNumber() {
        return deliveryContactNumber;
    }

    public void setDeliveryContactNumber(String deliveryContactNumber) {
        this.deliveryContactNumber = deliveryContactNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
