package etsisi.upm.models;

import etsisi.upm.util.Categories;

class Meeting extends ServiceProduct {
    public Meeting(int id, String name, double price, Categories category) {
        super(id, name, price, category);
    }


    public String getName() {
        return "";
    }

    String getDescription() {
        return "";
    }
}
