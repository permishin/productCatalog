package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductRepo extends CrudRepository<Product, Long> {
    List<Product> findByNameIsContaining(String name);
}
