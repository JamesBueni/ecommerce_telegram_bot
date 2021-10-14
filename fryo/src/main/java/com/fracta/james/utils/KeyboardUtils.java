package com.fracta.james.utils;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public final class KeyboardUtils {

	public KeyboardUtils() {
	}
	
	public static ReplyKeyboardMarkup create(List<KeyboardRow> rows) {
		var keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setKeyboard(rows);
		keyboardMarkup.setOneTimeKeyboard(false);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setSelective(true);
		return keyboardMarkup;
	}
}
