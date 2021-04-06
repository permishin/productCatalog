package com.example.productcatalog.service;

import com.example.productcatalog.entity.*;
import com.example.productcatalog.model.CartBean;
import com.example.productcatalog.repo.OrderRepo;
import com.example.productcatalog.repo.ProductListOrderRepo;
import com.example.productcatalog.repo.ProductRepo;
import com.example.productcatalog.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ControllerService {

    private final OrderRepo orderRepo;

    private final ProductListOrderRepo plo;

    private final S3Amazon s3Amazon;

    private final ProductRepo productRepo;

    private final UserRepo userRepo;

    private final ProductListOrderRepo productListOrderRepo;

    public ControllerService(OrderRepo orderRepo, ProductListOrderRepo plo, S3Amazon s3Amazon, ProductRepo productRepo, UserRepo userRepo, ProductListOrderRepo productListOrderRepo) {
        this.orderRepo = orderRepo;
        this.plo = plo;
        this.s3Amazon = s3Amazon;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.productListOrderRepo = productListOrderRepo;
    }

    //Метод сохранения нового заказа
    public void saveOrder(Orders orders,String email, String address, String comment, CartBean bean) {
        orders.setDate(new Date());
        orders.setEmail(email);
        orders.setAddress(address);
        orders.setComment(comment);
        orderRepo.save(orders);
        List<ProductListOrder> list = bean.saveSessionToProductListOrder(bean.getProd(), orders);
        for (ProductListOrder productListOrder : list) {
            plo.save(productListOrder);
        }
    }

    //Метод сохранения нового продукта
    public boolean saveProduct(Product product, String name, String description, Double price, MultipartFile file) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            product.setFileName(resultFileName);
            s3Amazon.uploadFile(resultFileName, file);
        } else {
            product.setFileName("404.jpg");
        }
        product.setCount(1);
        productRepo.save(product);
        return true;
    }

    //Удаление продукта
    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        try {
            if (!product.getFileName().equals("404.jpg")) {
                s3Amazon.deleteFile(product.getFileName());
            }
        } catch (IllegalArgumentException a) {
            a.getMessage();
        }
        productRepo.delete(product);
    }

    //Редактирование продукта
    public void editProduct(Long id, String name, String description, Double price, MultipartFile file) {
        Product product = productRepo.findById(id).orElseThrow(IllegalStateException::new);
        String resultFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            s3Amazon.uploadFile(resultFileName, file);
            if (!product.getFileName().equals("404.jpg")) {
                s3Amazon.deleteFile(product.getFileName());
            }
            product.setFileName(resultFileName);
        }
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productRepo.save(product);
    }

    //Редактор пользователя
    public void editUser(String username, Map<String, String> form, User user, String password) {
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
    }

    //Добавить товар в имеющийся заказ
    public void addProductToOrder(Long id, Long idProduct) {
        Orders order = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
        Product product = productRepo.findById(idProduct).orElseThrow(IllegalStateException::new);
        order.setProductListOrder(addProductToOrders(product,order));
        orderRepo.save(order);
    }

    //Удалить товар из заказа
    public void deleteProductFromOrder(Long id, Long idProduct) {
        Orders order = orderRepo.findById(id).orElseThrow(IllegalStateException::new);
        Product product = productRepo.findById(idProduct).orElseThrow(IllegalStateException::new);
        order.setProductListOrder(DeleteProductFromOrders(product,order));
        if (order.getProductListOrder().isEmpty()) {
            orderRepo.delete(order);
        } else {
            orderRepo.save(order);
        }
    }

    //Изменить количество продукта в заказе
    public void changeCount(Integer count, Long idProduct) {
        ProductListOrder product = productListOrderRepo.findById(idProduct).orElseThrow(IllegalStateException::new);
        product.setCount(count);
        product.setCostFinal(product.getPriceFinal() * product.getCount());
        productListOrderRepo.save(product);
    }

    public static List<ProductListOrder> addProductToOrders(Product product, Orders order) {
        boolean isNew = false;
        List<ProductListOrder> productList = order.getProductListOrder();
        for(ProductListOrder s : productList) {
            if (s.getProduct().getId().equals(product.getId())) {
                s.setCount(s.getCount() + 1);
                s.setCostFinal(s.getCount() * s.getPriceFinal());
                isNew = false;
                break;
            } else {
                isNew = true;
            }
        }
        if (isNew) {
            ProductListOrder productListOrder = new ProductListOrder();
            productListOrder.setProduct(product);
            productListOrder.setPriceFinal(product.getPrice());
            productListOrder.setCount(product.getCount());
            productListOrder.setCostFinal(product.getPrice() * product.getCount());
            productListOrder.setOrders(order);
            productList.add(productListOrder);
            return productList;
        }
        return productList;
    }

    //Удалить продукт из заказа
    public static List<ProductListOrder> DeleteProductFromOrders(Product product, Orders order) {
        List<ProductListOrder> productList = order.getProductListOrder();
        for (int i = 0; i < productList.size(); i ++) {
            if (productList.get(i).getProduct().getId().equals(product.getId())) {
                productList.remove(productList.get(i));
            }
        }
        return productList;
    }
}
