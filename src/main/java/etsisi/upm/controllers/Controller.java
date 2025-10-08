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

    public Product updateProduct(int id, String field, String newContent){
        if (this.products.get(id)==null) return null;
        switch (field){
            case "NAME":
                this.products.get(id).setName(newContent);
                break;
            case "CATEGORY":
                if(Categories.existCategory(newContent)){
                    Categories cat = Categories.valueOf(newContent);
                    this.products.get(id).setCategory(cat);
                }else return null;
                break;
            case "PRICE":
                this.products.get(id).setPrice(Double.parseDouble(newContent));
                break;
            default:
                return null;
        }
        return this.products.get(id);
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

   public boolean ticketNew(){
        return this.ticket.clear();
   }


    public String ticketPrint(){
        return this.ticket.toString();
    }

    public String prodList(){
        return this.products.toString();
    }
}
