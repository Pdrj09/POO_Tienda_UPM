package etsisi.upm.models;

import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Utilities;
import etsisi.upm.util.Categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Sellable implements Comparable<Sellable>, Presentable {
    protected final int id; // It is a global variable as the id cant change once the object is created
    protected String name;
    protected double price;
    protected Categories category;

    public abstract Sellable copy();

    public Sellable(int id, String name, double price, Categories category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return Utilities.round(price);
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sellable sellable = (Sellable) o;
        return id == sellable.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID_Product", String.valueOf(this.id)));
        kvs.add(new KV("Name", this.name));
        kvs.add(new KV("Price ud.", String.valueOf(this.getPrice())));
        return kvs;
    }

    //It's used to compare alphabetically this name and the other products name (it is case-insensitive)
    //returns value < 0 if this name comes before other name alphabetically
    //        value = 0 if its equal this name and other name
    //        value > 0 if this name comes after other name alphabetically
    @Override
    public int compareTo(Sellable other) {
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
