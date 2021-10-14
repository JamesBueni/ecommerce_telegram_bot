package com.fracta.james.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.fracta.james.commands.impl.OrderEnterPhoneCommand;
import com.fracta.james.handlers.Handler;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.impl.CustomerServiceDefault;

public class ContactHandler implements Handler<Message>{
	
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	
	private final OrderEnterPhoneCommand orderEnterPhoneCommand = OrderEnterPhoneCommand.getInstance();
	
	@Override
	public void handle(Message message) {
		var chatId = message.getChatId();
		var phone = message.getContact().getPhoneNumber();
		var action = customerService.findActionByChatId(chatId);
		
		if ("order=enter-customer-phone".equals(action))
			orderEnterPhoneCommand.enterPhone(chatId, phone);
	}
}