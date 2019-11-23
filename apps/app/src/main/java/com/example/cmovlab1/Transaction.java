package com.example.cmovlab1;

import java.util.ArrayList;

public class Transaction {

    private int totalEuro;
    private int totalCents;
    private String items;
    private String usedVoucher;
    private String usedDiscount;

    public Transaction(String price, String items, String usedVoucher, String usedDiscount) {
        this.items = items;
        if(usedVoucher.equals("null"))
            this.usedVoucher = "None";
        else
            this.usedVoucher = usedVoucher;

        int priceInt = Integer.parseInt(price);
        this.totalEuro = priceInt / 100;
        this.totalCents = priceInt % 100;
        this.usedDiscount = usedDiscount;

    }

    public int getTotalEuro() {
        return totalEuro;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public String getItems() {
        return items;
    }

    public String getUsedVoucher() {
        return usedVoucher;
    }

    public String getUsedDiscount() {
        return usedDiscount;
    }
}
