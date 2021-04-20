package com.example.productcatalog.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "id", length = 50, nullable = false)
    private Long id;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductListOrder> productListOrder;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date;

    private String email;

    private String address;

    private String comment;

    @Column(name = "orderprice")
    private Double orderPrice;

    public Orders() {
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ProductListOrder> getProductListOrder() {
        return productListOrder;
    }

    public void setProductListOrder(List<ProductListOrder> productListOrder) {
        this.productListOrder = productListOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
