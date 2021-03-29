package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Product;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/products-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/products-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    //Созданный продукт содержил то, что в него положили
    @Test
    public void add() {
        Product product = new Product("Пельмень", "Ничего себе пельмень", (double) 550);
        Assert.assertNotNull(product);
        Assert.assertEquals("Пельмень", product.getName());
        Assert.assertEquals((Double)550.0, product.getPrice());
    }

    //Корректность логина пользователя
    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='loginControl']").string("admin"));
    }

    //Проверка отобрбражени количества продуктв на странице
    @Test
     public void productListTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='product-list-test']/div").nodeCount(4));
    }

    //Фильтрация списка по имени
    @Test
    public void filterTest() throws Exception {
        this.mockMvc.perform(get("/main").param("filter", "вино"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='product-list-test']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='product-list-test']/div/div/div[@data-id='1']").exists())
                .andExpect(xpath("//*[@id='product-list-test']/div/div/div[@data-id='3']").exists());
    }
    //Добавление нового продукта
    @Test
    public void addProductToList() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/")
                .file("file", "123".getBytes())
                .param("name", "Bread")
                .param("price", "120.0")
                .param("description", "деревенское");
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='product-list-test']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='product-list-test']/div/div/div[@data-id='10']").exists())
                .andExpect(xpath("//div[@id='product-list-test']/div/div/div[@data-id='10']/div/h5").string("Bread"))
                .andExpect(xpath("//div[@id='product-list-test']/div/div/div[@data-id='10']/div/p").string("деревенское"));

    }
}