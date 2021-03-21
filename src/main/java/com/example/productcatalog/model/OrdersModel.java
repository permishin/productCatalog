package com.example.productcatalog.model;

import com.example.productcatalog.entity.ProductListOrder;

import java.util.List;

public class OrdersModel {

    private Long id;
    private List<ProductListOrder> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductListOrder> getList() {
        return list;
    }

    public void setList(List<ProductListOrder> list) {
        this.list = list;
    }
}
