package com.example.OrderHistory.repo;

import com.example.OrderHistory.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
