package com.example.productcatalog.model;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.entity.ProductListOrder;

import java.util.List;

public class OrderMethod {
    //Добавить продукт в заказ (редактирование)
    public static synchronized List addProductToOrders(Product product, Orders order) {
        boolean isNew = false;
        List<ProductListOrder> productList = order.getProductListOrder();
        for(ProductListOrder s : productList) {
            if (s.getProduct().getId() == product.getId()) {
                s.setCount(s.getCount() + 1);
                s.setCostFinal(s.getCount() * s.getPriceFinal());
                isNew = false;
                break;
            } else {
                isNew = true;
            }
        }
        if (isNew) {
            ProductListOrder productListOrder = new ProductListOrder();
            productListOrder.setProduct(product);
            productListOrder.setPriceFinal(product.getPrice());
            productListOrder.setCount(product.getCount());
            productListOrder.setCostFinal(product.getPrice() * product.getCount());
            productListOrder.setOrders(order);
            productList.add(productListOrder);
            return productList;
        }
        return productList;
    }
    //Удалить продукт из заказа
    public static synchronized List DeleteProductFromOrders(Product product, Orders order) {
       List<ProductListOrder> productList = order.getProductListOrder();
       for (int i = 0; i < productList.size(); i ++) {
           if (productList.get(i).getProduct().getId() == product.getId()) {
               productList.remove(productList.get(i));
           }
       }
        return productList;
    }

}
