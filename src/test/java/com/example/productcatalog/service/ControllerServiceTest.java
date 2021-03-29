package com.example.productcatalog.service;

import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.repo.OrderRepo;
import com.example.productcatalog.repo.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ControllerServiceTest {

    @Autowired
    private ControllerService controllerService;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private ProductRepo productRepo;
    //Сохранение заказа
    @Test
    void saveOrder() {
        Orders orders = new Orders();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        controllerService.saveOrder(orders,
                "er@er.ru",
                "Moscow",
                "Like a BOSS",
                CartBean.get(servletRequest.getSession()));
        Mockito.verify(orderRepo, Mockito.times(1)).save(orders);
    }
    //Сохранение продукта
    @Test
    void saveProduct() {
        Product product = new Product();
        MockMultipartFile file = new MockMultipartFile("foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes());
        controllerService.saveProduct(product, "Кисель", "На луне", 550.0, file);
        Mockito.verify(productRepo, Mockito.times(1)).save(product);
    }

}