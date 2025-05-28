package com.example.websitebantuonggolumiwood.admin.repository;

import com.example.websitebantuonggolumiwood.admin.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Integer> {
}
