package etsisi.upm.models;

import java.util.HashMap;
import java.util.Iterator;

public class Ticket {
    private HashMap<Product,Integer> list;
    private HashMap<Categories,Integer> categories;

    public Ticket(){
        this.list = new HashMap<>();
        this.categories = new HashMap<>();
    }

    // The ticket goes empty despite the products it has.
    public void clear (){
        list.clear();
    }

    // Add a product to the ticket, if the product already exists increments its amount
    public void add(Product prod, int amount){
        this.list.put(prod,this.list.containsKey(prod) ? this.list.get(prod)+amount : amount );
        Categories category = prod.getCategory();
        this.categories.put(category,this.categories.containsKey(category) ? this.categories.get(category)+1:1);
    }

    // Remove a product from the ticket
    public void remove(Product prod){
        this.list.remove(prod);
    }

    private double totaPrice(){
        return list.entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    private double totalDiscount(){
        return list.keySet()
                .stream()
                .mapToDouble(
                        product -> categories.get(product.getCategory()) > 1 ?
                                    product.getPrice() * product.getCategory().getDiscount() : 0)
                .sum();
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        return res.toString();
    }

    // ticket print (imprime factura)
    ///  Decirle a Pedro que para el add en ticket tiene que imprimir el producto y su descripcion = al amount.
}
