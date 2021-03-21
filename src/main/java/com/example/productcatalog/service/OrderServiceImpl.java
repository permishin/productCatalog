package com.example.productcatalog.service;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.model.OrdersModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final List<OrdersModel> CLIENT_REPOSITORY_MAP = new ArrayList<>();

    @Override
    public List<OrdersModel> readAll() {
        return new ArrayList<>(CLIENT_REPOSITORY_MAP);
    }
}
