package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Categories;
import etsisi.upm.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//We asign the variables
public class Product implements Comparable<Product>, Presentable {
    protected final int id; // It is a global variable as the id cant change once the object is created
    protected String name;
    protected double price;
    protected Categories category;
    protected boolean personalizable;
    protected int maxPers;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.OPEN_BRACE);
        builder.append(Constants.STR_PRODUCT);
        builder.append(Constants.STR_PROD_ID).append(id);
        builder.append(Constants.STR_PROD_NAME).append(name).append(Constants.QUOTE);
        builder.append(Constants.STR_CATEGORY).append(category);
        builder.append(Constants.STR_PRICE).append(price);
        builder.append(Constants.CLOSE_BRACE);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o.getClass() != Product.class)//critic error comprobation
            return false;
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
        return Utilities.round(price);
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

    public List<KV> getPresentableDetails() {
        return new ArrayList<>();
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID_Product", String.valueOf(this.id)));
        kvs.add(new KV("Name", this.name));
        kvs.add(new KV("Category", String.valueOf(this.category)));
        kvs.add(new KV("Price ud.", String.valueOf(this.getPrice())));
        if (this.isPersonalizable())
            kvs.add(new KV("Max Personalizations", String.valueOf(this.getMaxPers())));
        return kvs;
    }
    //It's used to compare alphabetically this name and the other products name (it is case-insensitive)
    //returns value < 0 if this name comes before other name alphabetically
    //        value = 0 if its equal this name and other name
    //        value > 0 if this name comes after other name alphabetically

    @Override
    public int compareTo(Product other) {
        //compare name
        int comparison = this.name.compareToIgnoreCase(other.name);
        if (comparison != 0)
            return comparison;
        //if the names are the same, we compare the name of the class
        if (!this.getClass().equals(other.getClass()))
            return this.getClass().getName().compareTo(other.getClass().getName());
        //if not, compare the id
        return Integer.compare(this.id, other.id);
    }
}