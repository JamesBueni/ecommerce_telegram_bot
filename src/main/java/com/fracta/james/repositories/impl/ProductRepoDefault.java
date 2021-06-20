package com.fracta.james.repositories.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fracta.james.domain.entities.Product;
import com.fracta.james.repositories.ProductRepo;

public class ProductRepoDefault implements ProductRepo {
	
	private final SessionFactory sessionFactory = HibernateFactory.getSessionFactory();

	@Override
	public Product findById(long id) {
		var session = sessionFactory.openSession();
		var product = session.get(Product.class, id);
		session.close();
		return product;
	}

	@Override
	public List<Product> findAllByCategoryName(String categoryName) {
		var session = sessionFactory.openSession();
		List<Product> products = session.createQuery("from Product where categoryName = :categoryName")
				.setParameter("categoryName", categoryName).getResultList();
		session.close();
		return products;
	}
}
