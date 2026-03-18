package com.furnistyle.shop.repository;

import com.furnistyle.shop.model.FurnitureItem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CatalogRepository {
    private static final String URL = "jdbc:sqlite:data/furnistyle.db";

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL);
    }

    public List<FurnitureItem> findAll(){
        String sql = "SELECT * FROM FurnitureItems";
        List<FurnitureItem> items = new ArrayList<>();

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int stockCount = resultSet.getInt("stockCount");
                String status = resultSet.getString("status");
                boolean archived = resultSet.getBoolean("archived");

                FurnitureItem item = new FurnitureItem(id, name, category, price, stockCount);

                if ("ON_ORDER".equals(status)){
                    item.setOnOrder();
                } else if ("OUT_OF_STOCK".equals(status)){
                    item.setOutOfStock();
                }

                if (archived){
                    item.setOnArchived();
                }

                items.add(item);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения каталога", e);
        }

        return items;
    }

    public FurnitureItem findById(int myId){
        String sql = "SELECT * FROM FurnitureItems WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, myId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    double price = resultSet.getDouble("price");
                    int stockCount = resultSet.getInt("stockCount");
                    String status = resultSet.getString("status");
                    boolean archived = resultSet.getBoolean("archived");

                    FurnitureItem item = new FurnitureItem(id, name, category, price, stockCount);

                    if ("ON_ORDER".equals(status)) {
                        item.setOnOrder();
                    } else if ("OUT_OF_STOCK".equals(status)) {
                        item.setOutOfStock();
                    }

                    if (archived) {
                        item.setOnArchived();
                    }

                    return item;
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения каталога", e);
        }

        throw new IllegalArgumentException("Не удалось найти товара по номеру");
    }

    public void addItem(String name, String category, Double price, Integer stockCount){
        String sql = "INSERT INTO FurnitureItems(name, category, price, stockCount, status, archived) VALUES (?, ?, ?, ?, ?, 0)";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, name);
            statement.setString(2, category);
            statement.setDouble(3, price);
            statement.setInt(4, stockCount);
            
            if (stockCount > 0) {
                statement.setString(5, "IN_STOCK");
            }
            else{
                statement.setString(5, "OUT_OF_STOCK");
            }

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления товара", e);
        }
    }

    public void removeItem(int myId){
        String sql = "DELETE FROM FurnitureItems WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, myId);

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления товара", e);
        }
    }

    public void setName(int id, String name){
        String sql = "UPDATE FurnitureItems SET name = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновление товара", e);
        }
    }

    public void setCategory(int id, String category){
        String sql = "UPDATE FurnitureItems SET category = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, category);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновление товара", e);
        }
    }

    public void setPrice(int id, double price){
        String sql = "UPDATE FurnitureItems SET price = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setDouble(1, price);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновление товара", e);
        }
    }

    public void setInStock(int id){
        String sql = "UPDATE FurnitureItems SET status = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, "IN_STOCK");
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления товара", e);
        }
    }

    public void setStockCount(int id, int stockCount){
        String sql = "UPDATE FurnitureItems SET stockCount = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, stockCount);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновление товара", e);
        }
    }

    public void setOnOrder(int id){
        String sql = "UPDATE FurnitureItems SET status = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, "ON_ORDER");
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновление товара", e);
        }
    }

    public void setOutOfStock(int id){
        String sql = "UPDATE FurnitureItems SET status = ? WHERE id = ?";

        try(
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, "OUT_OF_STOCK");
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка обновление товара", e);
        }
    }

    public void changeStock(int id, int newStockCount) {
        String status;

        if (newStockCount > 0) {
            status = "IN_STOCK";
        } else {
            status = "OUT_OF_STOCK";
        }

        String sql = "UPDATE FurnitureItems SET stockCount = ?, status = ? WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, newStockCount);
            statement.setString(2, status);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка изменения остатка товара", e);
        }
    }

}
