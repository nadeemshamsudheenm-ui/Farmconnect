package com.farmconnect.service;

import com.farmconnect.dto.*;
import com.farmconnect.entity.*;
import com.farmconnect.exception.InsufficientStockException;
import com.farmconnect.exception.InvalidOperationException;
import com.farmconnect.exception.ResourceNotFoundException;
import com.farmconnect.repository.OrderItemRepository;
import com.farmconnect.repository.OrderRepository;
import com.farmconnect.repository.ProductRepository;
import com.farmconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Places a new purchase order with inventory verification and atomic stock deduction.
     */
    @Transactional
    public OrderSummaryResponse placeOrder(OrderPlacementRequest req) {
        User buyer = userRepository.findById(req.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer user not found with ID: " + req.getBuyerId()));

        String trackingNumber = "FC-" + (System.currentTimeMillis() % 1000000) + "-" +
                UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Order order = new Order(
                trackingNumber,
                buyer,
                BigDecimal.ZERO,
                req.getDeliveryAddress(),
                req.getDeliveryContactNumber(),
                req.getNotes()
        );

        BigDecimal grandTotal = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest itemReq : req.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.getProductId()));

            if (product.getStatus() == ProductStatus.DELISTED) {
                throw new InvalidOperationException("Product '" + product.getName() + "' is currently delisted.");
            }

            if (product.getStockQuantity().compareTo(itemReq.getQuantity()) < 0) {
                throw new InsufficientStockException("Insufficient stock for product '" + product.getName() +
                        "'. Requested: " + itemReq.getQuantity() + " " + product.getUnit() +
                        ", Available: " + product.getStockQuantity() + " " + product.getUnit());
            }

            // Deduct stock
            BigDecimal remainingStock = product.getStockQuantity().subtract(itemReq.getQuantity());
            product.setStockQuantity(remainingStock);
            if (remainingStock.compareTo(BigDecimal.ZERO) == 0) {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }
            productRepository.save(product);

            BigDecimal lineSubtotal = product.getPricePerUnit().multiply(itemReq.getQuantity());
            grandTotal = grandTotal.add(lineSubtotal);

            OrderItem orderItem = new OrderItem(
                    product,
                    product.getFarmer(),
                    itemReq.getQuantity(),
                    product.getPricePerUnit(),
                    lineSubtotal
            );
            order.addItem(orderItem);
            items.add(orderItem);
        }

        order.setTotalAmount(grandTotal);
        Order savedOrder = orderRepository.save(order);

        return mapToOrderSummary(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getBuyerOrders(Long buyerId) {
        return orderRepository.findByBuyerIdOrderByOrderDateDesc(buyerId).stream()
                .map(this::mapToOrderSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc().stream()
                .map(this::mapToOrderSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderSummaryResponse getOrderByTrackingNumber(String trackingNumber) {
        Order order = orderRepository.findByOrderTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with tracking number: " + trackingNumber));
        return mapToOrderSummary(order);
    }

    @Transactional(readOnly = true)
    public List<FarmerOrderItemResponse> getFarmerOrderItems(Long farmerId) {
        if (farmerId == null || farmerId <= 0L) {
            return getAllOrderItems();
        }
        return orderItemRepository.findByFarmerIdOrderByOrderOrderDateDesc(farmerId).stream()
                .map(this::mapToFarmerOrderItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FarmerOrderItemResponse> getAllOrderItems() {
        return orderItemRepository.findAllByOrderByOrderOrderDateDesc().stream()
                .map(this::mapToFarmerOrderItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FarmerOrderItemResponse confirmOrderItem(Long orderItemId, Long farmerId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with ID: " + orderItemId));

        if (farmerId != null && farmerId > 0 && !item.getFarmer().getId().equals(farmerId)) {
            throw new InvalidOperationException("Unauthorized: This order item does not belong to your farm.");
        }

        item.setItemStatus(OrderItemStatus.CONFIRMED_BY_FARMER);
        OrderItem updatedItem = orderItemRepository.save(item);

        // Check overall parent order status
        Order parentOrder = updatedItem.getOrder();
        boolean allConfirmed = parentOrder.getItems().stream()
                .allMatch(i -> i.getItemStatus() == OrderItemStatus.CONFIRMED_BY_FARMER || i.getItemStatus() == OrderItemStatus.DISPATCHED);
        if (allConfirmed && parentOrder.getOrderStatus() == OrderStatus.PENDING) {
            parentOrder.setOrderStatus(OrderStatus.CONFIRMED);
            orderRepository.save(parentOrder);
        }

        return mapToFarmerOrderItemResponse(updatedItem);
    }

    @Transactional
    public FarmerOrderItemResponse dispatchOrderItem(Long orderItemId, Long farmerId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with ID: " + orderItemId));

        if (farmerId != null && farmerId > 0 && !item.getFarmer().getId().equals(farmerId)) {
            throw new InvalidOperationException("Unauthorized: This order item does not belong to your farm.");
        }

        item.setItemStatus(OrderItemStatus.DISPATCHED);
        OrderItem updatedItem = orderItemRepository.save(item);

        Order parentOrder = updatedItem.getOrder();
        boolean allDispatched = parentOrder.getItems().stream()
                .allMatch(i -> i.getItemStatus() == OrderItemStatus.DISPATCHED);
        if (allDispatched) {
            parentOrder.setOrderStatus(OrderStatus.SHIPPED);
            orderRepository.save(parentOrder);
        }

        return mapToFarmerOrderItemResponse(updatedItem);
    }

    private OrderSummaryResponse mapToOrderSummary(Order order) {
        List<OrderItemSummaryResponse> itemDtos = order.getItems().stream()
                .map(i -> new OrderItemSummaryResponse(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getProduct().getUnit(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getSubtotal(),
                        i.getItemStatus()
                ))
                .collect(Collectors.toList());

        return new OrderSummaryResponse(
                order.getId(),
                order.getOrderTrackingNumber(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getDeliveryAddress(),
                order.getDeliveryContactNumber(),
                order.getOrderDate(),
                order.getNotes(),
                itemDtos
        );
    }

    private FarmerOrderItemResponse mapToFarmerOrderItemResponse(OrderItem item) {
        return new FarmerOrderItemResponse(
                item.getId(),
                item.getOrder().getId(),
                item.getOrder().getOrderTrackingNumber(),
                item.getOrder().getOrderDate(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getProduct().getUnit(),
                item.getUnitPrice(),
                item.getSubtotal(),
                item.getItemStatus(),
                item.getOrder().getDeliveryAddress(),
                item.getOrder().getDeliveryContactNumber(),
                item.getOrder().getNotes()
        );
    }
}
