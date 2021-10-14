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

public class OrderEnterNameCommand implements Command<Long> {
	
	private static final OrderEnterNameCommand INSTANCE = new OrderEnterNameCommand();
	
	private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
	private final CustomerService customerService = CustomerServiceDefault.getInstance();
	private final TelegramService telegramService = TelegramServiceDefault.getInstance();
	
	private static final Pattern NAME_PATTERN = Pattern.compile("a-zA-Z");
	
	public OrderEnterNameCommand() {
	}
	
	public static OrderEnterNameCommand getInstance() {
		return INSTANCE;
	}
	
	private void sendCurrentName(long chatId) {
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null)
			throw new InvalidOrderStepException(String.format("Check the customer with %d chatId.", chatId));
		
		var currentName = order.getCustomer().getName();
		if (currentName.trim().length() > 0) {
			telegramService.sendMessage(new MessageSend(chatId, String.format("Current name: %s", currentName),
					createKeyboard(true)));
		}
		
	}
	
	private ReplyKeyboardMarkup createKeyboard(boolean skipstep) {
		var rows = new ArrayList<KeyboardRow>();
	
		if (skipstep) {
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
	
	public void enterName(long chatId, String name) {
		var matcher = NAME_PATTERN.matcher(name);
		if (!matcher.find()) {
			telegramService.sendMessage(new MessageSend(chatId, "Incorrect name, try again."));
			return;
		}
		
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null)
			throw new InvalidOrderStepException(String.format("Check the customer with %d chatId.", chatId));
	
		order.getCustomer().setName(name);
		orderStepService.updateCachedOrder(chatId, order);
		orderStepService.nextStepOrder(chatId);
	}

	@Override
	public void execute(Long chatId) {
		customerService.setActionForChatId(chatId, "order=enter-customer-name");
		telegramService.sendMessage(new MessageSend(chatId, "Enter your name: ", createKeyboard(false)));
		sendCurrentName(chatId);
	}
}