package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Order, Long> {

}
