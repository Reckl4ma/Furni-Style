package com.furnistyle.shop.model;

public class OrderLine {
    private FurnitureItem item;
    private int quantity;
    private int itemIdSnapshot;
    private String itemNameSnapshot;
    private String itemCategorySnapshot;
    private double unitPriceSnapshot;
    private FurnitureItem.Status itemStatusSnapshot;

    public OrderLine(FurnitureItem item, int quantity){
        if (item == null) {
            throw new IllegalArgumentException("Не выбран товар");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество товара не может быть меньше или равняться 0");
        }
        this.item = item;
        this.quantity = quantity;
        this.unitPriceSnapshot = item.getPrice();
        this.itemIdSnapshot = item.getId();
        this.itemNameSnapshot = item.getName();
        this.itemCategorySnapshot = item.getCategory();
        this.itemStatusSnapshot = item.getStatus();
    }

    public FurnitureItem getItem(){
        return item;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getItemIdSnapshot(){
        return itemIdSnapshot;
    }

    public String getItemNameSnapshot(){
        return itemNameSnapshot;
    }

    public String getItemCategorySnapshot(){
        return itemCategorySnapshot;
    }

    public double getUnitPriceSnapshot(){
        return unitPriceSnapshot;
    }

    public FurnitureItem.Status getItemStatusSnapshot(){
        return itemStatusSnapshot;
    }

    public double getLineTotal(){
        return unitPriceSnapshot * quantity;
    }

    public void changeQuantity(int count){
        if ((count * -1) >= quantity) {
            throw new IllegalArgumentException("Количество товара не может быть меньше или равняться 0");
        }
        quantity += count;
    }
}