package com.fracta.james.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import com.fracta.james.commands.impl.CartCommand;
import com.fracta.james.commands.impl.CatalogCommand;
import com.fracta.james.commands.impl.ShowProductsCommand;
import com.fracta.james.handlers.Handler;
import com.fracta.james.services.CartService;

public class CallbackHandler implements Handler<CallbackQuery> {

	private final CartCommand cartCommand = CartCommand.getInstance();
	private final CatalogCommand catalogCommand = CatalogCommand.getInstance();
	private final ShowProductsCommand showProductsCommand = ShowProductsCommand.getInstance();
	
	@Override
	public void handle(CallbackQuery callbackQuery) {
		if (callbackQuery.getMessage() != null)
			handleMessage(callbackQuery);
		else
			handleInline(callbackQuery);
	}

	private void handleInline(CallbackQuery callbackQuery) {
		var chatId = callbackQuery.getFrom().getId().longValue();
		var data = callbackQuery.getData();
		var inlineMessageId = callbackQuery.getInlineMessageId();
		
		if (data.startsWith("show-products=plus-product"))
			showProductsCommand.plusProduct(chatId, inlineMessageId, data);
		else if (data.startsWith("show-products=minus-product"))
			showProductsCommand.minusProduct(chatId, inlineMessageId, data);
		else if (data.equals("show-products=open-cart"))
			cartCommand.execute(chatId);
		else if (data.equals("show-products=open-catalog"))
			catalogCommand.execute(chatId);
	}

	private void handleMessage(CallbackQuery callbackQuery) {
		var chatId = callbackQuery.getFrom().getId().longValue();
		var messageId = callbackQuery.getMessage().getMessageId();
		var data = callbackQuery.getData();
		
		if (data.equals("cart=delete-product"))
			cartCommand.deleteProduct(chatId, messageId);
		else if (data.equals("cart=plus-product"))
			cartCommand.plusProduct(chatId, messageId);
		else if (data.equals("cart=minus-product"))
			cartCommand.minusProduct(chatId, messageId);
		else if (data.equals("cart=previous-product"))
			cartCommand.previousProduct(chatId, messageId);
		else if (data.equals("cart=next-product"))
			cartCommand.nextProduct(chatId, messageId);
		else if (data.equals("cart=process-order"))
			cartCommand.processOrder(chatId, messageId);
	}
}