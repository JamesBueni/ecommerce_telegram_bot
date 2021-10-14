package com.fracta.james.commands.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.fracta.james.commands.Command;
import com.fracta.james.commands.Commands;
import com.fracta.james.domain.entities.Product;
import com.fracta.james.domain.models.CartItem;
import com.fracta.james.domain.models.InlineQuerySend;
import com.fracta.james.domain.models.MessageEdit;
import com.fracta.james.domain.models.MessagePlaceholder;
import com.fracta.james.services.CartService;
import com.fracta.james.services.MessageService;
import com.fracta.james.services.ProductService;
import com.fracta.james.services.TelegramService;
import com.fracta.james.services.impl.CartServiceDefault;
import com.fracta.james.services.impl.MessageServiceCached;
import com.fracta.james.services.impl.ProductServiceDefault;
import com.fracta.james.services.impl.TelegramServiceDefault;
import com.fracta.james.utils.KeyboardUtils;

public class ShowProductsCommand implements Command<InlineQuery>{
	
	private static final ShowProductsCommand INSTANCE = new ShowProductsCommand();
	
	private final MessageService messageService = new MessageServiceCached();
	private final ProductService productService = new ProductServiceDefault();
	private final CartService cartService = new CartServiceDefault();
	private final TelegramService telegramService = new TelegramServiceDefault();
	
	private static final int PRODUCTS_QUANTITY_PER_PAGE = 50;
	private static final int MAX_QUANTITY_PER_PRODUCT = 50;
	private static final int MAX_PRODUCTS_QUANTITY_PER_CART = 50;

	public ShowProductsCommand() {
	}
	
	public static ShowProductsCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(InlineQuery inlineQuery) {
		Long chatId = inlineQuery.getFrom().getId().longValue();
        String inlineQueryId = inlineQuery.getId();
        String categoryName = inlineQuery.getQuery();

        List<Product> products = productService.findAllByCategoryName(categoryName, PRODUCTS_QUANTITY_PER_PAGE);
        if (!products.isEmpty())
            sendProductsQuery(chatId, inlineQueryId, products);
	}
	

    private String createProductText(Long chatId, Product product) {
    	var message = messageService.findByName("PRODUCT_MESSAGE");
        message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_PHOTO_URL%", product.getPhotoUrl()));
        message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_NAME%", product.getName()));
        message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_DESCRIPTION%", product.getDesc()));

        var cartItem = cartService.findCartItemByChatIdAndProductId(chatId, product.getId());
        if (cartItem == null) {
            message.removeTextBetweenPlaceholder("%PRODUCT_PRICES%");
        }
        else {
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_PRICE%", product.getPrice()));
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_QUANTITY%", cartItem.getQuantity()));
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_TOTAL_PRICE%", cartItem.getTotalPrice()));
        }
        return message.buildText();
    }
    
    private InlineKeyboardMarkup createProductKeyboard(Long chatId, Product product) {
    	var rows = new ArrayList<List<InlineKeyboardButton>>();
    	var productId = product.getId();
    	var cartItem = cartService.findCartItemByChatIdAndProductId(chatId, productId);
    	
    	if (cartItem != null) {
    		var firstInlineButtons = new ArrayList<InlineKeyboardButton>();
    		var minusButton = new InlineKeyboardButton("\u2796");
			minusButton.setCallbackData("show-products=minus-product_" + productId);
    		var plusButton = new InlineKeyboardButton("\u2796");
    		plusButton.setCallbackData("show-products=plus-product_" + productId);
    		var cartButton = new InlineKeyboardButton(cartItem.getQuantity() + " pcs");
    		cartButton.setCallbackData("show-products=product-quantity");
    	
    		var secondInlineButtons = new ArrayList<InlineKeyboardButton>();
    		var openCartButton = new InlineKeyboardButton(Commands.CART_COMMAND);
    		openCartButton.setCallbackData("show-products=open-cart");
    		var openCatalogButton = new InlineKeyboardButton(Commands.CATALOG_COMMAND);
    		openCatalogButton.setCallbackData("show-products=open-catalog");
    		
    		firstInlineButtons.addAll(List.of(minusButton, plusButton, cartButton));
    		secondInlineButtons.addAll(List.of(openCartButton, openCatalogButton));
    		rows.addAll(List.of(firstInlineButtons, secondInlineButtons));
    	}
    	else {
    		var inlineButtons = new ArrayList<InlineKeyboardButton>();
    		var plusButton = new InlineKeyboardButton(String.format(
    				"\uD83D\uDCB5 Price: $%.2f \uD83D\uDECD Add to cart", product.getPrice()));
    		plusButton.setCallbackData("show-products=plus-product");
    		
    		inlineButtons.add(plusButton);
    		rows.add(inlineButtons);
    	}
    	
    	var inlineKeyboardMarkup = new InlineKeyboardMarkup(rows);
    	inlineKeyboardMarkup.setKeyboard(rows);
    	return inlineKeyboardMarkup;
    }
    
