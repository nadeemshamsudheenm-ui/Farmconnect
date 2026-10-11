package com.farmconnect;

import com.farmconnect.dto.FarmerOrderItemResponse;
import com.farmconnect.dto.OrderItemRequest;
import com.farmconnect.dto.OrderPlacementRequest;
import com.farmconnect.dto.OrderSummaryResponse;
import com.farmconnect.entity.*;
import com.farmconnect.exception.InsufficientStockException;
import com.farmconnect.repository.FarmerProfileRepository;
import com.farmconnect.repository.OrderItemRepository;
import com.farmconnect.repository.ProductRepository;
import com.farmconnect.repository.UserRepository;
import com.farmconnect.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProfileRepository farmerProfileRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("ORDER MANAGEMENT: Buyer places order, inventory is decremented atomically")
    void testPlaceOrderDecrementsInventory() {
        User buyer = userRepository.findByEmail("procurement@freshdirect.com").orElseThrow();
        Product product = productRepository.findAll().get(0);
        BigDecimal originalStock = product.getStockQuantity();
        BigDecimal orderQty = new BigDecimal("10.00");

        OrderPlacementRequest request = new OrderPlacementRequest();
        request.setBuyerId(buyer.getId());
        request.setDeliveryAddress("100 Market Boulevard, Warehouse Bay 4");
        request.setDeliveryContactNumber("+91 98000 12345");
        request.setItems(List.of(new OrderItemRequest(product.getId(), orderQty)));

        OrderSummaryResponse response = orderService.placeOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.getOrderTrackingNumber()).startsWith("FC-");
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getQuantity()).isEqualByComparingTo(orderQty);

        // Verify inventory decrement in database
        Product reloadedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(reloadedProduct.getStockQuantity()).isEqualByComparingTo(originalStock.subtract(orderQty));
    }

    @Test
    @DisplayName("ORDER MANAGEMENT: Ordering more than available stock throws InsufficientStockException")
    void testInsufficientStockThrowsException() {
        User buyer = userRepository.findByEmail("procurement@freshdirect.com").orElseThrow();
        Product product = productRepository.findAll().get(0);
        BigDecimal excessiveQty = product.getStockQuantity().add(new BigDecimal("9999.00"));

        OrderPlacementRequest request = new OrderPlacementRequest();
        request.setBuyerId(buyer.getId());
        request.setDeliveryAddress("Test Address");
        request.setDeliveryContactNumber("+91 98000 12345");
        request.setItems(List.of(new OrderItemRequest(product.getId(), excessiveQty)));

        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    @DisplayName("ORDER WORKFLOW: Farmer receives notification and confirms item fulfillment")
    void testFarmerCanConfirmOrderItem() {
        FarmerProfile farmer = farmerProfileRepository.findAll().get(0);
        List<FarmerOrderItemResponse> items = orderService.getFarmerOrderItems(farmer.getId());

        assertThat(items).isNotEmpty();
        FarmerOrderItemResponse firstItem = items.get(0);

        FarmerOrderItemResponse confirmed = orderService.confirmOrderItem(firstItem.getOrderItemId(), farmer.getId());
        assertThat(confirmed.getItemStatus()).isEqualTo(OrderItemStatus.CONFIRMED_BY_FARMER);
    }

    @Test
    @DisplayName("ADMIN CONTROL: Admin can retrieve all orders and confirm/dispatch any farmer item with farmerId=0")
    void testAdminCanControlAllOrdersAndItems() {
        List<OrderSummaryResponse> allOrders = orderService.getAllOrders();
        assertThat(allOrders).isNotEmpty();

        List<FarmerOrderItemResponse> allItems = orderService.getAllOrderItems();
        assertThat(allItems).isNotEmpty();

        FarmerOrderItemResponse item = allItems.get(0);
        // Admin confirms with farmerId = 0
        FarmerOrderItemResponse confirmed = orderService.confirmOrderItem(item.getOrderItemId(), 0L);
        assertThat(confirmed.getItemStatus()).isEqualTo(OrderItemStatus.CONFIRMED_BY_FARMER);

        // Admin dispatches with farmerId = 0
        FarmerOrderItemResponse dispatched = orderService.dispatchOrderItem(item.getOrderItemId(), 0L);
        assertThat(dispatched.getItemStatus()).isEqualTo(OrderItemStatus.DISPATCHED);
    }
}
