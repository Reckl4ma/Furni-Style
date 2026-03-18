package com.furnistyle.shop.service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.furnistyle.shop.model.FurnitureItem;
import com.furnistyle.shop.model.Client;
import com.furnistyle.shop.model.Order;
import com.furnistyle.shop.model.OrderLine;
import com.furnistyle.shop.model.OrderStatus;
import com.furnistyle.shop.repository.CatalogRepository;

@Service
public class OrderService {
    private final CatalogRepository catalogRepository;
    private final CatalogService catalogService;
    private Map<Integer, Order> orders = new HashMap<>();
    private int nextOrderId = 1;
    private int nextClientId = 1;
    
    public OrderService(CatalogService catalogService, CatalogRepository catalogRepository){
        this.catalogService = catalogService;
        this.catalogRepository = catalogRepository;
    }

    public Order createOrder(Client client){
        int id = nextOrderId++;
        Order order = new Order(id, client);
        orders.put(id, order);
        return order;
    }

    public Order createOrder(String name, String phone){
        int id = nextClientId++;
        Client client = new Client(id, name, phone);
        return createOrder(client);
    }

    public Order getOrder(int orderId){
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Заказ с id=" + orderId + " не найден");
        }
        return order;
    }

    public void addItem(int orderId, FurnitureItem item, int quantity){
        if (item == null) {
            throw new IllegalArgumentException("Поля товара пусто");
        }
        if (item.getStatus() == FurnitureItem.Status.OUT_OF_STOCK) {
            throw new IllegalArgumentException("Товара нету в наличии");
        }
        if (item.isArchived()) {
            throw new IllegalArgumentException("Товара снят с продажи");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество товара не может быть меньше нуля");
        }
        if (quantity > item.getStockCount() && item.getStatus() != FurnitureItem.Status.ON_ORDER) {
            throw new IllegalArgumentException("Количество товара на складе меньше чем требуется");
        }
        Order order = getOrder(orderId);
        OrderLine orderLine = new OrderLine(item, quantity);
        order.addLine(orderLine);
    }

    public void addItem(int orderId, int itemId, int quantity){
        FurnitureItem item = catalogService.getById(itemId);
        addItem(orderId, item, quantity);
    }

    public void removeLine(int orderId, OrderLine line){
        Order order = getOrder(orderId);
        order.removeLine(line);
    }

    public void removeLine(int orderId, int itemId){
        Order order = getOrder(orderId);
        
        OrderLine target = null;

        for (OrderLine line : order.getLines()) {
            if (line.getItem().getId() == itemId) {
                target = line;
                break;
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Позиция не найдена в заказе");
        }

        order.getLines().remove(target);
    }

    public void changeStatus(int orderId, OrderStatus newStatus){
        if (newStatus == null) {
            throw new IllegalArgumentException("Статус не задан");
        }

        Order order = getOrder(orderId);

        if (!order.getStatus().canChangeTo(newStatus)) {
            throw new IllegalArgumentException("Переход на новый статус невозможен");
        }

        if (newStatus == OrderStatus.CONFIRMED) {
            List<OrderLine> lines = order.getLines();

            for (OrderLine line : lines){
                if (line.getItem().getStatus() == FurnitureItem.Status.OUT_OF_STOCK) {
                    throw new IllegalArgumentException(
                        "Ошибка, товар " + line.getItem().getName() + " в заказе уже полностью распродан"
                    );
                }

                if (line.getItem().getStatus() == FurnitureItem.Status.IN_STOCK
                        && line.getItem().getStockCount() < line.getQuantity()) {
                    throw new IllegalArgumentException(
                        "Ошибка, требуемое количество товара " + line.getItem().getName() + " в заказе уже распродано"
                    );
                }
            }

            for (OrderLine line : lines){
                if (line.getItem().getStatus() == FurnitureItem.Status.IN_STOCK) {
                    int newStockCount = line.getItem().getStockCount() - line.getQuantity();

                    catalogRepository.changeStock(line.getItem().getId(), newStockCount);
                    line.getItem().setStockCount(newStockCount);
                }
            }
        }

        if (newStatus == OrderStatus.CANCELLED
                && (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.IN_PROGRESS)) {

            List<OrderLine> lines = order.getLines();

            for (OrderLine line : lines){
                FurnitureItem currentItem = catalogRepository.findById(line.getItem().getId());

                if (currentItem.getStatus() != FurnitureItem.Status.ON_ORDER) {
                    int newStockCount = currentItem.getStockCount() + line.getQuantity();

                    catalogRepository.changeStock(currentItem.getId(), newStockCount);

                    currentItem.setStockCount(newStockCount);
                    line.getItem().setStockCount(newStockCount);
                }
            }
        }

        order.changeStatus(newStatus);
    }

    public List<Order> getActiveOrders(){
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : orders.values()){
            if (order.getStatus() == OrderStatus.CREATED || order.getStatus() == OrderStatus.IN_PROGRESS || order.getStatus() == OrderStatus.CONFIRMED) {
                activeOrders.add(order);
            }
        }
        return activeOrders;
    }

    public List<Order> getAwaitingOrders(){
        List<Order> awaitingOrders = new ArrayList<>();
        for (Order order : orders.values()){
            if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.CREATED) {
                awaitingOrders.add(order);
            }
        }
        return awaitingOrders;
    }

    public List<Order> getFinishedOrders(){
        List<Order> finishedOrders = new ArrayList<>();
        for (Order order : orders.values()){
            if (order.getStatus() == OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELLED) {
                finishedOrders.add(order);
            }
        }
        return finishedOrders;
    }

    public Map<Integer, Order> getAllOrders(){
        return orders;
    }

    public void changeQuantity(int orderId, int itemId, int count){
        Order order = getOrder(orderId);

        OrderLine target = null;

        for (OrderLine line : order.getLines()) {
            if (line.getItem().getId() == itemId) {
                target = line;
                break;
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Позиция не найдена в заказе");
        }

        target.changeQuantity(count);
    }
}