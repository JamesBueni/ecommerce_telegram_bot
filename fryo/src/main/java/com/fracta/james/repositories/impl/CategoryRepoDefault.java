package com.fracta.james.repositories.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fracta.james.domain.entities.Category;
import com.fracta.james.repositories.CategoryRepo;

public class CategoryRepoDefault implements CategoryRepo {

	private final SessionFactory sessionFactory = HibernateFactory.getSessionFactory();

	@Override
	public List<Category> findAll() {
		var session = sessionFactory.openSession();
		List<Category> categories = session.createQuery("from Category").getResultList();
		session.close();
		return categories;
	}

}