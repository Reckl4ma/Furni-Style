package com.furnistyle.shop.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.furnistyle.shop.model.Order;
import com.furnistyle.shop.model.OrderStatus;
import com.furnistyle.shop.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestParam String name, @RequestParam String phone){
        return orderService.createOrder(name, phone);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable int orderId){
        return orderService.getOrder(orderId);
    }

    @PostMapping("/{orderId}/addItem")
    public void addItem(@PathVariable int orderId, @RequestParam int itemId, @RequestParam int quantity){
        orderService.addItem(orderId, itemId, quantity);
    }

    @PostMapping("/{orderId}/changeQuantity")
    public void postMethodName(@PathVariable int orderId, @RequestParam int itemId, @RequestParam int count) {
        orderService.changeQuantity(orderId, itemId, count);
    }
    
    @PostMapping("/{orderId}/removeLine")
    public void removeLine(@PathVariable int orderId, @RequestParam int itemId){
        orderService.removeLine(orderId, itemId);
    }

    @PostMapping("/{orderId}/changeStatus")
    public void changeStatus(@PathVariable int orderId, @RequestParam OrderStatus newStatus){
        orderService.changeStatus(orderId, newStatus);
    }
    
    @GetMapping("/getAllOrders")
    public Map<Integer, Order> getAllOrders(){
        return orderService.getAllOrders();
    }
}
