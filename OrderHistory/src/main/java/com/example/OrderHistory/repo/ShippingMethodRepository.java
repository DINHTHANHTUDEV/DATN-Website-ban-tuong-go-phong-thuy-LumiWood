package com.example.OrderHistory.repo;

import com.example.OrderHistory.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod,Integer> {
}
