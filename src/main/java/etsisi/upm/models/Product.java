package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.util.Categories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

//We asign the variables
public class Product implements Comparable<Product>, Viewable{
    private final int id; // It is a global variable as the id cant change once the object is created
    private String name;
    private double price;
    private Categories category;
    private boolean personalizable;
    private int maxPers;
    public static final int maxPeople = 100;

//Variables to avoid magic numbers
    protected static final String OPEN_BRACE = "{";
    private static final String STR_PRODUCT = "class:Product";
    protected static final String STR_NAME = ", name:'";
    protected static final String STR_ID = ", id:";
    protected static final String STR_CATEGORY = ", category:";
    protected static final String SINGLE_QUOTE = "'";
    protected static final String STR_PRICE = ", price:";
    protected static final String CLOSE_BRACE = "}";



    //this is the constructor that creates a product
    public Product(int id, String name, double price, Categories category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.personalizable = false;
    }
    //if it has maxpers we consider that the product can be personalized
    public Product(int id, String name, double price, Categories category, int maxPers ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.personalizable = false;
        this.maxPers = maxPers;
        this.personalizable = true;
    }
    // update certain characteristics of product
    public int update(String name, Categories category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        return id;
    }


    //toString method
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(OPEN_BRACE);
        builder.append(STR_PRODUCT);
        builder.append(STR_ID).append(id);
        builder.append(STR_NAME).append(name).append(SINGLE_QUOTE);
        builder.append(STR_CATEGORY).append(category);
        builder.append(STR_PRICE).append(price);
        builder.append(CLOSE_BRACE);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) { //equals function to see if we are refering to the same object
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    //It returns the value of id characters


    public boolean isPersonalizable(){
       return  this.personalizable;
    }
    //getters and setters
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMaxPers(int maxPers) {
        this.maxPers = maxPers;
    }

    public double getPrice() {
        return price;
    }

    public int getMaxPers() {
        return maxPers;
    }

    public String getName(){
        return name;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    //Its used to compare alfabeticaly this name and the other products name (it is case insensitive)
    //returns value < 0 if this name comes before other name alfabetically
    //        value = 0 if its equal this name and other name
    //        value > 0 if this name comes after other name alfabetically

    @Override
    public int compareTo(Product other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}