	public void minusProduct(Long chatId, String inlineMessageId, String data) {
		var productId = Integer.parseInt(data.split("_")[1]);
		var cartItem = cartService.findCartItemByChatIdAndProductId(chatId, productId);
		var product = cartItem != null ? cartItem.getProduct() : productService.findById(productId);
     
		if (product == null)
			return;
		if (cartItem != null) {
    	if (cartItem.getQuantity() > 1) {
    		cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartService.updateCartItem(chatId, cartItem);
        }
    	else
    		cartService.deleteCartItem(chatId, cartItem.getId());
        }
     
        telegramService.editMessageText(new MessageEdit(
        		inlineMessageId, createProductText(chatId, product), createProductKeyboard(chatId, product)));
    }

    public void plusProduct(Long chatId, String inlineMessageId, String data) {
        var productId = Integer.parseInt(data.split("_")[1]);
        var cartItem = cartService.findCartItemByChatIdAndProductId(chatId, productId);
        var product = cartItem != null ? cartItem.getProduct() : productService.findById(productId);
        if (product == null)
            return;

        if (cartItem != null) {
            if (cartItem.getQuantity() < MAX_QUANTITY_PER_PRODUCT) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartService.updateCartItem(chatId, cartItem);
            }
        } else {
            if (cartService.findAllCartItemsByChatId(chatId).size() < MAX_PRODUCTS_QUANTITY_PER_CART)
            	cartService.saveCartItem(chatId, new CartItem(product, 1));

        }
        telegramService.editMessageText(new MessageEdit(
                inlineMessageId, createProductText(chatId, product), createProductKeyboard(chatId, product)));
    }
    
    private List<InlineQueryResult> createProductsInlineQuery(Long chatId, List<Product> products) {
        List<InlineQueryResult> productsResult = new ArrayList<>();
        for (Product product : products) {
        	var inlineQueryResultArticle = new InlineQueryResultArticle();
        	inlineQueryResultArticle.setId(Long.valueOf(product.getId()).toString());
        	inlineQueryResultArticle.setThumbUrl(Long.valueOf(product.getPhotoUrl()).toString());
        	inlineQueryResultArticle.setThumbWidth(48);
        	inlineQueryResultArticle.setThumbHeight(48);
        	inlineQueryResultArticle.setTitle(product.getName());
        	inlineQueryResultArticle.setDescription(product.getDesc());
        	inlineQueryResultArticle.setReplyMarkup(createProductKeyboard(chatId, product));
        	
        	var inputMessageContent = new InputTextMessageContent();
        	inputMessageContent.setParseMode("HTML");
        	inputMessageContent.setMessageText(createProductText(chatId, product));
        	inlineQueryResultArticle.setInputMessageContent(inputMessageContent);
       }
        return productsResult;
    }
    
	private void sendProductsQuery(long chatId, String inlineQueryId, List<Product> products) {
		telegramService.sendInlineQuery(new InlineQuerySend(chatId, createProductsInlineQuery(chatId, products)));
	}
}