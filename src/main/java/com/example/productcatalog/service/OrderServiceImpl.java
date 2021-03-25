package com.example.productcatalog.service;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.ProductListOrder;
import com.example.productcatalog.repo.OrderRepo;
import com.example.productcatalog.repo.ProductListOrderRepo;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;



@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    private final ProductListOrderRepo productListOrderRepo;

    public OrderServiceImpl(OrderRepo orderRepo, ProductListOrderRepo productListOrderRepo) {
        this.orderRepo = orderRepo;
        this.productListOrderRepo = productListOrderRepo;
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
    @Override
    public Orders findById(Long id) {
        return orderRepo.findById(id).orElseThrow(IllegalStateException::new);
    }

    @Override
    public void create(Orders order) {
        Orders orders = new Orders();
        orders.setAddress(order.getAddress());
        orders.setDate(new Date());
        orders.setEmail(order.getEmail());
        orderRepo.save(orders);
        for(ProductListOrder p : order.getProductListOrder()) {
            p.setOrders(orders);
            productListOrderRepo.save(p);
        }
    }

    @Override
    public boolean update(Orders order, Long id) {
        List<Orders> ordersList = (List<Orders>) orderRepo.findAll();
        Orders orders = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
        for (Orders o : ordersList) {
            if (o.getId().longValue() == id.longValue()) {
                if (order.getAddress() != null) orders.setAddress(order.getAddress());
                if (order.getEmail() != null) orders.setEmail(order.getEmail());
                if (order.getProductListOrder() != null) orders.setProductListOrder(order.getProductListOrder());
                orderRepo.save(orders);
                return true;
            }
        }
            return false;
    }


}