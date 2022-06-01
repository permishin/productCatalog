package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Role;
import com.example.productcatalog.entity.User;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.repo.UserRepo;
import com.example.productcatalog.service.ControllerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserRepo userRepo;

    private final ControllerService controllerService;

    public UserController(UserRepo userRepo, ControllerService controllerService) {
        this.userRepo = userRepo;
        this.controllerService = controllerService;
    }

    //Геттер страница пользователей
    @GetMapping
    public String userList(HttpServletRequest request,
                           Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());
        return "userList";
    }

    //Страница редактора пользователя
    @GetMapping("{user}")
    public String userEdit(HttpServletRequest request,
                           @PathVariable User user,
                           Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());

        return "userEdit";
    }

    //Метод пост редактора пользователя
    @PostMapping()
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            @RequestParam String password) {
        controllerService.editUser(username, form, user, password);
        return "redirect:/user";
    }

    //Удаление пользователя
    @PostMapping("/{id}/remove")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(IllegalStateException::new);
        userRepo.delete(user);
        return "redirect:/user";
    }

    //Добавление пользователя
    @PostMapping("/addUser")
    public String addNewUser(@RequestParam String username,
                             User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.put("message", "Такой юзер уже существует!");
            model.put("users", userRepo.findAll());
            return "userList";
        }
        if (username == null || username.equals("") || username.contains(" ")) {
            model.put("message", "Поле Логин не может быть пустым и содержать пробелы");
            model.put("users", userRepo.findAll());
            return "userList";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        return "redirect:/user";
    }
}
