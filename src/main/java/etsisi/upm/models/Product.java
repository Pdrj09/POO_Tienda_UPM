package etsisi.upm.models;


import java.util.Objects;

//We asign the variables
public class Product {
    private int id;
    private String name;
    private double price;
    private Categories category;

    private static final String OPEN_BRACE = "{";
    private static final String STR_PRODUCT = "class:Product";
    private static final String STR_NAME = ", name:'";
    private static final String STR_ID = ", id:";
    private static final String STR_CATEGORY = ", category:";
    private static final String STR_PRICE = ", price:";
    private static final String CLOSE_BRACE = "}";



    //this is the constructor that creates a product
    public Product(int id, String name, double price, Categories category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
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
        builder.append(STR_NAME).append(name).append("'");
        builder.append(STR_CATEGORY).append(category);
        builder.append(STR_PRICE).append(price);
        builder.append(CLOSE_BRACE);
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
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
