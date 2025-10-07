package etsisi.upm.models;

import java.util.HashMap;
import java.util.Iterator;

public class Ticket {
    public HashMap<Product,Integer> list = new HashMap<>();

    // The ticket goes empty despite the products it has.
    public void clear (){
        list.clear();
    }

    // Add a product to the ticket, if the product already exists increments its amount
    public void add(Product p, int amount){
        this.list.put(p,this.list.containsKey(p) ? this.list.get(p)+amount : amount );
    }

    // Remove a product from the ticket
    public void remove(Product p){
        this.list.remove(p);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Iterator<HashMap<Product,Integer>> ite = new Iterator<HashMap<Product, Integer>>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public HashMap<Product, Integer> next() {
                return null;
            }
        }
        return res.toString();
    }

    // ticket print (imprime factura)
    ///  Decirle a Pedro que para el add en ticket tiene que imprimir el producto y su descripcion = al amount.
}
