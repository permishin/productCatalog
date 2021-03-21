package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.entity.ProductListOrder;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.model.OrderMethod;
import com.example.productcatalog.repo.OrderRepo;
import com.example.productcatalog.repo.ProductListOrderRepo;
import com.example.productcatalog.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class OrderController {

    private final OrderRepo orderRepo;

    private final ProductListOrderRepo productListOrderRepo;

    private final ProductRepo productRepo;

    public OrderController(OrderRepo orderRepo, ProductListOrderRepo productListOrderRepo, ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.productListOrderRepo = productListOrderRepo;
    }

    @Value("${upload.path}")
    private String uploadPath;
    // Гет страницы orders
    @GetMapping("/orders")
    public String orders(HttpServletRequest request,
                         Model model) {
        Iterable<Orders> list = orderRepo.findAll();
        model.addAttribute("orders", list);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());
        return "orders";
    }
    // Пост удаления заказа
    @PostMapping("/{order_id}/orderDelete")
    public String deleteOrder(@PathVariable(value = "order_id") Long order_id){
        Orders orders = orderRepo.findById(order_id).orElseThrow(IllegalStateException::new);
        orderRepo.delete(orders);
        return "redirect:/orders";
    }
    //Страница редактирования заказа
    @GetMapping("/orders/{id}/edit")
    public String orderEdit(@PathVariable(value = "id") Long id,
                            Model model) {
        if (!orderRepo.existsById(id)) {
            return "redirect:/orders";
        }
        Orders order = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
        Iterable<Product> productList = productRepo.findAll();
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("orderEdit", order);
        model.addAttribute("productList", productList);
        return "orderEdit";
    }
    //Добавить продукт в заказ
    @GetMapping("/orders/{id}/addProduct/{idProduct}")
    public String addProductToOrder(@PathVariable(value = "id") Long id,
                                    @PathVariable (value = "idProduct") Long idProduct) {
        Orders order = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
        Product product = productRepo.findById(idProduct).orElseThrow(IllegalStateException::new);
        order.setProductListOrder(OrderMethod.addProductToOrders(product,order));
        orderRepo.save(order);
        return "redirect:/orders/{id}/edit";
    }
    //Удалить продукт из заказа
    @GetMapping("/orders/{id}/deleteProduct/{idProduct}")
    public String deleteProductToOrder(@PathVariable(value = "id") Long id,
                                       @PathVariable (value = "idProduct") Long idProduct) {
        Orders order = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
        Product product = productRepo.findById(idProduct).orElseThrow(IllegalStateException::new);
        order.setProductListOrder(OrderMethod.DeleteProductFromOrders(product,order));
        if (order.getProductListOrder().isEmpty()) {
            orderRepo.delete(order);
        } else {
            orderRepo.save(order);
        }
        return "redirect:/orders/{id}/edit";
    }
    //Изменить количество продукта в заказе
    @PostMapping("/orders/{id}/changeCount/{idProduct}")
    public String changeCount(@RequestParam Integer count,
                              @PathVariable (name = "idProduct") Long idProduct,
                              @PathVariable (name = "id") Long id) {
        ProductListOrder product = productListOrderRepo.findById(idProduct).orElseThrow(IllegalStateException::new);
        product.setCount(count);
        product.setCostFinal(product.getPriceFinal() * product.getCount());
        productListOrderRepo.save(product);
        return "redirect:/orders/{id}/edit";
    }
}
