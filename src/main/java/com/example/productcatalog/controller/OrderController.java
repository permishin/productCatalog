package com.example.productcatalog.controller;

import com.example.productcatalog.entity.CartBean;
import com.example.productcatalog.entity.Order;
import com.example.productcatalog.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

    private OrderRepo orderRepo;

    public OrderController(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/orders")
    public String orders(HttpServletRequest request,
                         Model model) {
        Iterable<Order> list = orderRepo.findAll();
        model.addAttribute("order", list);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());
        return "orders";
    }
    @PostMapping("/{order_id}/orderDelete")
    public String deleteOrder(@PathVariable(value = "order_id") Long order_id, Model model){
        Order order = orderRepo.findById(order_id).orElseThrow(IllegalStateException::new);
        orderRepo.delete(order);
        return "redirect:/orders";
    }
}
