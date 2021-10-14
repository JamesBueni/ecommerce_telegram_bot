package com.fracta.james.services.impl;

import java.util.List;

import com.fracta.james.domain.entities.Category;
import com.fracta.james.repositories.CategoryRepo;
import com.fracta.james.repositories.impl.CategoryRepoDefault;
import com.fracta.james.services.CategoryService;

public class CategoryServiceDefault implements CategoryService {

	private static final CategoryService INSTANCE = new CategoryServiceDefault();
	private final CategoryRepo repo = new CategoryRepoDefault();
	
	public CategoryServiceDefault() {
	}
	
	public static CategoryService getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<Category> findAll() {
		return repo.findAll();
	}
	

}
