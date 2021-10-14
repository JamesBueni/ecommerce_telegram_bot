package com.fracta.james.commands.impl;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.fracta.james.commands.Command;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.services.CategoryService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CategoryServiceDefault;
import com.fracta.james.services.impl.TelegramServiceDefault;

public class CatalogCommand implements Command<Long> {
	
	private static final CatalogCommand INSTANCE = new CatalogCommand();

	private final CategoryService categoryService = CategoryServiceDefault.getInstance();
	private final TelegramService telegramService = TelegramServiceDefault.getInstance();
	
	public CatalogCommand() {
	}

	public static CatalogCommand getInstance() {
		return INSTANCE;
	}
	
	private InlineKeyboardMarkup createCategoryKeyboard() {
		// TODO
//		var result = new InlineKeyboardMarkup().setKeyboard(new ArrayList<List<InlineKeyboardButton>>() {{
//			for (var category : categoryService.findAll()) {
//				add(new ArrayList<InlineKeyboardButton>() {{
//					var categoryName = category.getName();
//					add(new InlineKeyboardButton(categoryName).setSwitchInlineQueryCurrentChat(categoryName));
//				}});
//			}
//		}});
		return null;
	}
	
	@Override
	public void execute(Long chatId) {
		telegramService.sendMessage(new MessageSend(chatId, "Choose a category: ", createCategoryKeyboard()));
	}
}