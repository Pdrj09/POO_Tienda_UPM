package etsisi.upm.controllers;

import etsisi.upm.models.Categories;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import java.util.HashMap;
import java.util.HashSet;

public class Controller {
    private HashMap<Integer,Product> products;
    private Ticket ticket;

    public Controller() {
        this.products = new HashMap<>();
        this.ticket = new Ticket();
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    public boolean addProduct (String name, String category, double price,int id) {
        Product product;
        if(Categories.existCategory(category)){
            product=new Product(id,name,price,Categories.valueOf(category));
            products.put(product.getId(), product);
            return true;
        }else return false;
    }

    //here we delete a product from the hashmap of products
    //return true if delete succeed, else false
    public Product deleteProduct (int prodId) {
        if(products.containsKey(prodId)){
            ticket.remove(products.get(prodId));
            return  products.remove(prodId);
        }else return null;
    }

   public boolean addProductToTicket(int prodId, int amount){
        if(products.containsKey(prodId)){
            ticket.add(products.get(prodId),amount);
            return true;
        }else return false;
   }

   public boolean removeProductFromTicket(int prodId){
       if(products.containsKey(prodId)){
           ticket.remove(products.get(prodId));
           return true;
       }else return false;
   }


    //with this method we list all the products that we have in our catalog
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        System.out.println("CATALOG");
        for (Product prod : products.values()) {
            builder.append("{");
            builder.append("class:Product");
            builder.append(", id:").append(prod.getId());
            builder.append(", name:'").append(prod.getCategory()).append("'");
            builder.append(", category:").append(prod.getName());
            builder.append(", price:").append(prod.getPrice());
            builder.append("}");
        }
        return builder.toString();
    }
}
