package com.fracta.james.services;

import java.util.List;

import com.fracta.james.domain.models.CartItem;

public interface CartService {
	
	void saveCartItem(long chatId, CartItem cartItem);
	void updateCartItem(long chatId, CartItem cartItem);
	void deleteCartItem(long chatId, long cartItemId);
	
	List<CartItem> findAllCartItemsByChatId(long chatId);
	CartItem findCartItemByChatIdAndProductId(long chatId, long productId);
	void deleteAllCartItemsByChatId(long chatId);
	
	void setPageNumber(long chatId, long pageNumber);
	long findPageNumberByChatId(long chatId);
	
	float calculateTotalPrice(List<CartItem> cartItems);

}
