package com.fracta.james.services.impl;

import java.util.List;

import com.fracta.james.core.ConfigReader;
import com.fracta.james.domain.entities.Customer;
import com.fracta.james.domain.entities.Order;
import com.fracta.james.domain.entities.OrderItem;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.services.NotificationService;
import com.fracta.james.services.TelegramService;

public class NotificationServiceDefault implements NotificationService {

	private static final NotificationService INSTANCE = new NotificationServiceDefault();
	private final TelegramService telegramService = new TelegramServiceDefault();
	
	private static final ConfigReader CONFIG = ConfigReader.getInstance();
	private static final String ADMIN_PANEL_BASE_URL = CONFIG.getProperty("admin-panel.base-url");
	private static final Long TELEGRAM_ADMIN_CHAT_ID = Long.parseLong(CONFIG.getProperty("telegram.admin.chat-id"));
	
	public NotificationServiceDefault() {
	}
	
	public static NotificationService getInstance() {
		return INSTANCE;
	}


	@Override
	public void notifyAdminChatAboutNewOrder(Order order) {
		telegramService.sendMessage(new MessageSend(TELEGRAM_ADMIN_CHAT_ID, createOrderAndCustomerInfo(order)));
		telegramService.sendMessage(new MessageSend(TELEGRAM_ADMIN_CHAT_ID, createOrderItemsInfo(order)));
	}
	
	private String createOrderAndCustomerInfo(Order order) {
		return "#order_" + order.getId() + "\n"
				+ "<b>Order URL</b>\n" + buildOrderUrl(order.getId()) + "\n\n"
				+ "<b>Order Info</b>\n" + buildOrderInfo(order) + "\n\n"
				+ "<b>Customer Info</b>\n" + buildCustomerInfo(order.getCustomer());
	}
	
	private String buildOrderUrl(long orderId) {
		return ADMIN_PANEL_BASE_URL + "/orders/edit/" + orderId;
	}
	
	private String buildOrderInfo(Order order) {
		return "$" + order.getAmount();
	}
	
	private String buildCustomerInfo(Customer customer) {
		return customer.getName() + "\n"
				+ customer.getPhone() + "\n"
				+ customer.getCity() + "\n"
				+ customer.getAddress() + "\n"
				+ "<a href=\tg://user?id=" + customer.getChatId() + "\">Open profile</a>";
	}
	
	private String createOrderItemsInfo(Order order) {
		return "#order_" + order.getId() + "\n"
				+ "<b>Order Items</b>\n" + buildOrderItemsInfo(order.getItems());
	}
	
	private String buildOrderItemsInfo(List<OrderItem> orderItems) {
		var result = new StringBuilder();
		for (int i = 0; i < orderItems.size(); i++) {
			var item = orderItems.get(i);
			result.append(i + 1).append(". ").append(orderItems.get(i).getProductName())
			.append(" | ").append(item.getQuantity()).append(" pcs")
			.append(" | $").append(item.getProductPrice() * item.getQuantity());
		}
		
		return result.toString();
	}
}
