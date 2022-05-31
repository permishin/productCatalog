package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.repo.OrderRepo;
import com.example.productcatalog.repo.ProductRepo;
import com.example.productcatalog.service.ControllerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
//@PreAuthorize("hasAuthority('ADMIN')")
public class OrderController {

    private final OrderRepo orderRepo;

    private final ProductRepo productRepo;

    private final ControllerService controllerService;

    public OrderController(OrderRepo orderRepo, ProductRepo productRepo, ControllerService controllerService) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.controllerService = controllerService;
    }

    @Value("${file.Dir}")
    private String fileDir;

    // Гет страницы orders
    @GetMapping("/orders")
    public String orders(HttpServletRequest request,
                         Model model) {
        Iterable<Orders> list = orderRepo.findAll();
        model.addAttribute("orders", list);
        model.addAttribute("uploadPath", fileDir);
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
        model.addAttribute("uploadPath", fileDir);
        model.addAttribute("orderEdit", order);
        model.addAttribute("productList", productList);
        return "orderEdit";
    }
    //Добавить продукт в заказ
    @GetMapping("/orders/{id}/addProduct/{idProduct}")
    public String addProductToOrder(@PathVariable(value = "id") Long id,
                                    @PathVariable (value = "idProduct") Long idProduct) {
        controllerService.addProductToOrder(id, idProduct);
        return "redirect:/orders/{id}/edit";
    }
    //Удалить продукт из заказа
    @GetMapping("/orders/{id}/deleteProduct/{idProduct}")
    public String deleteProductFromOrder(@PathVariable(value = "id") Long id,
                                         @PathVariable (value = "idProduct") Long idProduct) {
        controllerService.deleteProductFromOrder(id, idProduct);
        return "redirect:/orders/{id}/edit";
    }
    //Изменить количество продукта в заказе
    @PostMapping("/orders/{id}/changeCount/{idProduct}")
    public String changeCount(@RequestParam Integer count,
                              @PathVariable (name = "idProduct") Long idProduct) {
        controllerService.changeCount(count, idProduct);
        return "redirect:/orders/{id}/edit";
    }
}
