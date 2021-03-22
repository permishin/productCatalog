package com.example.productcatalog.service;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.model.OrdersModel;
import com.example.productcatalog.repo.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepo orderRepo;

    public OrderServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public List<Orders> readAll() {
        return (List<Orders>) orderRepo.findAll();
    }

    @Override
    public boolean delete(Long id) {
        if (orderRepo.existsById(id)) {
            Orders orders = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
            orderRepo.delete(orders);
            return true;
        }
        return false;
    }
}