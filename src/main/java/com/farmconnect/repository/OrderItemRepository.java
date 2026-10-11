package com.farmconnect.repository;

import com.farmconnect.entity.OrderItem;
import com.farmconnect.entity.OrderItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByFarmerIdOrderByOrderOrderDateDesc(Long farmerId);

    List<OrderItem> findByFarmerIdAndItemStatus(Long farmerId, OrderItemStatus status);

    List<OrderItem> findAllByOrderByOrderOrderDateDesc();
}
