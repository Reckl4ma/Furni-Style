package com.furnistyle.shop.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Order {
    private int id;
    private Client client;
    private List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order(int id, Client client){
        if (client == null) {
            throw new IllegalArgumentException("Клиент не задан");
        }
        this.id = id;
        this.client = client;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public int getId(){
        return id;
    }

    public Client getClient(){
        return client;
    }

    public List<OrderLine> getLines(){
        return lines;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void addLine(OrderLine newLine){
        if (newLine == null){
            throw new IllegalArgumentException("Позиция заказа не задана");
        }
        if (status == OrderStatus.DONE || status == OrderStatus.CANCELLED){
            throw new IllegalArgumentException("Нельзя изменять завершённый или отменённый заказ");
        }
        lines.add(newLine);
    }

    public void removeLine(OrderLine myLine){
        if (myLine == null){
            throw new IllegalArgumentException("Позиция заказа не задана");
        }
        if (status == OrderStatus.DONE || status == OrderStatus.CANCELLED){
            throw new IllegalArgumentException("Нельзя изменять завершённый заказ");
        }
        lines.remove(myLine);
    }

    public double getTotal(){
        double total = 0;
        for (OrderLine line : lines){
            if (line.getItemStatusSnapshot() != FurnitureItem.Status.ON_ORDER){
                total += line.getLineTotal();
            }
        }
        return total;
    }

    public void changeStatus(OrderStatus newStatus){
        if (newStatus == null) {
            throw new IllegalArgumentException("Поля статуса пуст");
        }
        if (!status.canChangeTo(newStatus)){
            throw new IllegalArgumentException("Нельзя поменять статус");
        }
        status = newStatus;
    }
}
