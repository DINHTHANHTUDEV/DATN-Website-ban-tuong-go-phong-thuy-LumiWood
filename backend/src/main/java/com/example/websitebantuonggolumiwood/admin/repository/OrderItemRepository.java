package com.example.websitebantuonggolumiwood.admin.repository;

import com.example.websitebantuonggolumiwood.admin.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Integer orderId);
}
