package com.furnistyle.shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.furnistyle.shop.model.FurnitureItem;
import com.furnistyle.shop.service.CatalogService;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService){
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<FurnitureItem> getAll(){
        return catalogService.getAllForClient();
    }

    @PostMapping("/{itemId}/changeStock")
    public void changeStock(@PathVariable int itemId, @RequestParam int newStock){
        catalogService.update(itemId, null, null, null, newStock, null);
    }

    @PostMapping("/{itemId}/changePrice")
    public void changePrice(@PathVariable int itemId, @RequestParam double newPrice){
        catalogService.update(itemId, null, null, newPrice, null, null);
    }

    @PostMapping("/{itemId}/setOnOrder")
    public void setOnOrder(@PathVariable int itemId){
        catalogService.setOnOrder(itemId);
    }

    @PostMapping("/{itemId}/setNotOnOrder")
    public void setNotOnOrder(@PathVariable int itemId){
        catalogService.setNotOnOrder(itemId);
    }

    @PostMapping("/addItem")
    public void addItem(@RequestParam String name, @RequestParam String category, @RequestParam Double price, @RequestParam Integer stockCount){
        catalogService.add(name, category, price, stockCount);
    }

    @PostMapping("/{itemId}/remove")
    public void remove(@PathVariable int itemId){
        catalogService.remove(itemId);
    }

    @PostMapping("/{itemId}/archive")
    public void archive(@PathVariable int itemId){
        catalogService.archive(itemId);
    }
}
