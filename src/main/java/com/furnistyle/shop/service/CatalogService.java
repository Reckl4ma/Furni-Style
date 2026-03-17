package com.furnistyle.shop.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.furnistyle.shop.model.FurnitureItem;

@Service
public class CatalogService {
    private List<FurnitureItem> catalog = new ArrayList<>();

    public CatalogService() {
        catalog.add(new FurnitureItem(1, "Диван Лофт", "Диваны", 49990, 3));
        catalog.add(new FurnitureItem(2, "Диван Милан", "Диваны", 55990, 2));
        catalog.add(new FurnitureItem(3, "Угловой диван Сканди", "Диваны", 68990, 1));

        catalog.add(new FurnitureItem(4, "Стол Орион", "Столы", 15990, 5));
        catalog.add(new FurnitureItem(5, "Стол Бруно", "Столы", 18990, 4));
        catalog.add(new FurnitureItem(6, "Журнальный столик Рио", "Столы", 7990, 6));

        catalog.add(new FurnitureItem(7, "Шкаф Норд", "Шкафы", 25990, 0));
        catalog.add(new FurnitureItem(8, "Шкаф Классик", "Шкафы", 13990, 0));
        catalog.add(new FurnitureItem(9, "Шкаф Купе Лайт", "Шкафы", 32990, 2));

        catalog.add(new FurnitureItem(10, "Кровать Астра", "Кровати", 29990, 2));
        catalog.add(new FurnitureItem(11, "Кровать Лион", "Кровати", 34990, 1));

        catalog.add(new FurnitureItem(12, "Комод Вега", "Комоды", 11990, 4));
        catalog.add(new FurnitureItem(13, "Комод Сити", "Комоды", 9990, 3));

        catalog.add(new FurnitureItem(14, "Тумба ТВ Нео", "Тумбы", 8990, 5));
        catalog.add(new FurnitureItem(15, "Тумба ТВ Ретро", "Тумбы", 12990, 2));

        catalog.add(new FurnitureItem(16, "Стул Сканди", "Стулья", 3990, 10));
    }

    public List<FurnitureItem> getAll(){
        return List.copyOf(catalog);
    }

    public List<FurnitureItem> getAllForClient(){
        List<FurnitureItem> clientCatalog = new ArrayList<>();
        for (FurnitureItem item : catalog){
            if (item.isArchived() == false) {
                clientCatalog.add(item);
            }
        }
        return List.copyOf(clientCatalog);
    }

    public FurnitureItem getById(int id){
        for (FurnitureItem item : catalog){
            if (item.getId() == id) {
                return item;
            }
        }
        throw new IllegalArgumentException("Не удалось найти товара по номеру");
    }

    public void add(FurnitureItem item){
        if (item == null) {
            throw new IllegalArgumentException("Не передано поле товара");
        }
        for (FurnitureItem oldItem : catalog){
            if (oldItem.getId() == item.getId()) {
                throw new IllegalArgumentException("Такой товар уже существует");
            }
        }
        catalog.add(item);
    }

    public void add(String name, String category, Double price, Integer stockCount){
        if (name == null) {
            throw new IllegalArgumentException("Не передано название товара");
        }
        if (category == null) {
            throw new IllegalArgumentException("Не передано категория товара");
        }
        if (price == null) {
            throw new IllegalArgumentException("Не передано цена товара");
        }
        if (stockCount == null) {
            throw new IllegalArgumentException("Не передано количество товара");
        }
        if (catalog == null || catalog.size() == 0) {
            FurnitureItem item = new FurnitureItem(1, name, category, price, stockCount);
            catalog.add(item);
            return;
        }
        int currentId = 1;

        while(true){
            boolean exists = false;
            for (FurnitureItem oldItem : catalog){
                if (oldItem.getId() == currentId) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                break;
            }

            currentId++;
        }
        FurnitureItem item = new FurnitureItem(currentId, name, category, price, stockCount);
        catalog.add(item);
    }

    public void update(int id, String name, String category, Double price, Integer stockCount, Boolean isOrder){
        FurnitureItem item = getById(id);
        if (name != null && !item.getName().equals(name)) {
            item.setName(name);
        }
        if (category != null && !item.getCategory().equals(category)) {
            item.setCategory(category);
        }
        if (price != null && item.getPrice() != price) {
            item.setPrice(price);
        }
        if (stockCount != null && item.getStockCount() != stockCount) {
            item.setStockCount(stockCount);
        }
        if (isOrder != null && item.isOrder() != isOrder) {
            if (item.isOrder() == true && isOrder == false) {
                item.setNotOnOrder();
            }
            if (item.isOrder() == false && isOrder == true) {
                item.setOnOrder();
            }
        }
    }

    public void archive(int id){
        FurnitureItem item = getById(id);
        item.setOnArchived();
    }

    public void restore(int id){
        FurnitureItem item = getById(id);
        item.setNotOnArchived();
    }

    public void remove(int id){
        FurnitureItem item = getById(id);
        catalog.remove(item);
    }

    public void setOnOrder(int id){
        FurnitureItem item = getById(id);
        item.setOnOrder();
    }

    public void setNotOnOrder(int id){
        FurnitureItem item = getById(id);
        item.setNotOnOrder();
    }
}
