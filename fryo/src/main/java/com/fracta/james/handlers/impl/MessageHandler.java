package com.fracta.james.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.fracta.james.commands.Commands;
import com.fracta.james.commands.impl.CartCommand;
import com.fracta.james.commands.impl.CatalogCommand;
import com.fracta.james.commands.impl.StartCommand;
import com.fracta.james.handlers.Handler;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.OrderStepService;
import com.fracta.james.services.impl.CustomerServiceDefault;
import com.fracta.james.services.impl.OrderStepServiceDefault;

public class MessageHandler implements Handler<Message> {

	private final Handler<Message> actionHandler = new ActionHandler();
	
	private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	
	private final StartCommand startCommand = StartCommand.getInstance();
	private final CartCommand cartCommand = CartCommand.getInstance();
	private final CatalogCommand catalogCommand = CatalogCommand.getInstance();
	
	@Override
	public void handle(Message message) {
		var chatId = message.getChatId();
		var text = message.getText();
		
		if (text.startsWith(Commands.START_COMMAND) || text.equals(Commands.ORDER_CANCEL_COMMAND))
			startCommand.execute(chatId);
		else if (text.equals(Commands.CART_COMMAND))
			cartCommand.execute(chatId);
		else if (text.equals(Commands.CATALOG_COMMAND))
			catalogCommand.execute(chatId);
		else if (text.equals(Commands.ORDER_BACK_STEP_COMMAND))
			orderStepService.previousStepOrder(chatId);
		else if (text.equals(Commands.ORDER_NEXT_STEP_COMMAND))
			orderStepService.nextStepOrder(chatId);
		else if (customerService.findActionByChatId(chatId) != null)
			actionHandler.handle(message);
	}
}