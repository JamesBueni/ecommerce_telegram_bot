package com.fracta.james.commands.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.fracta.james.commands.Command;
import com.fracta.james.commands.Commands;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.exceptions.InvalidOrderStepException;
import com.fracta.james.services.CustomerService;
import com.fracta.james.services.OrderStepService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CustomerServiceDefault;
import com.fracta.james.services.impl.OrderStepServiceDefault;
import com.fracta.james.services.impl.TelegramServiceDefault;
import com.fracta.james.utils.KeyboardUtils;

public class OrderEnterPhoneCommand implements Command<Long>{

	private static final OrderEnterPhoneCommand INSTANCE = new OrderEnterPhoneCommand();
	
	private final OrderStepService orderStepService = new OrderStepServiceDefault();
	private final CustomerService customerService = new CustomerServiceDefault();
	private final TelegramService telegramService = new TelegramServiceDefault();
	
	private static final Pattern PHONE_PATTERN = Pattern.compile("[0-9]");

	public OrderEnterPhoneCommand() {
	}
	
	public static OrderEnterPhoneCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(Long chatId) {
		customerService.setActionForChatId(chatId, "order=enter-customer-phone");
		telegramService.sendMessage(new MessageSend(chatId, "Enter your phone: ", createKeyboard(false)));
		sendCurrentPhone(chatId);
	}

	private ReplyKeyboardMarkup createKeyboard(boolean skipStep) {
		var rows = new ArrayList<KeyboardRow>();
		
		if (skipStep) {
			var firstRow = new KeyboardRow();
			firstRow.add(new KeyboardButton(Commands.ORDER_NEXT_STEP_COMMAND));
			rows.add(firstRow);
		}
		var secondRow = new KeyboardRow();
		secondRow.addAll(List.of(
				new KeyboardButton(Commands.ORDER_CANCEL_COMMAND),
				new KeyboardButton(Commands.ORDER_BACK_STEP_COMMAND)));
		rows.add(secondRow);
		
		return KeyboardUtils.create(rows);
	}

	private void sendCurrentPhone(long chatId) {
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null)
			throw new InvalidOrderStepException(String.format("Check the customer with %d chatId.", chatId));
		
		var currentPhone = order.getCustomer().getPhone();
		if (currentPhone.trim().length() > 0) {
			telegramService.sendMessage(new MessageSend(chatId, String.format("Current phone: %s", currentPhone),
					createKeyboard(true)));
		}
	}
	
	public void enterPhone(long chatId, String phone) {
		var matcher = PHONE_PATTERN.matcher(phone);
		if (!(matcher.find())) {
			telegramService.sendMessage(new MessageSend(chatId, "Incorrect phone, try again."));
			return;
		}
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null)
			throw new InvalidOrderStepException(String.format("Check the customer with %d chatId.", chatId));
	
		order.getCustomer().setPhone(phone);
		orderStepService.updateCachedOrder(chatId, order);
		orderStepService.nextStepOrder(chatId);
	}
}