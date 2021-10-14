package com.fracta.james.commands.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.OrderColumn;

import com.fracta.james.commands.Command;
import com.fracta.james.domain.entities.Customer;
import com.fracta.james.domain.entities.Order;
import com.fracta.james.domain.entities.OrderStatus;
import com.fracta.james.domain.models.CartItem;
import com.fracta.james.exceptions.EntityNotFoundException;
import com.fracta.james.services.CartService;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.OrderService;
import com.fracta.james.services.OrderStepService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CartServiceDefault;
import com.fracta.james.services.impl.CustomerServiceDefault;
import com.fracta.james.services.impl.OrderServiceDefault;
import com.fracta.james.services.impl.OrderStepServiceDefault;
import com.fracta.james.services.impl.TelegramServiceDefault;

public class OrderProcessCommand implements Command<Long> {

	private static final OrderProcessCommand INSTANCE = new OrderProcessCommand();
	
	private final OrderService orderService = OrderServiceDefault.getInstance();
	private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	private final CartService cartService = CartServiceDefault.getInstance();
	private final TelegramService telegramService = TelegramServiceDefault.getInstance();
	
	
	public OrderProcessCommand() {
	}

	public static OrderProcessCommand getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void execute(Long chatId) {
		var customer = customerService.findByChatId(chatId);
		if (customer == null)
			throw new EntityNotFoundException(String.format("Customer with %d chatId not found.", chatId));
		
		orderStepService.saveCachedOrder(chatId, 
				buildOrder(customer, cartService.findAllCartItemsByChatId(chatId)));
		orderStepService.nextStepOrder(chatId);
	}

	private Order buildOrder(Customer customer, List<CartItem> cartItems) {
		var order = new Order();
		order.setCustomer(customer);
		order.setAmount(cartService.calculateTotalPrice(cartItems));
		order.setDate(LocalDateTime.now());
		order.setItems(orderService.fromCartItems(cartItems));
		order.setStatus(OrderStatus.WAITING);
		return order;
	}
}