package com.example.websitebantuonggolumiwood.admin.repository;

import com.example.websitebantuonggolumiwood.admin.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("""
    SELECT o FROM Order o
    WHERE
      (:keyword IS NULL OR
       lower(o.customerName) LIKE lower(CONCAT('%', :keyword, '%')) OR
       lower(o.customerPhone) LIKE lower(CONCAT('%', :keyword, '%')) OR
       lower(o.guestEmail) LIKE lower(CONCAT('%', :keyword, '%'))
      )
      AND (:status IS NULL OR o.status = :status)
      AND (:startDate IS NULL OR o.orderDate >= :startDate)
      AND (:endDate IS NULL OR o.orderDate <= :endDate)
""")
    Page<Order> findAllWithFilters(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );



}
