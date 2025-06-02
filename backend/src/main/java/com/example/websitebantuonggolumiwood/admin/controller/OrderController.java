package com.example.websitebantuonggolumiwood.admin.controller;

import com.example.websitebantuonggolumiwood.admin.entity.Order;
import com.example.websitebantuonggolumiwood.admin.entity.OrderItem;
import com.example.websitebantuonggolumiwood.admin.entity.ShippingMethod;
import com.example.websitebantuonggolumiwood.admin.repository.OrderRepository;
import com.example.websitebantuonggolumiwood.admin.repository.ShippingMethodRepository;
import com.example.websitebantuonggolumiwood.admin.response.OrderDetailResponse;
import com.example.websitebantuonggolumiwood.admin.response.OrderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "http://localhost:5174")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ShippingMethodRepository shippingMethodRepository;

    public OrderController(OrderRepository orderRepository, ShippingMethodRepository shippingMethodRepository) {
        this.orderRepository = orderRepository;
        this.shippingMethodRepository = shippingMethodRepository;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDetailResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate,desc") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = sortParams[0];

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        if (keyword != null && !keyword.isBlank()) {
            try {
                Integer idKeyword = Integer.parseInt(keyword.trim());
                Optional<Order> optOrder = orderRepository.findById(idKeyword);
                if (optOrder.isPresent()) {
                    List<Order> singleOrderList = List.of(optOrder.get());
                    Page<Order> pageResult = new org.springframework.data.domain.PageImpl<>(singleOrderList, pageable, 1);
                    Page<OrderDetailResponse> responsePage = pageResult.map(this::mapToOrderDetailResponse);
                    return ResponseEntity.ok(responsePage);
                }
            } catch (NumberFormatException ignored) {
                // Không phải số, bỏ qua tìm ID
            }
        }

        // Nếu không tìm theo ID, hoặc keyword null/empty thì tìm theo keyword tên, sdt, email
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim().toLowerCase() : null;

        Page<Order> orderPage = orderRepository.findAllWithFilters(
                cleanKeyword,
                (status != null && !status.isBlank()) ? status.trim() : null,
                startDateTime,
                endDateTime,
                pageable
        );

        Page<OrderDetailResponse> responsePage = orderPage.map(this::mapToOrderDetailResponse);
        return ResponseEntity.ok(responsePage);
    }





    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng không tồn tại");
        }
        OrderDetailResponse response = mapToOrderDetailResponse(optOrder.get());
        return ResponseEntity.ok(response);
    }

    // DTO cho cập nhật trạng thái
    public static class UpdateStatusRequest {
        private String newStatus;
        private String cancelReason;

        public String getNewStatus() {
            return newStatus;
        }

        public void setNewStatus(String newStatus) {
            this.newStatus = newStatus;
        }

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer id,
            @RequestBody UpdateStatusRequest request
    ) {
        String newStatus = request.getNewStatus();
        String cancelReason = request.getCancelReason();

        if (newStatus == null || newStatus.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Trạng thái mới không được để trống");
        }

        if ("CANCELLED".equalsIgnoreCase(newStatus) && (cancelReason == null || cancelReason.trim().isEmpty())) {
            return ResponseEntity.badRequest().body("Cần cung cấp lý do hủy đơn hàng");
        }

        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng không tồn tại");
        }

        Order order = optOrder.get();
        order.setStatus(newStatus);

        if ("CANCELLED".equalsIgnoreCase(newStatus)) {
            order.setCancelReason(cancelReason);
        } else {
            order.setCancelReason(null);
        }

        orderRepository.save(order);

        OrderDetailResponse response = mapToOrderDetailResponse(order);
        return ResponseEntity.ok(response);
    }

    private OrderDetailResponse mapToOrderDetailResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());

        OrderDetailResponse response = new OrderDetailResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setCustomerAddress(order.getCustomerAddress());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());

        response.setDiscountAmount(order.getDiscountAmount());
        response.setUserId(order.getUserId());
        response.setGuestEmail(order.getGuestEmail());

        response.setShippingRecipientName(order.getShippingRecipientName());
        response.setShippingRecipientPhone(order.getShippingRecipientPhone());
        response.setShippingStreetAddress(order.getShippingStreetAddress());
        response.setShippingWard(order.getShippingWard());
        response.setShippingDistrict(order.getShippingDistrict());
        response.setShippingCity(order.getShippingCity());

        response.setShippingMethodId(order.getShippingMethodId());
        response.setShippingCost(order.getShippingCost());

        if (order.getShippingMethodId() != null) {
            Optional<ShippingMethod> shippingMethodOpt = shippingMethodRepository.findById(order.getShippingMethodId());
            response.setShippingMethodName(shippingMethodOpt.map(ShippingMethod::getName).orElse("N/A"));
        } else {
            response.setShippingMethodName("N/A");
        }

        response.setPaymentMethod(order.getPaymentMethod());
        response.setOrderNote(order.getOrderNote());
        response.setCancelReason(order.getCancelReason());

        response.setDepositAmount(order.getDepositAmount());
        response.setDepositStatus(order.getDepositStatus());

        response.setItems(itemResponses);

        return response;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse itemResponse = new OrderItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setProductId(item.getProductId());
        itemResponse.setQuantity(item.getQuantity());
        itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());
        return itemResponse;
    }
}
