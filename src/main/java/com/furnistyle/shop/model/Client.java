package com.furnistyle.shop.model;

public class Client {
    private int id;
    private String name;
    private String phone;

    public Client(int id, String name, String phone){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        if (phone == null || !phone.matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException("Телефон клиента не соответствует формату");
        }
        
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public void setName(String newName){
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        name = newName;
    }

    public void setPhone(String newPhone){
        if (newPhone == null || !newPhone.matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException("Телефон клиента не соответствует формату");
        }
        phone = newPhone;
    }
}
