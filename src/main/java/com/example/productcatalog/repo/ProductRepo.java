package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductRepo extends CrudRepository<Product, Long> {
    Iterable<Product> findByName(String name);
    List<Product> findByNameIsContaining(String name);
}
