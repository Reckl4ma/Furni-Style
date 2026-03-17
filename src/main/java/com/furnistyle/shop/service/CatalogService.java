package com.furnistyle.shop.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.furnistyle.shop.model.FurnitureItem;
import com.furnistyle.shop.repository.CatalogRepository;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<FurnitureItem> getAll(){
        return catalogRepository.findAll();
    }

    public List<FurnitureItem> getAllForClient(){
        List<FurnitureItem> clientCatalog = new ArrayList<>();
        for (FurnitureItem item : catalogRepository.findAll()){
            if (item.isArchived() == false) {
                clientCatalog.add(item);
            }
        }
        return List.copyOf(clientCatalog);
    }

    public FurnitureItem getById(int id){
        return catalogRepository.findById(id);
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
        catalogRepository.addItem(name, category, price, stockCount);
    }

    public void update(int id, String name, String category, Double price, Integer stockCount, Boolean isOrder){
        FurnitureItem item = catalogRepository.findById(id);
        if (name != null && !item.getName().equals(name)) {
            catalogRepository.setName(id, name);
        }
        if (category != null && !item.getCategory().equals(category)) {
            catalogRepository.setCategory(id, category);
        }
        if (price != null && item.getPrice() != price) {
            catalogRepository.setPrice(id, price);
        }
        if (stockCount != null && item.getStockCount() != stockCount) {
            catalogRepository.setStockCount(id, stockCount);

            if (!item.isOrder()) {
                if (stockCount > 0) {
                    catalogRepository.setInStock(id);
                } else {
                    catalogRepository.setOutOfStock(id);
                }
            }
        }
        if (isOrder != null && item.isOrder() != isOrder) {
            if (isOrder) {
                catalogRepository.setOnOrder(id);
            } else {
                FurnitureItem updatedItem = catalogRepository.findById(id);

                if (updatedItem.getStockCount() > 0) {
                    catalogRepository.setInStock(id);
                } else {
                    catalogRepository.setOutOfStock(id);
                }
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
        catalogRepository.removeItem(id);
    }

    public void setOnOrder(int id){
        catalogRepository.setOnOrder(id);
    }

    public void setNotOnOrder(int id){
        catalogRepository.setOutOfStock(id);
    }
}
