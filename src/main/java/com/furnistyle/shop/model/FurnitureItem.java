package com.furnistyle.shop.model;

public class FurnitureItem {
    private int id;
    private String name;
    private String category;
    private double price;
    public enum Status{
        IN_STOCK,
        ON_ORDER,
        OUT_OF_STOCK
    }
    private Status status;
    private int stockCount;
    private boolean archived;

    public FurnitureItem(int id, String name, String category, double price, int stockCount){
        if (price <= 0) {
            throw new IllegalArgumentException("Цена товара не может быть меньше или равняться 0");
        }
        if (stockCount < 0) {
            throw new IllegalArgumentException("Количество товара не может быть меньше 0");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя товара неможет быть пустым");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Название категории неможет быть пустым");
        }
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        if (stockCount > 0) {
            status = Status.IN_STOCK;
        }
        else{
            status = Status.OUT_OF_STOCK;
        }
        this.stockCount = stockCount;
        this.archived = false;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getCategory(){
        return category;
    }

    public double getPrice(){
        return price;
    }

    public Status getStatus(){
        return status;
    }

    public boolean isOrder(){
        return status == Status.ON_ORDER;
    }

    public int getStockCount(){
        return stockCount;
    }

    public boolean isArchived(){
        return archived;
    }

    public void setName(String newName){
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Имя товара неможет быть пустым");
        }
        name = newName;
    }

    public void setCategory(String newCategory){
        if (newCategory == null || newCategory.isBlank()) {
            throw new IllegalArgumentException("Название категории неможет быть пустым");
        }
        category = newCategory;
    }

    public void setPrice(double newPrice){
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной или равняться 0");
        }
        price = newPrice;
    }

    public void setOnOrder(){
        status = Status.ON_ORDER;
        stockCount = 0;
    }

    public void setNotOnOrder(){
        status = Status.OUT_OF_STOCK;
        stockCount = 0;
    }

    public void setOnArchived(){
        archived = true;
    }

    public void setNotOnArchived(){
        archived = false;
    }

    public void setStockCount(int newStockCount){
        if (newStockCount < 0) {
            throw new IllegalArgumentException("Количество товара не может быть меньше нуля");
        }
        if (status == Status.ON_ORDER) {
            throw new IllegalArgumentException("Этот товар под заказ, у него не может быть количество товара");
        }
        if (newStockCount > 0) {
            status = Status.IN_STOCK;
        }
        else {
            status = Status.OUT_OF_STOCK;
        }
        stockCount = newStockCount;
    }
}