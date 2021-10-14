package com.fracta.james.services.impl;

import java.util.List;

import com.fracta.james.domain.models.CartItem;
import com.fracta.james.exceptions.ValidationException;
import com.fracta.james.repositories.CartRepo;
import com.fracta.james.repositories.impl.CartRepoDefault;
import com.fracta.james.services.CartService;

public class CartServiceDefault implements CartService {

	private static final CartService INSTANCE = new CartServiceDefault();
	private final CartRepo repo = new CartRepoDefault();
	
	public CartServiceDefault() {
	}
	
	public static CartService getInstance() {
		return INSTANCE;
	}

	@Override
	public void saveCartItem(long chatId, CartItem cartItem) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		if (cartItem == null)
			throw new IllegalArgumentException("cartItem shouldn't be null");
		if (cartItem.getProduct() == null)
			throw new ValidationException("cartItem's product shouldn't be null");
		repo.saveCartItem(chatId, cartItem);
	}

	@Override
	public void updateCartItem(long chatId, CartItem cartItem) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		if (cartItem == null)
			throw new IllegalArgumentException("cartItem shouldn't be null");
		if (cartItem.getProduct() == null)
			throw new ValidationException("cartItem's product shouldn't be null");
		repo.updateCartItem(chatId, cartItem);
	}

	@Override
	public void deleteCartItem(long chatId, long cartItemId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		if (Long.valueOf(cartItemId) == null)
			throw new IllegalArgumentException("cartItemId shouldn't be null");
		repo.deleteCartItem(chatId, cartItemId);
	}

	@Override
	public List<CartItem> findAllCartItemsByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		return repo.findAllCartItemsByChatId(chatId);
	}

	@Override
	public CartItem findCartItemByChatIdAndProductId(long chatId, long productId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		if (Long.valueOf(productId) == null)
			throw new IllegalArgumentException("productId shouldn't be null");
		return repo.findCartItemByChatIdAndProductId(chatId, productId);
	}

	@Override
	public void deleteAllCartItemsByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		repo.deleteAllCartItemsByChatId(chatId);
	}

	@Override
	public void setPageNumber(long chatId, long pageNumber) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		if (Long.valueOf(pageNumber) == null)
			throw new IllegalArgumentException("pageNumber shouldn't be null");
		repo.setPageNumber(chatId, pageNumber);
	}

	@Override
	public long findPageNumberByChatId(long chatId) {
		if (Long.valueOf(chatId) == null)
			throw new IllegalArgumentException("chatId shouldn't be null");
		return repo.findPageNumberByChatId(chatId);
	}

	@Override
	public float calculateTotalPrice(List<CartItem> cartItems) {
		var totalPrice = 0F;
		for(var cartItem : cartItems)
			totalPrice += cartItem.getTotalPrice();
		
		return totalPrice;
	}
}
