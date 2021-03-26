package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Product;

import org.junit.Assert;
import org.junit.jupiter.api.Test;


class MainControllerTest {


    @Test
    void add() {
        Product product = new Product("Пельмень", "Ничего себе пельмень", (double) 550);
        Assert.assertNotNull(product);
        Assert.assertEquals("Пельмень", product.getName());
        Assert.assertEquals((Double)550.0, product.getPrice());
    }
}