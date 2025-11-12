package etsisi.upm.models;

import etsisi.upm.util.Categories;

public abstract class ServiceProduct extends Product {
    protected String expirationDate;
    protected static int maxPeople = 100;
    protected int numPeople;

    public ServiceProduct(int id, String name, double price, Categories category) {
        super(id, name, price, category);
    }

    public double calculatePrice(int amount, double price){
        return amount*price;
    }

    abstract String getDescription();
}
