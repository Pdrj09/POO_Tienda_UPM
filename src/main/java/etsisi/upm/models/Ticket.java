package etsisi.upm.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Ticket {
    private HashMap<Product,Integer> list;
    private HashMap<Categories,Integer> categories;

    private static final String DISCOUNT = "**discount -";
    private static final String TOTAL_PRICE = "\nTotal price: ";
    private static final String TOTAL_DISCOUNT = "\nTotal discount: ";
    private static final String FINAL_PRICE = "\nFinal price: ";
    private static final String NEXT_LINE = "\n";


    public Ticket(){
        this.list = new HashMap<>();
        this.categories = new HashMap<>();
    }

    // The ticket goes empty despite the products it has.
    public boolean clear (){
        list.clear();
        categories.clear();
        if (list.isEmpty() && categories.isEmpty())
            return true;
        return false;
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

    private double totalPrice(){
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
        for (Map.Entry<Product, Integer> entry : list.entrySet()){
            res.append(entry.getKey().toString());
            if (categories.get(entry.getKey().getCategory()) > 1)
                res.append(DISCOUNT).append(entry.getKey().getPrice() * entry.getKey().getCategory().getDiscount());
        }
        res.append(TOTAL_PRICE).append(totalPrice());
        res.append(TOTAL_DISCOUNT).append(totalDiscount());
        res.append(FINAL_PRICE).append(totalPrice() - totalDiscount());
        res.append(NEXT_LINE);
        return res.toString();
    }

}
