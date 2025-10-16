package etsisi.upm.models;

import java.util.*;

public class Ticket {

    //Stores the list of products and their quantities in the current transaction
    private Map<Product,Integer> list;
    private Map<Categories,Integer> categories;

    private static final int ZERO = 0;

    private static final String DISCOUNT = "**discount -";
    private static final String TOTAL_PRICE = "\nTotal price: ";
    private static final String TOTAL_DISCOUNT = "\nTotal discount: ";
    private static final String FINAL_PRICE = "\nFinal price: ";
    private static final String NEXT_LINE = "\n";
    private static final int MAX_SIZE = 100;


    public Ticket(){
        this.list = new TreeMap<>();
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
        if (amount == ZERO){
            return;
        }
        int total = ZERO;
        for(int value: this.list.values()){
            total = total + value;
        }
        //we see how many products are in list
        if(total + amount <= MAX_SIZE) {
            this.list.put(prod, this.list.containsKey(prod) ? this.list.get(prod) + amount : amount);
            Categories category = prod.getCategory();
            this.categories.put(category, this.categories.containsKey(category) ? this.categories.get(category) + amount : amount);
        }

    }

    // Remove a product from the ticket
    public void remove(Product prod){
        this.categories.put(prod.getCategory(),this.categories.get(prod.getCategory())-list.get(prod));
        this.list.remove(prod);
    }

    private double totalPrice(){
        double sum=0;
        for(Map.Entry<Product,Integer> entry : list.entrySet()){
            sum+=entry.getKey().getPrice() * entry.getValue();
        }
        return sum;
    }

    private double totalDiscount(){
        double sum=0;
        for (Product product : list.keySet()){
            if (categories.get(product.getCategory()) >1){
                sum+=product.getPrice() * product.getCategory().getDiscount();
            }
        }
        return sum;
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : list.entrySet()) {
            res.append(entry.getKey().toString());
            if (categories.get(entry.getKey().getCategory()) > 1)
                res.append(DISCOUNT).append(entry.getKey().getPrice() * entry.getKey().getCategory().getDiscount());
            res.append(NEXT_LINE);
        }
        res.append(TOTAL_PRICE).append(totalPrice());
        res.append(TOTAL_DISCOUNT).append(totalDiscount());
        res.append(FINAL_PRICE).append(totalPrice() - totalDiscount());
        res.append(NEXT_LINE);
        return res.toString();
    }

}
