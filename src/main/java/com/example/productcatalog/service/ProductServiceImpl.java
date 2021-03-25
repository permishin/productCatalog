package com.example.productcatalog.service;

import com.example.productcatalog.entity.Product;
import com.example.productcatalog.repo.ProductRepo;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> readAll() {
        return (List<Product>) productRepo.findAll();
    }

    @Override
    public boolean delete(Long id) {
        if (productRepo.existsById(id)) {
            Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
            productRepo.delete(product);
            return true;
        }
        return false;
    }
    @Override
    public Product findById(Long id) {
        return productRepo.findById(id).orElseThrow(IllegalStateException::new);
    }

    @Override
    public void create(Product product) {
        Product products = new Product();
        products.setName(product.getName());
        products.setPrice(product.getPrice());
        products.setDescription(product.getDescription());
        products.setFileName("404.jpg");
        productRepo.save(products);
    }

    @Override
    public boolean update(Product product, Long id) {
        List<Product> productList = (List<Product>) productRepo.findAll();
        Product products = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        for (Product p : productList) {
            if (p.getId().longValue() == id.longValue()) {
                if (product.getName() != null) products.setName(product.getName());
                if (product.getPrice() != null) products.setPrice(product.getPrice());
                if (product.getDescription() != null) products.setDescription(product.getDescription());
                productRepo.save(products);
                return true;
            }
        }
        return false;
    }
}
