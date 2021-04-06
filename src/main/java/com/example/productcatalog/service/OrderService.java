package com.example.productcatalog.service;

import com.example.productcatalog.entity.Orders;
import java.util.List;

public interface OrderService {

    List<Orders> readAll();

    boolean delete(Long id);

    Orders findById(Long id);

    void create(Orders order);

    boolean update(Orders order, Long id);
}
