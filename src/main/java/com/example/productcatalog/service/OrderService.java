package com.example.productcatalog.service;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.model.OrdersModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    List<Orders> readAll();

    boolean delete(Long id);
}
