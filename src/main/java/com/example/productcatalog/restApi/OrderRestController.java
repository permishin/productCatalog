package com.example.productcatalog.restApi;


import com.example.productcatalog.entity.Orders;
import com.example.productcatalog.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/API/orders")
public class OrderRestController {

    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    //Получить все заказы
    @GetMapping
    public ResponseEntity<List<Orders>> findAll() {
        final List<Orders> orders = orderService.readAll();

        return orders != null &&  !orders.isEmpty()
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Получить заказ по ID
    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable(value = "id") Long Id) {
        final Orders order = orderService.findById(Id);
        return order != null
                ? new ResponseEntity(order, HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //Удалить заказ по ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = orderService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    //Добавить заказ
    @PostMapping(value = "/add")
    public ResponseEntity<?> create(@RequestBody Orders order) {
        orderService.create(order);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //Обновить заказ
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                    @RequestBody Orders order) {
        final boolean updated = orderService.update(order, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
