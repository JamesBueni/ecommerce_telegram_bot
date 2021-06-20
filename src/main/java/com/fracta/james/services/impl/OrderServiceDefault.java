package com.fracta.james.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.fracta.james.domain.entities.Order;
import com.fracta.james.domain.entities.OrderItem;
import com.fracta.james.domain.models.CartItem;
import com.fracta.james.repositories.OrderRepo;
import com.fracta.james.repositories.impl.OrderRepoDefault;
import com.fracta.james.services.OrderService;

public class OrderServiceDefault implements OrderService {

	private static final OrderService INSTANCE = new OrderServiceDefault();
	private final OrderRepo repo = new OrderRepoDefault();
	
	public OrderServiceDefault() {
	}
	
	public static OrderService getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void save(Order order) {
		if (order == null)
			throw new IllegalArgumentException("order shouldn't be null");
		repo.save(order);
	}

	@Override
	public List<OrderItem> fromCartItems(List<CartItem> cartItems) {
		var orderItems = new ArrayList<OrderItem>();
		for (var cartItem : cartItems) {
			var orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setProductName(cartItem.getProduct().getName());
			orderItem.setProductPrice(cartItem.getProduct().getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItems.add(orderItem);
		}
		
		return orderItems;
	}

}
