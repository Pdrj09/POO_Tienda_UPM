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
public class Product extends Sellable {

    protected boolean personalizable;
    protected int maxPers;

    //this is the constructor that creates a product
    public Product(int id, String name, double price, Categories category) {
        super(id, name, price, category);
        this.category = category;
        this.personalizable = false;
    }
    //if it has maxpers we consider that the product can be personalized
    public Product(int id, String name, double price, Categories category, int maxPers ) {
        super(id, name, price, category);
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

    //It returns the value of id characters
    public boolean isPersonalizable(){
       return  this.personalizable;
    }


    //getters and setters

    public void setMaxPers(int maxPers) {
        this.maxPers = maxPers;
    }

    public int getMaxPers() {
        return maxPers;
    }

    public List<KV> getPresentableDetails() {
        return new ArrayList<>();
    }

    @Override
    public Product copy() {
        return new Product(id, name, price, category);
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
}