package com.example.productcatalog.restApi;


import com.example.productcatalog.entity.Product;
import com.example.productcatalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    //Получить список продуктов
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        final List<Product> products = productService.readAll();

        return products != null &&  !products.isEmpty()
                ? new ResponseEntity<>(products, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //Получить продукт по ID
    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable(value = "id") Long Id) {
        final Product product = productService.findById(Id);
        return product != null
                ? new ResponseEntity(product, HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    //Удалить продукт по ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = productService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    //Добавить продукт
    @PostMapping(value = "/")
    public ResponseEntity<?> create(@RequestBody Product product) {
        productService.create(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //Обновить продукт
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                    @RequestBody Product product) {
        final boolean updated = productService.update(product, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
