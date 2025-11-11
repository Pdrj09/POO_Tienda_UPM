package etsisi.upm.models;

import etsisi.upm.util.Categories;
import etsisi.upm.util.TicketStates;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket {

    //Stores the list of products and their quantities in the current transaction
    private String id;
    private LocalDateTime closeDate;
    private TicketStates state;

    private Map<Product,Integer> list;
    private Map<Categories,Integer> categories;

    private static final int ZERO = 0;

    private static final String DISCOUNT = "**discount -";
    private static final String TOTAL_PRICE = "\nTotal price: ";
    private static final String TOTAL_DISCOUNT = "\nTotal discount: ";
    private static final String FINAL_PRICE = "\nFinal price: ";
    private static final String NEXT_LINE = "\n";
    private static final int MAX_SIZE = 100;


    public Ticket() {
        this(String.format("%05d", (int) (Math.random() * 100000)));
        //ESTO SOLO DEVUELVE UN NUMERO ALEATORIO COMO ID SIN LA FECHA; SIN TREEMAP, SIN HASHMAP Y SIN STATE
        //Por lo que este tichet una vez creado es inutil ya que no puedes llamar luego al otro constructor

    }


    public Ticket(String id){

        LocalDateTime now = LocalDateTime.now();
        String formatted = formatDate(now);
        this.id = formatted + "-" +id;
        this.list = new TreeMap<>();
        this.categories = new HashMap<>();
        this.state = TicketStates.EMPTY;

    }

    public static String formatDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        return date.format(formatter);
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
    //if ticket add (Product prod, int amount) -p <personalizacion>

    public void addPersonalized(Product prod, int amount, String [] personalized)
    {
        //if the product can be personalized
        if (prod.isPersonalizable()){
            if (amount == ZERO){
                return;
            }
            double total = ZERO;

            for(int value: this.list.values()){
                total = total + value + (0.1 * value);
             }
            StringBuilder personalizations = new StringBuilder();

            for (int i = 0; i < personalized.length; i++) {
                personalizations.append(personalized[i]);
                personalizations.append(" ");
            }
            //we see how many products are in list
            if(total + amount <= MAX_SIZE) {
                //TODO Hacer que un producto tenga las personalizaciones guardadas en el ticket
                this.list.put(prod, this.list.getOrDefault(prod, ZERO) + amount);
                Categories category = prod.getCategory();
                this.categories.put(category, this.categories.getOrDefault(category, ZERO) + amount);
                this.state=TicketStates.ACTIVE;
            }
        }else{
            throw new IllegalArgumentException("You cant personalice a product that is not personalizable");
        }

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
            this.list.put(prod, this.list.getOrDefault(prod, ZERO) + amount);
            Categories category = prod.getCategory();
            this.categories.put(category, this.categories.getOrDefault(category, ZERO) + amount);
            this.state=TicketStates.ACTIVE;
        }

    }

    // Remove a product from the ticket
    public void remove(Product prod){
        this.list.remove(prod);
        if(this.list.isEmpty()) this.state = TicketStates.EMPTY;
    }

    public String close(){
        this.closeDate = LocalDateTime.now();
        String date = formatDate(this.closeDate);
        this.id+="-"+date;
        return this.getId();
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

    public String getId() {
        return id;
    }
}
