package com.vasyerp.discount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vasyerp.discount.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{

	
	
}
