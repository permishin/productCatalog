package com.example.productcatalog.service;

import com.example.productcatalog.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> readAll();

    boolean delete(Long id);

    Product findById(Long id);

    void create(Product product);

    boolean update(Product product, Long id);

}
