package etsisi.upm.models;

import etsisi.upm.util.Categories;

class Food extends ServiceProduct {

    public Food(int id, String name, double price, Categories category) {
        super(id, name, price, category);
    }


    public String getName() {
        return "";
    }


    String getDescription() {
        return "";
    }
}
