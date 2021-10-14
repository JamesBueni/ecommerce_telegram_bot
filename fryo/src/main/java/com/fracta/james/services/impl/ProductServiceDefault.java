package com.fracta.james.services.impl;

import java.util.List;

import com.fracta.james.domain.entities.Product;
import com.fracta.james.repositories.ProductRepo;
import com.fracta.james.repositories.impl.ProductRepoDefault;
import com.fracta.james.services.ProductService;

public class ProductServiceDefault implements ProductService {

	private static final ProductService INSTANCE = new ProductServiceDefault();
	private final ProductRepo repo = new ProductRepoDefault();
	
	public ProductServiceDefault() {
	}

	public static ProductService getInstance() {
		return INSTANCE;
	}

	@Override
	public Product findById(long id) {
		if (Long.valueOf(id) == null)
			throw new  IllegalArgumentException("Id shouldn't be null.");
		return repo.findById(id);
	}

	@Override
	public List<Product> findAllByCategoryName(String categoryName, int size) {
		if (categoryName == null)
			throw new IllegalArgumentException("Category name shouldn't be null.");
		if (size < 1)
			throw new IllegalArgumentException("Size should be > 1.");
		
		return repo.findAllByCategoryName(categoryName, size);
	}
}