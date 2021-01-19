package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Product;
import org.springframework.data.repository.CrudRepository;


public interface ProductRepo extends CrudRepository<Product, Long> {
    Iterable<Product> findByName(String name);
}
