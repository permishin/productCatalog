package com.example.productcatalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "plo")
public class ProductListOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Product product;
//
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "orders_id", foreignKey = @ForeignKey(name = "FK_PLO_ORDERS"))
    private Orders orders;

    private Double priceFinal;

    private Integer count;

    private Double costFinal;

    public ProductListOrder() {
    }

    public ProductListOrder(Product product, Double priceFinal, Integer count, Double costFinal) {
        this.product = product;
        this.priceFinal = priceFinal;
        this.count = count;
        this.costFinal = costFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Double getPriceFinal() {
        return priceFinal;
    }

    public void setPriceFinal(Double priceFinal) {
        this.priceFinal = priceFinal;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getCostFinal() {
        return costFinal;
    }

    public void setCostFinal(Double costFinal) {
        this.costFinal = costFinal;
    }
}
