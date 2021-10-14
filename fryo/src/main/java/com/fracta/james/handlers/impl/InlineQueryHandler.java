package com.fracta.james.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import com.fracta.james.commands.impl.ShowProductsCommand;
import com.fracta.james.handlers.Handler;

public class InlineQueryHandler implements Handler<InlineQuery> {

	private final ShowProductsCommand showProductsCommand = ShowProductsCommand.getInstance();
	
	@Override
	public void handle(InlineQuery inlineQuery) {
		if (inlineQuery.getQuery() != null)
			showProductsCommand.execute(inlineQuery);
	}
}