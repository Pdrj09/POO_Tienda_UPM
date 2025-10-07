package etsisi.upm.models;


import java.util.Objects;

//We asign the variables
public class Product {
    private int id;
    private String name;
    private float price;
    private Categories category;
    private static int nextId = 1;


    //this is the constructor that creates a product
    public Product(String name, float price, Categories category) {
        this.id = nextId++;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // update certain characteristics of product
    public int update(String name, Categories category, float price) {
        this.name = name;
        this.category = category;
        this.price = price;
        return id;
    }


    //toString method
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("class:Product");
        builder.append(", id:").append(id);
        builder.append(", name:'").append(name).append("'");
        builder.append(", category:").append(category);
        builder.append(", price:").append(price);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //getters and setters
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
