package com.fracta.james.commands.impl;

import org.telegram.telegrambots.meta.api.objects.Chat;

import com.fracta.james.commands.Command;
import com.fracta.james.commands.Commands;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.exceptions.InvalidOrderStepException;
import com.fracta.james.services.CartService;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.MessageService;
import com.fracta.james.services.NotificationService;
import com.fracta.james.services.OrderService;
import com.fracta.james.services.OrderStepService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CartServiceDefault;
import com.fracta.james.services.impl.CustomerServiceDefault;
import com.fracta.james.services.impl.MessageServiceCached;
import com.fracta.james.services.impl.NotificationServiceDefault;
import com.fracta.james.services.impl.OrderServiceDefault;
import com.fracta.james.services.impl.OrderStepServiceDefault;
import com.fracta.james.services.impl.TelegramServiceDefault;
import com.sun.nio.sctp.Notification;

public class OrderCreateCommand implements Command<Long>{

	private static final OrderCreateCommand INSTANCE = new OrderCreateCommand();
		
	private final MessageService messageService = MessageServiceCached.getInstance();
	private final NotificationService notificationService = NotificationServiceDefault.getInstance();
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	private final CartService cartService = CartServiceDefault.getInstance();
	private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
	private final OrderService orderService = OrderServiceDefault.getInstance();
	private final TelegramService telegramService = TelegramServiceDefault.getInstance();
	
	public OrderCreateCommand() {
	}

	public static OrderCreateCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(Long chatId) {
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null) {
			throw new InvalidOrderStepException(
					String.format("Invalid order step for the customer with %d chatId.", chatId));
		}
		orderService.save(order);
		customerService.update(order.getCustomer());
		this.sendOrderMessageToCustomer(chatId);
		this.clearCustomerCache(chatId);
		notificationService.notifyAdminChatAboutNewOrder(order);
	}

	private void sendOrderMessageToCustomer(Long chatId) {
		var message = messageService.findByName("ORDER_CREATED_MESSAGE").buildText();
		telegramService.sendMessage(new MessageSend(chatId, message, Commands.createMainMenuKeyboard()));
	}
	
	private void clearCustomerCache(Long chatId) {
		customerService.setActionForChatId(chatId, null);
		cartService.deleteAllCartItemsByChatId(chatId);
		orderStepService.deleteCachedOrderByChatId(chatId);
	}
}