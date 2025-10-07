package etsisi.upm;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    public List<Product> list = new ArrayList<>();

    /// The ticket goes empty despite the products it has.
    public void clear (){
        list.clear();
    }

    /// Add a created product to the Ticket with the amount. It goes to the end of the list.
    public void add(Product p, int amount){
        // First we have to search if there's an instance of the product (id) in the ticket.
        for(Product prod : list){       // (loop through each element of the current list...)
            if(prod.getId() == p.getId()){
                prod.setAmount(prod.getAmount() + amount);
                return;
            }
        }

        //if product is not found
        p.setAmount(amount);
        list.add(p);
    }

    public void remove(Product p){
        for(Product prod : list){       // (loop through each element of the current list...)
            if(prod.getId() == p.getId()){
                list.remove(p);
                return;
            }
        } //There will always be only one instance of a product.
    }

    public void print(){
        for ()
    }
    // ticket print (imprime factura)
    ///  Decirle a Pedro que para el add en ticket tiene que imprimir el producto y su descripcion = al amount.
}
