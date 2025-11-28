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

    private Map<Product,List<Object>> list;//Position 0->Cuantity 1-n->Personalizations
    /// Todo multimap
    private Map<Categories,Integer> categories;

    private static final int ZERO = 0;

    private static final String DISCOUNT = "**discount -";
    private static final String TOTAL_PRICE = "\nTotal price: ";
    private static final String TOTAL_DISCOUNT = "\nTotal discount: ";
    private static final String FINAL_PRICE = "\nFinal price: ";
    private static final String NEXT_LINE = "\n";
    private static final int MAX_SIZE = 100;
    private static final double EXTRA_PRICE_PERSONALIZATIONS = 0.1;
    private static final int MIN_FOR_DISCOUNT = 1;

    private static final String ERROR_MAXSIZE = "Ticket lleno, tamaño máximo de ";
    private static final String ERROR_NONPERSONALIZABLE = "You cant personalice a product that is not personalizable";
    private static final String ERROR_ZERO_AMOUNT = "The min amount to add is 1";


    public Ticket(String id){
        LocalDateTime now = LocalDateTime.now();
        String formatted = formatDate(now);
        this.id = formatted + "-" +id;
        this.list = new TreeMap<>();
        this.categories = new HashMap<>();
        this.state = TicketStates.EMPTY;

    }

    public Ticket(){
        this(String.format("%05d", new Random().nextInt(100_000)));
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
    public Ticket addProduct(Product prod, int amount, List<String> customizations)
    {
        if (countProducts() + amount > MAX_SIZE)  throw new IllegalStateException(ERROR_MAXSIZE + Ticket.MAX_SIZE);
        boolean isGoingToPersonalize = customizations != null;

        List<Object> entry = this.list.get(prod);

        if(entry!=null){//If it doesn't exist, amount has to be > 0
            if (amount == 0) throw new IllegalStateException(ERROR_ZERO_AMOUNT);
        }else {//If it exists but is not going to be personalized amount has to be > 0
            if (!isGoingToPersonalize && amount == 0)  throw new IllegalStateException(ERROR_ZERO_AMOUNT);
        }

        if(entry == null){
            entry = new ArrayList<>();
            entry.addFirst(amount);

            if (customizations != null) {
                entry.addAll(customizations);
            }

            this.list.put(prod,entry);
        }else{
            if (amount > 0) {
                int oldAmount = (int) entry.getFirst();
                entry.set(0, oldAmount + amount);
            }

            if (customizations != null) {
                entry.addAll(customizations);
            }
        }

        Categories category = prod.getCategory();
        this.categories.put(category, this.categories.getOrDefault(category, ZERO) + amount);
        this.state = TicketStates.ACTIVE;

        return this;
    }

    private int countProducts(){
        int total = ZERO;
        for(List<Object> list : this.list.values()){
            Integer quantity = (Integer) list.getFirst();
            total += quantity;
        }
        return total;
    }

    // Remove a product from the ticket
    public Ticket remove(Product prod){
        this.list.remove(prod);
        if(this.list.isEmpty()) this.state = TicketStates.EMPTY;
        return this;
    }

    public String close(){
        this.closeDate = LocalDateTime.now();
        String date = formatDate(this.closeDate);
        this.id+="-"+date;
        this.state = TicketStates.CLOSED;
        return this.getId();
    }

    // totalPrice updated with Meal&Meetings version
    private double totalPrice(){
        double sum=0;
        for(Map.Entry<Product,List<Object>> entry : list.entrySet()){
            double price = calculateProductPrice(entry);
            int amount = (int) entry.getValue().getFirst();

            sum+= price * amount;
        }
        return sum;
    }

    private double totalDiscount(){
        double sum=0;
        for(Map.Entry<Product,List<Object>> entry : list.entrySet()){
            Product product = entry.getKey();
            if (categories.get(product.getCategory()) > MIN_FOR_DISCOUNT){
                sum+=calculateProductPrice(entry) * product.getCategory().getDiscount();
            }
        }
        return sum;
    }

    private double calculateProductPrice(Map.Entry<Product,List<Object>> entry){
        Product product = entry.getKey();
        int amount = (int) entry.getValue().getFirst(); // number of amount(products)/people(services)
        double price = product.getPrice();
        int personalizations = entry.getValue().size()-1;

        double personalizationsExtra = price*EXTRA_PRICE_PERSONALIZATIONS*personalizations;
        price += personalizationsExtra;
        return price;
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Product, List<Object>> entry : list.entrySet()) {
            res.append(entry.getKey().toString());
            if (categories.get(entry.getKey().getCategory()) > MIN_FOR_DISCOUNT)
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
    
    public TicketStates getState() {
        return state;
    }
    //look if the product already exists
    public boolean containsProduct(Product prod) {
        return this.list.containsKey(prod);
    }

    public boolean isClosed(){
        return this.state == TicketStates.CLOSED;
    }
}
