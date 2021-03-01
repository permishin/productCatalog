package com.example.productcatalog.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "ID", length = 50, nullable = false)
    private Long order_id;

    @ManyToOne
    private Product product;

    @ManyToMany
    @Column(name = "PRODUCTLIST", length = 225, nullable = false)
    private List<Product> orderProductList;

    public Order() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order(List<Product> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public List<Product> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<Product> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
