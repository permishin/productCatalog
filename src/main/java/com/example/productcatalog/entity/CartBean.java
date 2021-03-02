package com.example.productcatalog.entity;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartBean {

    private ArrayList<Product> prod = new ArrayList<Product>();
   // private Map<Product, Integer> prod1 = new HashMap<>();
   // private List<Integer> ids = new ArrayList<Integer>();

    public synchronized void addItemProduct(Product product) {
        prod.add(product);
    }
   // public synchronized void addItemProduct(Product product, Integer count) { prod1.put(product,count); }
   // public synchronized void addItem(int id) {ids.add(id);}
    public synchronized void deleteItemProduct(Product product) {
        for(int i = 0; i < prod.size(); i++) {
            if (product.getId().equals(prod.get(i).getId())) {
                prod.remove(i);
                break;
            }
            }
    }
    public synchronized Integer quantity() {
        Integer quantity = prod.size();
        return quantity;
    }
    public synchronized Double totalCost() {
        Double cost = 0.0;
        for(Product p : prod) {
            cost += p.getPrice();
        }
        return cost;
    }

   // public synchronized List<Integer> getIds() {return new ArrayList<Integer>(ids);}

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
}
