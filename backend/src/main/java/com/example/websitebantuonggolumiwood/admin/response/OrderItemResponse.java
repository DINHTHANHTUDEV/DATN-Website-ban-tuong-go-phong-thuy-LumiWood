package com.example.websitebantuonggolumiwood.admin.response;

import com.example.websitebantuonggolumiwood.admin.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderItemResponse {

    private Integer id;
    private Integer productId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
