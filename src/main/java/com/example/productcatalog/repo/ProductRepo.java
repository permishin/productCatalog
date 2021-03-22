package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public interface ProductRepo extends CrudRepository<Product, Long> {

    default List<Product> filter(List<Product> product, String filter) {
        List<Product> result = new ArrayList<>();
        for (Product p : product) {
            if (p.getName().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))){
                result.add(p);
            }
        }
        return result;
    }

}
