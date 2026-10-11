package com.farmconnect.controller;

import com.farmconnect.dto.FarmerOrderItemResponse;
import com.farmconnect.dto.OrderPlacementRequest;
import com.farmconnect.dto.OrderSummaryResponse;
import com.farmconnect.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Buyer creates a new purchase order.
     */
    @PostMapping
    public ResponseEntity<OrderSummaryResponse> placeOrder(@Valid @RequestBody OrderPlacementRequest request) {
        OrderSummaryResponse response = orderService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Buyer views their own order history.
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OrderSummaryResponse>> getBuyerOrders(@PathVariable Long buyerId) {
        List<OrderSummaryResponse> orders = orderService.getBuyerOrders(buyerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Track an order by tracking code.
     */
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<OrderSummaryResponse> trackOrder(@PathVariable String trackingNumber) {
        OrderSummaryResponse order = orderService.getOrderByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(order);
    }

    /**
     * Admin or authorized user views all system purchase orders.
     */
    @GetMapping("/all")
    public ResponseEntity<List<OrderSummaryResponse>> getAllOrders() {
        List<OrderSummaryResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Admin views all item fulfillment requests across all farms.
     */
    @GetMapping("/items/all")
    public ResponseEntity<List<FarmerOrderItemResponse>> getAllOrderItems() {
        List<FarmerOrderItemResponse> items = orderService.getAllOrderItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Farmer receives incoming item fulfillment requests (or 0 for all farms / Admin).
     */
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<FarmerOrderItemResponse>> getFarmerOrderItems(@PathVariable Long farmerId) {
        List<FarmerOrderItemResponse> items = orderService.getFarmerOrderItems(farmerId);
        return ResponseEntity.ok(items);
    }

    /**
     * Farmer or Admin confirms an item fulfillment.
     */
    @PutMapping("/items/{orderItemId}/confirm")
    public ResponseEntity<FarmerOrderItemResponse> confirmOrderItem(
            @PathVariable Long orderItemId,
            @RequestParam(required = false, defaultValue = "0") Long farmerId) {
        FarmerOrderItemResponse response = orderService.confirmOrderItem(orderItemId, farmerId);
        return ResponseEntity.ok(response);
    }

    /**
     * Farmer or Admin marks an item as dispatched/shipped.
     */
    @PutMapping("/items/{orderItemId}/dispatch")
    public ResponseEntity<FarmerOrderItemResponse> dispatchOrderItem(
            @PathVariable Long orderItemId,
            @RequestParam(required = false, defaultValue = "0") Long farmerId) {
        FarmerOrderItemResponse response = orderService.dispatchOrderItem(orderItemId, farmerId);
        return ResponseEntity.ok(response);
    }
}
