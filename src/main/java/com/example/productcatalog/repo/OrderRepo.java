package com.example.productcatalog.repo;

import com.example.productcatalog.entity.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Orders, Long> {

}
