package etsisi.upm.controllers;

import etsisi.upm.models.Product;

import java.util.HashSet;

public class ProductsController {
    private HashSet<Product> products;

    public ProductsController() {
        this.products = new HashSet<>();
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    private boolean addProduct (Product prod) {
        return products.add(prod);
    }

    //here we delete a product from the hashmap of products
    //return true if delete succeed, else false
    private boolean deleteProduct (Product prod) {
        return products.remove(prod);
    }

    //with this method we list all the products that we have in our catalog
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        System.out.println("CATALOG");
        for (Product prod : products) {
            builder.append("{");
            builder.append("class:Product");
            builder.append(", id:").append(prod.getId());
            builder.append(", name:'").append(prod.getName()).append("'");
            builder.append(", category:").append(prod.getName());
            builder.append(", price:").append(prod.getPrice());
            builder.append("}");
        }
        return builder.toString();
    }
}
