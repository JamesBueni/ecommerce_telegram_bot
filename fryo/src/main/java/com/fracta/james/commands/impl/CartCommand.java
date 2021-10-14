package com.fracta.james.commands.impl;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.fracta.james.commands.Command;
import com.fracta.james.domain.models.CartItem;
import com.fracta.james.domain.models.MessageEdit;
import com.fracta.james.domain.models.MessagePlaceholder;
import com.fracta.james.domain.models.MessageSend;
import com.fracta.james.services.CartService;
import com.fracta.james.services.MessageService;
import com.fracta.james.services.OrderStepService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CartServiceDefault;
import com.fracta.james.services.impl.MessageServiceCached;
import com.fracta.james.services.impl.OrderStepServiceDefault;
import com.fracta.james.services.impl.TelegramServiceDefault;

public class CartCommand implements Command<Long> {

	private static final CartCommand INSTANCE = new CartCommand();
	
	private final MessageService messageService = MessageServiceCached.getInstance();
	private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
	private final CartService cartService = CartServiceDefault.getInstance();
	private final TelegramService telegramService = TelegramServiceDefault.getInstance();
	
	private static final int MAX_PRODUCT_QUANTITY = 50;
	
	public CartCommand() {
	}
	
	public static CartCommand getInstance() {
		return INSTANCE;
	}
	
	private String createProductText(CartItem cartItem) {
		var message = messageService.findByName("CART_MESSAGE");
		if (cartItem != null) {
			var product = cartItem.getProduct();
			message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_NAME", product.getName()));
			message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_DESC%", product.getDesc()));
			message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_PRICE%", product.getPrice()));
			message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_QUANTITY%", cartItem.getQuantity()));
			message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_TOTAL_AMOUNT%", cartItem.getTotalPrice()));
		}
		return message.buildText();
	}
	
	private InlineKeyboardMarkup createCartKeyboard(List<CartItem> cartItems, int currentCartPage) {
		// TODO
		return null;
	}
	
	public void minusProduct(long chatId, long messageId) {
		var cartItems = cartService.findAllCartItemsByChatId(chatId);
		var currentCartPage = cartService.findPageNumberByChatId(chatId);
		if (cartItems.isEmpty()) {
			telegramService.editMessageText(new MessageEdit(messageId, chatId, "Your cart is empty."));
			return;
		}
		
		var cartItem = cartItems.get((int) currentCartPage);
		if (cartItem != null && cartItem.getQuantity() > 1) {
			cartItem.setQuantity(cartItem.getQuantity() - 1);
			cartService.updateCartItem(chatId, cartItem);
			telegramService.editMessageText(new MessageEdit(messageId, chatId,
					createProductText(cartItem), createCartKeyboard(cartItems, (int)currentCartPage)));
		}
	}
	
	public void plusProduct(long chatId, long messageId) {
		var cartItems = cartService.findAllCartItemsByChatId(chatId);
		var currentCartPage = cartService.findPageNumberByChatId(chatId);
		if (cartItems.isEmpty()) {
			telegramService.editMessageText(new MessageEdit(messageId, chatId, "Your cart is empty."));
			return;
		}
		
		var cartItem = cartItems.get((int) currentCartPage);
		if (cartItem != null && cartItem.getQuantity() < MAX_PRODUCT_QUANTITY) {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartService.updateCartItem(chatId, cartItem);
			telegramService.editMessageText(new MessageEdit(messageId, chatId, createProductText(cartItem),
					createCartKeyboard(cartItems, (int) currentCartPage)));
		}
	}
	
	public void previousProduct(long chatId, long messageId) {
		var cartItems = cartService.findAllCartItemsByChatId(chatId);
		if (cartItems.isEmpty()) {
			telegramService.editMessageText(new MessageEdit(messageId, chatId, "Your cart is empty"));
			return;
		}
		if (cartItems.size() == 1)
			return;
		
		var currentCartPage = cartService.findPageNumberByChatId(chatId);
		if (currentCartPage <= 0)
			currentCartPage = cartItems.size() - 1;
		else
			--currentCartPage;
		
		cartService.setPageNumber(chatId, currentCartPage);
		telegramService.editMessageText(new MessageEdit(messageId, chatId,
				createProductText(cartItems.get((int) currentCartPage)),
				createCartKeyboard(cartItems, (int) currentCartPage)));
	}
	
	public void nextProduct(long chatId, long messageId) {
		var cartItems = cartService.findAllCartItemsByChatId(chatId);
		if (cartItems.isEmpty()) {
			telegramService.editMessageText(new MessageEdit(messageId, chatId, "your cart's empty"));
			return;
		}
		if (cartItems.size() == 1)
			return;
		
		var currentCartPage = cartService.findPageNumberByChatId(chatId);
		if (currentCartPage >= cartItems.size() - 1)
			currentCartPage = 0;
		else
			++currentCartPage;
		
		cartService.setPageNumber(chatId, currentCartPage);
		telegramService.editMessageText(new MessageEdit(chatId, messageId,
				createProductText(cartItems.get((int) currentCartPage)),
				createCartKeyboard(cartItems, (int) currentCartPage)));
	}
		
	public void deleteProduct(long chatId, long messageId) {
		var cartItems = cartService.findAllCartItemsByChatId(chatId);
		var currentCartNumber = cartService.findPageNumberByChatId(chatId);
		if (!cartItems.isEmpty()) {
			var cartItem = cartItems.get((int) currentCartNumber);
			if (cartItem != null) {
				cartItems.remove(currentCartNumber);
				cartService.deleteCartItem(chatId, cartItem.getId());
			}
		}
		if (cartItems.isEmpty()) {
			telegramService.editMessageText(new MessageEdit(messageId, chatId, "Your cart's cleared."));
			return;
		}
		if (cartItems.size() == currentCartNumber) {
			--currentCartNumber;
			cartService.setPageNumber(chatId, currentCartNumber);
		}
		
		telegramService.editMessageText(new MessageEdit(messageId, chatId,
				createProductText(cartItems.get((int) currentCartNumber)),
				createCartKeyboard(cartItems, (int) currentCartNumber)));
	}
	
	public void processOrder(long chatId, long messageId) {
		telegramService.editMessageText(new MessageEdit(messageId, chatId, "Creating order..."));
		orderStepService.revokeStepOrder(chatId);
		orderStepService.nextStepOrder(chatId);
	}
	
	@Override
	public void execute(Long chatId) {
		var cartItems = cartService.findAllCartItemsByChatId(chatId);
		cartService.setPageNumber(chatId, 0);
		if (cartItems.isEmpty()) {
			telegramService.sendMessage(new MessageSend(chatId, "Your cart's empty."));
			return;
		}
		telegramService.sendMessage(new MessageSend(chatId,
				createProductText(cartItems.get(0)), createCartKeyboard(cartItems, 0)));
	}
}