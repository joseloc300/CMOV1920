package com.example.terminal;

public class Item {

    private String id;
    private String name;
    private int euros;
    private int cents;
    private String info;

    public Item(String id, String name, int cents, String info) {
        this.id = id;
        this.name = name;
        this.euros = cents / 100;
        this.cents = cents % 100;
        this.info = info;
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

    public String getInfo() { return info; }
}
