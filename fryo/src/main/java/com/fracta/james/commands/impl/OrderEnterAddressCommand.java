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

public class OrderEnterAddressCommand implements Command<Long> {
	
	private static final OrderEnterAddressCommand INSTANCE = new OrderEnterAddressCommand();
	
	private final OrderStepService orderStepService = new OrderStepServiceDefault();
	private final CustomerService customerService = new CustomerServiceDefault();
	private final TelegramService telegramService = new TelegramServiceDefault();
	
	private static final Pattern ADDRESS_PATTERN = Pattern.compile("[a-zA-Z]");
	
	public OrderEnterAddressCommand() {
	}

	public static OrderEnterAddressCommand getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void execute(Long chatId) {
		customerService.setActionForChatId(chatId, "order=enter-customer-address");
		telegramService.sendMessage(new MessageSend(chatId, "Enter your address: ", createKeyboard(false)));
		sendCurrentAddress(chatId);
	}

	private void sendCurrentAddress(long chatId) {
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null)
			throw new InvalidOrderStepException(String.format("Check the customer with %d chatId.", chatId));
		
		var currentAddress = order.getCustomer().getAddress();
		if (currentAddress.trim().length() > 0) {
			telegramService.sendMessage(new MessageSend(chatId, String.format("Current address: %s", currentAddress),
					createKeyboard(true)));
		}
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
	
	public void enterAddress(long chatId, String address) {
		var matcher = ADDRESS_PATTERN.matcher(address);
		if (!(matcher.find())) {
			telegramService.sendMessage(new MessageSend(chatId, "Incorrect address, try again."));
			return;
		}
		
		var order = orderStepService.findCachedOrderByChatId(chatId);
		if (order == null || order.getCustomer() == null)
			throw new InvalidOrderStepException(String.format("Check the customer with %d chatId", chatId));
		
		order.getCustomer().setAddress(address);
		orderStepService.updateCachedOrder(chatId, order);
		orderStepService.nextStepOrder(chatId);
	}
}
