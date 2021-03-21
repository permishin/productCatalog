package com.example.productcatalog.repo;

import com.example.productcatalog.entity.ProductListOrder;
import org.springframework.data.repository.CrudRepository;

public interface ProductListOrderRepo extends CrudRepository<ProductListOrder, Long> {
}
