package com.example.productcatalog.controller;

import com.example.productcatalog.entity.ProductListOrder;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.repo.ProductListOrderRepo;
import com.example.productcatalog.repo.ProductRepo;
import com.example.productcatalog.service.ControllerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class MainController {

    private final ProductRepo productRepo;

    private final ProductListOrderRepo productListOrderRepo;

    private final ControllerService controllerService;

    public MainController(ProductRepo productRepo, ProductListOrderRepo productListOrderRepo, ControllerService controllerService) {
        this.productRepo = productRepo;
        this.productListOrderRepo = productListOrderRepo;
        this.controllerService = controllerService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    //Гет страницы продуктов
    @GetMapping("/")
    public String main(HttpServletRequest request,
                       Model model) {

        Iterable<Product> list = productRepo.findAll();
        model.addAttribute("title", "Каталог товаров");
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("list", list);
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());
        return "main";
    }

    //Добавление нового продукта
    @PostMapping("/")
    public String add(
            @RequestParam (defaultValue = "Без названия")String name,
            @RequestParam (defaultValue = "Без описания")String description,
            @RequestParam (defaultValue = "0")Double price,
            @RequestParam("file") MultipartFile file,
            Model model) {
        Product product = new Product();
        controllerService.saveProduct(product, name, description, price,file);
        Iterable<Product> list = productRepo.findAll();
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("list", list);
        return "main";
    }

    //Удаление продукта
    @PostMapping("/{id}/remove")
    public String delete(@PathVariable(value = "id") Long id,
                         Model model) {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        List<ProductListOrder> prodList = (List<ProductListOrder>) productListOrderRepo.findAll();
        for (ProductListOrder p : prodList) {
            if (p.getProduct().getName().contains(product.getName())) {
                model.addAttribute("message", "Не надо сюда нажимать! Продукт нельзя удалить пока он есть в сохраненном заказе =)");
                model.addAttribute("list", productRepo.findAll());
                model.addAttribute("uploadPath", uploadPath);
                return "main";
            }
        }
        controllerService.deleteProduct(id);
        return "redirect:/";
    }

    //Страница редактирование продукта
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") Long id,
                       Model model) {
        if (!productRepo.existsById(id)) {
            return "redirect:/";
        }
        Optional<Product> user = productRepo.findById(id);
        List<Product> list = new ArrayList<>();
        user.ifPresent(list::add);
        model.addAttribute("user", list);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("title", "Редактировать продукт");
        return "edit";
    }

    //Пост редактирования продукта
    @PostMapping("/{id}/edit")
    public String PostEdit(@PathVariable(value = "id") Long id,
                           @RequestParam String name,
                           @RequestParam String description,
                           @RequestParam Double price,
                           @RequestParam("file") MultipartFile file) {
        controllerService.editProduct(id, name, description, price, file);
        return "redirect:/";
    }

    //Фильтр
    @GetMapping("/main")
    public String filter(HttpServletRequest request,
                         @RequestParam (required = false, defaultValue = "") String filter,
                         Model model) {
        Iterable<Product> product;
        if (filter != null && !filter.isEmpty()) {
            product = productRepo.filter((List<Product>) productRepo.findAll(), filter);
        } else {
            product = productRepo.findAll();
        }
        model.addAttribute("filterView", filter);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("list", product);
        model.addAttribute("quantity", CartBean.get(request.getSession()).quantity());
        return "main";
    }

    //Страница не найдена или нет доступа
    @GetMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("msg", "Вы сломали интернет!");
        return "403";
    }
}
