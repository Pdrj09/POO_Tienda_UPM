package etsisi.upm.controllers;

import etsisi.upm.models.Categories;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import java.util.HashMap;
import java.util.HashSet;

public class Controller {
    private HashMap<Integer,Product> products;
    private Ticket ticket;

    private static final String ERROR_CREATTE_PRODUCT = "Error al crear el producto";

    private static final String CATALOG = "Catalog:\n";
    private static final String NEXT_LINE = "\n";
    private static final String TAB_SPACE = "\t";

    public Controller() {
        this.products = new HashMap<>();
        this.ticket = new Ticket();
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    public String addProduct (String name, String category, double price,int id) {
        Product product;
        if(Categories.existCategory(category)){
            product=new Product(id,name,price,Categories.valueOf(category));
            products.put(product.getId(), product);
            return product.toString();
        }else return ERROR_CREATTE_PRODUCT;
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
        StringBuilder builder = new StringBuilder();

        builder.append(CATALOG);

        for( Product p : products.values()){
            builder.append(TAB_SPACE)
                    .append(p.toString())
                    .append(NEXT_LINE);
        }

        return builder.toString();
    }
}
