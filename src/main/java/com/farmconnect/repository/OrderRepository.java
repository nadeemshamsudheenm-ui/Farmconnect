package com.farmconnect.repository;

import com.farmconnect.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerIdOrderByOrderDateDesc(Long buyerId);

    Optional<Order> findByOrderTrackingNumber(String orderTrackingNumber);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items item WHERE item.farmer.id = :farmerId ORDER BY o.orderDate DESC")
    List<Order> findOrdersForFarmer(@Param("farmerId") Long farmerId);

    List<Order> findAllByOrderByOrderDateDesc();
}
