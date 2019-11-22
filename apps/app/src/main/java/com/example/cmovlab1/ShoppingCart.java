package com.example.cmovlab1;

import java.util.ArrayList;

public class ShoppingCart {
    private static ArrayList<Item> cart = new ArrayList<>();

    public int itemExists(String id) {
        int ret = -1;

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId().equals(id)) {
                ret = i;
                break;
            }
        }

        return ret;
    }

    public Boolean addItem(Item i) {
        cart.add(i);
        return true;
    }

    public Boolean removeItem(int index) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
            return true;
        }
        else
            return false;
    }

}
