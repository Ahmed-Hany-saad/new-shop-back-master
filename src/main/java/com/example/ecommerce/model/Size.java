package com.example.ecommerce.model;

import jakarta.persistence.Embeddable;
@Embeddable
public class Size {
    private String name;
    private int quantity;

    public Size(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Size() {

    }

    public String getSizeName() {
        return name;
    }
    public void setSizeName(String name) {
        this.name = name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
