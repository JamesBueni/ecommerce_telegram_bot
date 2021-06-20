package com.fracta.james.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fracta.james.domain.models.CartItem;
import com.fracta.james.repositories.CartRepo;
import com.fracta.james.utils.ClonerUtils;

public class CartRepoDefault implements CartRepo {

	private final AtomicInteger lastCartItemId = new AtomicInteger();
	private final Map<Long, List<CartItem>> cartItems = new HashMap<>();
	
	private final Map<Long, Long> cartPageNumbers = new HashMap<>();
	
	@Override
	public void updateCartItem(long chatId, CartItem cartItem) {
		cartItems.computeIfAbsent(chatId, cartItems -> new ArrayList<>());
		var receivedCartItems = cartItems.get(chatId);
		IntStream.range(0, receivedCartItems.size())
				.filter(i -> Long.valueOf(cartItem.getId()).equals(receivedCartItems.get(i).getId()))
				.findFirst()
				.ifPresent(i -> receivedCartItems.set(i, ClonerUtils.cloneObject(cartItem)));
	}

	@Override
	public void saveCartItem(long chatId, CartItem cartItem) {
		cartItems.computeIfAbsent(chatId, cartItems -> new ArrayList<>());
		cartItem.setId(lastCartItemId.incrementAndGet());
		cartItems.get(chatId).add(ClonerUtils.cloneObject(cartItem));
	}

	@Override
	public void deleteCartItem(long chatId, long cartItemId) {
		cartItems.computeIfAbsent(chatId, cartItems -> new ArrayList<>());
		var receivedCartItems = cartItems.get(chatId);
		receivedCartItems.stream()
				.filter(cartItem -> Long.valueOf(cartItem.getId()).equals(cartItemId))
				.findFirst()
				.ifPresent(receivedCartItems::remove);
		
	}

	@Override
	public List<CartItem> findAllCartItemsByChatId(long chatId) {
		cartItems.computeIfAbsent(chatId, cartItems -> new ArrayList<>());
		return cartItems.get(chatId).stream()
				.map(ClonerUtils::cloneObject)
				.collect(Collectors.toList());
	}

	@Override
	public CartItem findCartItemByChatIdAndProductId(long chatId, long productId) {
		cartItems.computeIfAbsent(chatId, cartItems -> new ArrayList<>());
		return cartItems.get(chatId).stream()
				.filter(cartItem -> Long.valueOf(cartItem.getProduct().getId()).equals(productId))
				.findFirst()
				.map(ClonerUtils::cloneObject)
				.orElse(null);
	}

	@Override
	public void deleteAllCartItemsByChatId(long chatId) {
		cartItems.remove(chatId);
	}

	@Override
	public void setPageNumber(long chatId, long pageNumber) {
		cartPageNumbers.put(chatId, pageNumber);
	}

	@Override
	public long findPageNumberByChatId(long chatId) {
		return cartPageNumbers.getOrDefault(chatId, 0L);
	}
	
	
}
