package com.example.Statistic.Repositories;

import com.example.Statistic.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Integer> {

    @Query("SELECT SUM(o.totalAmount) FROM Orders o WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year")
    Float getRevenueByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(o) FROM Orders o WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year")
    Long getOrderCountByMonth(@Param("month") int month, @Param("year") int year);

    @Query(value = "SELECT CAST(o.order_date AS date) AS date, SUM(o.total_amount) AS revenue " +
            "FROM orders o " +
            "WHERE o.order_date >= :startDate AND o.order_date < DATEADD(day, 1, :endDate) " +
            "GROUP BY CAST(o.order_date AS date) " +
            "ORDER BY CAST(o.order_date AS date)", nativeQuery = true)
    List<Object[]> getDailyRevenueNative(@Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE order_date >= :startDate AND order_date < DATEADD(day, 1, :endDate)", nativeQuery = true)
    Long getOrderCountBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT SUM(total_amount) FROM orders WHERE order_date >= :startDate AND order_date < DATEADD(day, 1, :endDate)", nativeQuery = true)
    Double getRevenueBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Thêm hàm lấy tổng số đơn hàng (không theo điều kiện thời gian)
    @Query("SELECT COUNT(o) FROM Orders o")
    Long getTotalOrderCount();

    // Thêm hàm lấy tổng doanh thu (không theo điều kiện thời gian)
    @Query("SELECT SUM(o.totalAmount) FROM Orders o")
    Double getTotalRevenue();
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.orderDate BETWEEN :start AND :end AND o.status = 'COMPLETED'")
    Long countOrdersBetween(@Param("start") Date start, @Param("end") Date end);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Orders o WHERE o.orderDate BETWEEN :start AND :end AND o.status = 'COMPLETED'")
    Double sumRevenueBetween(@Param("start") Date start, @Param("end") Date end);
}
