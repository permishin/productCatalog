package com.example.productcatalog.entity;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class CartBean {

    private ArrayList<Product> prod = new ArrayList<Product>();
    private List<Integer> ids = new ArrayList<Integer>();

    public synchronized void addItemProduct(Product product) {
        prod.add(product);
    }
    public synchronized void addItem(int id) {
        ids.add(id);
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

    public synchronized List<Integer> getIds() {
        return new ArrayList<Integer>(ids);
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


}
