package com.example.productcatalog.controller;

import com.example.productcatalog.entity.ProductListOrder;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.repo.OrderRepo;
import com.example.productcatalog.repo.ProductListOrderRepo;
import com.example.productcatalog.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ShoppingController {

    private final ProductRepo prodRepo;

    private final OrderRepo orderRepo;

    private final ProductListOrderRepo productListOrderRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public ShoppingController(ProductRepo prodRepo, OrderRepo orderRepo, ProductListOrderRepo productListOrderRepo) {
        this.prodRepo = prodRepo;
        this.orderRepo = orderRepo;
        this.productListOrderRepo = productListOrderRepo;
    }

    @RequestMapping({ "/buyProduct" })
    public String listProductHandler(HttpServletRequest request,
                                     Model model,
                                     @RequestParam(value = "id", defaultValue = "") Long id){
        HttpSession session = request.getSession();
        CartBean bean = CartBean.get(session);
        Product product = prodRepo.findById(id).orElseThrow(IllegalStateException::new);
        bean.findID(id);
        if(bean.findIDNoAdd(id) == false) {
            bean.addItemProduct(product);
        }
        product.setCount(1);
        model.addAttribute("cart", bean);
        return "redirect:/shoppingCart";
    }
    @GetMapping("/shoppingCart")
    public String shoppingCart(HttpServletRequest request,
                               Model model) {
        HttpSession session = request.getSession();
        CartBean bean = CartBean.get(session);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("cartForm", bean);
        model.addAttribute("totalCost", bean.totalCost());
        model.addAttribute("quantity", bean.quantity());
        return "shoppingCart";
    }

    @PostMapping("/{id}/shoppingCart")
    public String remoteProduct(HttpServletRequest request,
                                @PathVariable(value = "id") Long id) {
        HttpSession session = request.getSession();
        CartBean bean = CartBean.get(session);
        Product product = prodRepo.findById(id).orElseThrow(IllegalStateException::new);
        bean.deleteItemProduct(product);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/{id}/updateCount")
    public String updateCount(HttpServletRequest request,
                              @RequestParam Integer count,
                              @PathVariable(value = "id") Long id) {
        HttpSession session = request.getSession();
        CartBean bean = CartBean.get(session);
        bean.getProd().get(bean.number(id)).setCount(count);
        if(count == 0) {
            Product product = prodRepo.findById(id).orElseThrow(IllegalStateException::new);
            bean.deleteItemProduct(product);
        }
        return "redirect:/shoppingCart";
    }

    @PostMapping("/makeOrder")
    public String makeOrder(HttpServletRequest request,
                            @RequestParam(name = "email") String email,
                            @RequestParam(name = "address") String address) {
        HttpSession session = request.getSession();
        CartBean bean = CartBean.get(session);
        Orders orders = new Orders();
        orders.setDate(new Date());
        orders.setEmail(email);
        orders.setAddress(address);
        orderRepo.save(orders);
        List<ProductListOrder> list = bean.saveSessionToProductListOrder(bean.getProd(), orders);
        for(int i = 0; i < list.size(); i ++){
            productListOrderRepo.save(list.get(i));
        }
        bean.deleteAll(bean);
        return "redirect:/orders";
    }
}
