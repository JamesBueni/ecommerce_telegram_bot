package com.fracta.james.handlers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import com.fracta.james.commands.impl.OrderEnterAddressCommand;
import com.fracta.james.commands.impl.OrderEnterCityCommand;
import com.fracta.james.commands.impl.OrderEnterNameCommand;
import com.fracta.james.commands.impl.OrderEnterPhoneCommand;
import com.fracta.james.handlers.Handler;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.impl.CustomerServiceDefault;

public class UpdateHandler implements Handler<Update> {

	private static final Logger LOG = LogManager.getLogger(UpdateHandler.class);
	
	private final Handler<CallbackQuery> callbackQueryHandler = new CallbackHandler();
	private final Handler<InlineQuery> inlineQueryHandler = new InlineQueryHandler();
	private final Handler<Message> messageHandler = new MessageHandler();
	private final Handler<Message> contactHandler = new ContactHandler();
	
	@Override
	public void handle(Update update) {
		try {
			handleUpdate(update);
		} catch (Exception e) {
			LOG.error("Failed to handle the update.", e);
		}
	}
	
	private void handleUpdate(Update update) {
		if (update.hasMessage()) {
			var msg = update.getMessage();
			
			if (msg.hasText() && msg.getReplyMarkup() == null)
				messageHandler.handle(msg);
			else if (msg.hasContact())
				contactHandler.handle(msg);
		}
		else if (update.hasInlineQuery())
			inlineQueryHandler.handle(update.getInlineQuery());
		else if (update.hasCallbackQuery())
			callbackQueryHandler.handle(update.getCallbackQuery());
	}
}