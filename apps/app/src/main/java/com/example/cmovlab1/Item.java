package com.example.cmovlab1;

public class Item {

    private String id;
    private String name;
    private int euros;
    private int cents;

    public Item(String id, String name, int cents) {
        this.id = id;
        this.name = name;
        this.euros = cents / 100;
        this.cents = cents % 100;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCents() {
        return cents;
    }

    public int getEuros() {
        return euros;
    }
}
