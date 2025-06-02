package com.example.websitebantuonggolumiwood.admin.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderResponse {

    private Integer id;
    private String customerName;
    private String customerPhone;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;

}
