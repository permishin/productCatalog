package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Role;
import com.example.productcatalog.entity.User;
import com.example.productcatalog.repo.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //Страница регистрации
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    //Пост регистрации
    @PostMapping("/registration")
    public String addNewUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.put("message", "Такой юзер уже существует!");
            return "registration";
        }
        if (!user.getPassword().equals(user.getPasswordRetry())) {
            model.put("message", "Введенные пароли не совпадают! Пишите медленнее и более вдумчиво...");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        return "redirect:/login";
    }
}
