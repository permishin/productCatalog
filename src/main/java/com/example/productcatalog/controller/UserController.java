package com.example.productcatalog.controller;

import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.entity.Role;
import com.example.productcatalog.entity.User;
import com.example.productcatalog.repo.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    private final UserRepo userRepo;
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userList(HttpServletRequest request,
                           Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEdit(HttpServletRequest request,
                           @PathVariable User user,
                           Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());

        return "userEdit";
    }

    @PostMapping()
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            @RequestParam String password,
            Model model) {

        user.setUsername(username);
        user.setPassword(password);
        user.getRoles().clear();

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if(roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
        return "redirect:/user";
    }

    @PostMapping("/{id}/remove")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(IllegalStateException::new);
        userRepo.delete(user);
        return "redirect:/user";
    }
    @PostMapping("/addUser")
    public String addNewUser(@RequestParam String username,
                             User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.put("message", "Такой юзер уже существует!");
            model.put("users", userRepo.findAll());
            return "userList";
        }

        if (username == null || username == "" || username.contains(" ")) {
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
