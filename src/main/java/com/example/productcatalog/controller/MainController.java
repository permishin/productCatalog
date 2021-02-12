package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Product;
import com.example.productcatalog.plugin.S3Amazon;
import com.example.productcatalog.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MainController {

    private S3Amazon s3Amazon;
    private final ProductRepo productRepo;


    public MainController(S3Amazon s3Amazon, ProductRepo productRepo) {
        this.s3Amazon = s3Amazon;
        this.productRepo = productRepo;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String main(Model model) {
        Iterable<Product> list = productRepo.findAll();
        model.addAttribute("title", "Каталог товаров");
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("list", list);
        return "main";
    }

    @PostMapping("/")
    public String add(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        Product product = new Product(name, description);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            product.setFileName(resultFileName);
            s3Amazon.uploadFile(resultFileName, file);
        }
        productRepo.save(product);
        Iterable<Product> list = productRepo.findAll();
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("list", list);
        return "main";
    }

    @PostMapping("/{id}/remove")
    public String delete(@PathVariable(value = "id") Long id) {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        try {
            s3Amazon.deleteFile(product.getFileName());
        } catch (IllegalArgumentException a) {
            a.getMessage();
        }
        productRepo.delete(product);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") Long id, Model model) {
        if (!productRepo.existsById(id)) {
            return "redirect:/";
        }
        Optional<Product> user = productRepo.findById(id);
        ArrayList<Product> list = new ArrayList<>();
        user.ifPresent(list::add);
        model.addAttribute("user", list);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("title", "Редактировать продукт");
        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String PostEdit(@PathVariable(value = "id") Long id,
                           @RequestParam String name,
                           @RequestParam String description,
                           @RequestParam("file") MultipartFile file) {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        String resultFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            s3Amazon.uploadFile(resultFileName, file);
            s3Amazon.deleteFile(product.getFileName());
            product.setFileName(resultFileName);
        }
            product.setName(name);
            product.setDescription(description);
            productRepo.save(product);
        return "redirect:/";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Model model) {
        Iterable<Product> product;
        if (filter != null && !filter.isEmpty()) {
            product = productRepo.findByName(filter);
        } else {
            product = productRepo.findAll();
        }
        model.addAttribute("list", product);
        return "main";
    }
}
