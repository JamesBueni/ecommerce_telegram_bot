package com.fracta.james.services;

import java.util.List;

import com.fracta.james.domain.entities.Order;
import com.fracta.james.domain.entities.OrderItem;
import com.fracta.james.domain.models.CartItem;

public interface OrderService {
	
	void save(Order order);
	List<OrderItem> fromCartItems(List<CartItem> cartItems);
}
