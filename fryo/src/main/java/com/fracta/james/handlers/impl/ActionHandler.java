package com.fracta.james.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.fracta.james.commands.impl.OrderEnterAddressCommand;
import com.fracta.james.commands.impl.OrderEnterCityCommand;
import com.fracta.james.commands.impl.OrderEnterNameCommand;
import com.fracta.james.commands.impl.OrderEnterPhoneCommand;
import com.fracta.james.handlers.Handler;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.impl.CustomerServiceDefault;

public class ActionHandler implements Handler<Message>{
	
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	
	private final OrderEnterNameCommand orderEnterNameCommand = OrderEnterNameCommand.getInstance();
	private final OrderEnterPhoneCommand orderEnterPhoneCommand = OrderEnterPhoneCommand.getInstance();
	private final OrderEnterCityCommand orderEnterCityCommand = OrderEnterCityCommand.getInstance();
	private final OrderEnterAddressCommand orderEnterAddressCommand = OrderEnterAddressCommand.getInstance();
	
	@Override
	public void handle(Message message) {
		var chatId = message.getChatId();
		var text = message.getText();
		var action = customerService.findActionByChatId(chatId);
		
		if ("order=enter-customer-name".equals(action))
			orderEnterNameCommand.enterName(chatId, text);
		else if ("order=enter-customer-phone".equals(action))
			orderEnterPhoneCommand.enterPhone(chatId, text);
		else if ("order=enter-customer-city".equals(action))
			orderEnterCityCommand.enterCity(chatId, text);
		else if ("order=enter-customer-address".equals(action))
			orderEnterAddressCommand.enterAddress(chatId, text);		
	}
}