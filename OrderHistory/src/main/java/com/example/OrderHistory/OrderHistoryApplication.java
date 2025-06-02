package com.example.OrderHistory;

import com.example.OrderHistory.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderHistoryApplication {

	public static void main(String[] args) {
		Order order = new Order();
		SpringApplication.run(OrderHistoryApplication.class, args);
	}

}
