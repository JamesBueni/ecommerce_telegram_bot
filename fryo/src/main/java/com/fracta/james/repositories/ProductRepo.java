package com.fracta.james.repositories;

import java.util.List;

import com.fracta.james.domain.entities.Product;

public interface ProductRepo {
	
	Product findById(long id);
	List<Product> findAllByCategoryName(String categoryName, int size);
}
