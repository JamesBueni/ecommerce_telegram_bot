package com.fracta.james.commands.impl;

import com.fracta.james.commands.Command;
import com.fracta.james.commands.Commands;
import com.fracta.james.domain.entities.Customer;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.MessageService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CustomerServiceDefault;
import com.fracta.james.services.impl.MessageServiceCached;
import com.fracta.james.services.impl.TelegramServiceDefault;

public class StartCommand implements Command<Long> {

	private static final StartCommand INSTANCE = new StartCommand();

	private final MessageService messageService = MessageServiceCached.getInstance();
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	private final TelegramService telegramService = TelegramServiceDefault.getInstance();
	
	public StartCommand() {
	}
	
	public static StartCommand getInstance() {
		return INSTANCE;
	}
	
	private void saveCustomer(long chatId) {
		var customer = new Customer();
		customer.setChatId(chatId);
		customer.setActive(true);
		customerService.save(customer);
	}
	
	private void activateCustomer(Customer customer) {
		customer.setActive(true);
		customerService.update(customer);
	}
	
	private void sendStartMessage(long chatId) {
		var messageText = messageService.findByName("START_MESSAGE").buildText();
		telegramService.sendMessage(new MessageSend(chatId, messageText, Commands.createMainMenuKeyboard()));
	}
	
	@Override
	public void execute(Long chatId) {
		var customer = customerService.findByChatId(chatId);
		if (customer == null)
			saveCustomer(chatId);
		else if (!customer.isActive())
			activateCustomer(customer);
		
		sendStartMessage(chatId);
	}	
}