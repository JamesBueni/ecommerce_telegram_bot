package com.fracta.james.services;

import java.util.List;

import com.fracta.james.domain.entities.Product;

public interface ProductService {
	
	Product findById(long id);
	List<Product> findAllByCategoryName(String categoryName);
}
