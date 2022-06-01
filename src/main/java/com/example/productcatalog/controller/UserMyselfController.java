package com.example.productcatalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserMyselfController {

    /**
     * Получение информации о текущем пользователе
     *
     * @return возвращает страницу userInfo.html
     */
    @GetMapping("/user/userInfo")
    public String userInfo() {
        return "userInfo";
    }
}
