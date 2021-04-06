package com.example.productcatalog.model;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.entity.ProductListOrder;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


public class CartBean {

    public CartBean() {
    }

    private final List<Product> prod = new ArrayList<>();

    public synchronized void addItemProduct(Product product) {
        prod.add(product);
    }
    public synchronized void deleteItemProduct(Product product) {
        for(int i = 0; i < prod.size(); i++) {
            if (product.getId().equals(prod.get(i).getId())) {
                prod.remove(i);
                break;
            }
        }
    }
    public synchronized Integer quantity() {
        int count = 0;
        for (Product product : prod) {
            count += product.getCount();
        }
        return count;
    }
    public synchronized Double totalCost() {
        double cost = 0.0;
        for(Product p : prod) {
            cost += p.getPrice() * p.getCount();
        }
        return cost;
    }
    public synchronized List<Product> getProd() {
        return new ArrayList<Product>(prod);
    }
    public static CartBean get(HttpSession session) {
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
    public synchronized void deleteAll(CartBean bean) {
        bean.prod.clear();
    }

    public synchronized Boolean findID(Long id) {
        for(int i = 0; i < getProd().size(); i++) {
            if (getProd().get(i).getId().equals(id)) {
                Integer c = getProd().get(i).getCount() + 1;
                getProd().get(i).setCount(c);
                return true;
            }
        }
        return false;
    }
    public synchronized Boolean findIDNoAdd(Long id) {
        for(int i = 0; i < getProd().size(); i++) {
            if (getProd().get(i).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    public synchronized Integer number(Long id) {
        for(int i = 0; i < getProd().size(); i ++) {
            if (getProd().get(i).getId().equals(id)){
                return i;
            }
        }
        return null;
    }

    public synchronized List<ProductListOrder> saveSessionToProductListOrder (List<Product> product, Orders order) {
        List<ProductListOrder> list = new ArrayList<>();
        ProductListOrder productListOrder;
        for (Product value : product) {
            productListOrder = new ProductListOrder();
            productListOrder.setProduct(value);
            productListOrder.setPriceFinal(value.getPrice());
            productListOrder.setCount(value.getCount());
            productListOrder.setCostFinal(value.getPrice() * value.getCount());
            productListOrder.setOrders(order);
            list.add(productListOrder);
        }
        return list;
    }
}

