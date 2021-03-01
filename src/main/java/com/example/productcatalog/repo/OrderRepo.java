package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Order;
import com.example.productcatalog.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Order, Long> {
}
