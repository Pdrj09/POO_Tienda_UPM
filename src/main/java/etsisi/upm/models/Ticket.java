package etsisi.upm.models;

import java.util.HashMap;
import java.util.Iterator;

public class Ticket {
    private HashMap<Product,Integer> list = new HashMap<>();
    private HashMap<String,Integer> categories = new HashMap<>(); //Cambiar por categorias UwU

    // The ticket goes empty despite the products it has.
    public void clear (){
        list.clear();
    }

    // Add a product to the ticket, if the product already exists increments its amount
    public void add(Product p, int amount){
        this.list.put(p,this.list.containsKey(p) ? this.list.get(p)+amount : amount );
        Categories category = p.getCategory();
        this.categories.put(category,this.categories.containsKey(category) ? this.categories.get(category)+1:1);
    }

    // Remove a product from the ticket
    public void remove(Product p){
        this.list.remove(p);
    }

    private double totaPrice(){
        return list.entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    private double totalDiscount(){
        return list.entrySet()
                .stream()
                .mapToDouble()
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        return res.toString();
    }

    // ticket print (imprime factura)
    ///  Decirle a Pedro que para el add en ticket tiene que imprimir el producto y su descripcion = al amount.
}
