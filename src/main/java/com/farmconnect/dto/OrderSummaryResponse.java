package com.farmconnect.dto;

import com.farmconnect.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryResponse {
    private Long orderId;
    private String orderTrackingNumber;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private String deliveryContactNumber;
    private LocalDateTime orderDate;
    private String notes;
    private List<OrderItemSummaryResponse> items;

    public OrderSummaryResponse() {
    }

    public OrderSummaryResponse(Long orderId, String orderTrackingNumber, OrderStatus orderStatus,
                                BigDecimal totalAmount, String deliveryAddress, String deliveryContactNumber,
                                LocalDateTime orderDate, String notes, List<OrderItemSummaryResponse> items) {
        this.orderId = orderId;
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.deliveryContactNumber = deliveryContactNumber;
        this.orderDate = orderDate;
        this.notes = notes;
        this.items = items;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderItemSummaryResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemSummaryResponse> items) {
        this.items = items;
    }
}
