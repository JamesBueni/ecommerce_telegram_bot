package com.fracta.james.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public final class Commands {
	
	public static final String START_COMMAND = "/start";
	
	public static final String CATALOG_COMMAND = "\uD83D\uDCE6 Catalog";
	public static final String CART_COMMAND = "\uD83D\uDECD Cart";
	
	public static final String ORDER_NEXT_STEP_COMMAND = "\u2705 Correct";
	public static final String ORDER_BACK_STEP_COMMAND = "\uD83E\uDCA8 Back";
	public static final String ORDER_CANCEL_COMMAND = "\uD83D\uDDD9 Cancel";
	
	public Commands() {
	}
	
	public static ReplyKeyboardMarkup createMainMenuKeyboard() {
		// To be implemented
		return null;
	}
	
}
