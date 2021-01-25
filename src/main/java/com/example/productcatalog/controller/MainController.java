package com.example.productcatalog.controller;

import com.example.productcatalog.entity.Product;
import com.example.productcatalog.repo.ProductRepo;
import com.example.productcatalog.service.LoadImages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


@Controller
public class MainController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private LoadImages loadImages;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String main(Model model) {
        Iterable<Product> list = productRepo.findAll();
        model.addAttribute("title", "Каталог товаров");
        model.addAttribute("list", list);
        return "main";
    }

    @PostMapping("/")
    public String add(@RequestParam("file") MultipartFile file,
                    @RequestParam String name,
                      @RequestParam String description, Model model) throws IOException {
        Product product = new Product(name, description);

        if (file != null) {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            product.setFilename(resultFileName);

        }
        productRepo.save(product);
        Iterable<Product> list = productRepo.findAll();
        model.addAttribute("list", list);
        return "main";
    }

    @PostMapping("/{id}/remove")
    public String delete(@PathVariable(value = "id") Long id) {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
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
        model.addAttribute("title", "Редактировать продкт");
        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String PostEdit(@PathVariable(value = "id") Long id, @RequestParam String name, @RequestParam String description, @RequestParam("file") MultipartFile file) throws IOException {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        String fileImg = loadImages.loadImg(file);
        product.setFilename(fileImg);
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
