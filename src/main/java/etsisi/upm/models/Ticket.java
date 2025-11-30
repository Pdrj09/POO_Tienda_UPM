package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Categories;
import etsisi.upm.util.TicketStates;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket implements Presentable {

    //Stores the list of products and their quantities in the current transaction
    private String id;
    private LocalDateTime closeDate;
    private TicketStates state;

    private Map<Product,List<Object>> list;//Position 0->Cuantity 1-n->Personalizations
    /// Todo multimap
    private Map<Categories,Integer> categories;


    public Ticket(String id){
        LocalDateTime now = LocalDateTime.now();
        String formatted = formatDate(now);
        this.id = formatted + Constants.HYPEN +id;
        this.list = new TreeMap<>();
        this.categories = new HashMap<>();
        this.state = TicketStates.EMPTY;

    }

    public Ticket(){
        this(String.format(Constants.ID_FORMAT, new Random().nextInt(Constants.MAX_RANDOM)));
    }

    public static String formatDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_WH);
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
        if (countProducts() + amount > Constants.MAX_SIZE_TICKET)  throw new IllegalStateException(Constants.ERROR_MAXSIZE_TICKET + Constants.MAX_SIZE_TICKET);
        boolean isGoingToPersonalize = customizations != null;

        List<Object> entry = this.list.get(prod);

        if(entry!=null){//If it doesn't exist, amount has to be > 0
            if (amount == Constants.ZERO) throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);
        }else {//If it exists but is not going to be personalized amount has to be > 0
            if (!isGoingToPersonalize && amount == Constants.ZERO)  throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);
        }

        if(entry == null){
            entry = new ArrayList<>();
            entry.addFirst(amount);

            if (customizations != null) {
                entry.addAll(customizations);
            }

            this.list.put(prod,entry);
        }else{
            if (amount > Constants.ZERO) {
                int oldAmount = (int) entry.getFirst();
                entry.set(Constants.ZERO, oldAmount + amount);
            }

            if (customizations != null) {
                entry.addAll(customizations);
            }
        }

        Categories category = prod.getCategory();
        this.categories.put(category, this.categories.getOrDefault(category, Constants.ZERO) + amount);
        this.state = TicketStates.ACTIVE;

        return this;
    }

    private int countProducts(){
        int total = Constants.ZERO;
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
        this.id+=Constants.HYPEN+date;
        this.state = TicketStates.CLOSED;
        return this.getId();
    }

    // totalPrice updated with Meal&Meetings version
    private double totalPrice(){
        double sum=Constants.ZERO;
        for(Map.Entry<Product,List<Object>> entry : list.entrySet()){
            double price = calculateProductPrice(entry);
            int amount = (int) entry.getValue().getFirst();

            sum+= price * amount;
        }
        return round(sum);
    }

    private double totalDiscount(){
        double sum=Constants.ZERO;
        for(Map.Entry<Product,List<Object>> entry : list.entrySet()){
            Product product = entry.getKey();
            int amount = (int) entry.getValue().getFirst();
            if (categories.get(product.getCategory()) > Constants.MIN_FOR_DISCOUNT){
                double productBasePrice = product.getPrice() * amount;
                sum+= productBasePrice * product.getCategory().getDiscount();
            }
        }
        return round(sum);
    }

    private double calculateProductPrice(Map.Entry<Product,List<Object>> entry){
        Product product = entry.getKey();
        int amount = (int) entry.getValue().getFirst(); // number of amount(products)/people(services)
        double price = product.getPrice();
        int personalizations = entry.getValue().size()-Constants.ONE;

        double personalizationsExtra = price*Constants.EXTRA_PRICE_PERSONALIZATIONS*personalizations;
        price += personalizationsExtra;
        return price;
    }

    private double round(double value) {
        long factor = (long) Math.pow(10, 2); //2 decimals
        value *= factor;
        long tmp = Math.round(value);
        return (double) tmp/factor;
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Product, List<Object>> entry : list.entrySet()) {
            res.append(entry.getKey().toString());
            if (categories.get(entry.getKey().getCategory()) > Constants.MIN_FOR_DISCOUNT) {
                double individualDiscount = entry.getKey().getPrice() * entry.getKey().getCategory().getDiscount();
                res.append(Constants.DISCOUNT).append(round(individualDiscount));
            }
            res.append(Constants.ENTER_KEY);
        }
        res.append(Constants.TOTAL_PRICE).append(getTotalPriceView());
        res.append(Constants.TOTAL_DISCOUNT).append(getTotalDiscountView());
        res.append(Constants.FINAL_PRICE).append(getFinalPriceView());
        res.append(Constants.ENTER_KEY);
        return res.toString();
    }

    //getters for the view (there are used by a method in view, DO NOT TOUCH)
    public String getId() {
        return id;
    }
    
    public TicketStates getState() {
        return state;
    }
    public String getCloseDateFormatted() {
        if (closeDate == null)
            return Constants.HYPEN;
        return closeDate.format(DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT));
    }

    public double getTotalPriceView() {
        return round(totalPrice());
    }

    public double getTotalDiscountView() {
        return round(totalDiscount());
    }

    public double getFinalPriceView() {
        return round(getTotalPriceView() - getTotalDiscountView());
    }

    public Map<Product, List<Object>> getList() {
        //Return an unmodifiableMap to prevent external modifications
        return Collections.unmodifiableMap(list);
    }

    //look if the product already exists
    public boolean containsProduct(Product prod) {
        return this.list.containsKey(prod);
    }

    public boolean isClosed(){
        return this.state == TicketStates.CLOSED;
    }

    public double getDiscountPerUnit(Product prod) {
        if (this.categories.getOrDefault(prod.getCategory(), 0) > Constants.MIN_FOR_DISCOUNT) {
            double discount = prod.getPrice() * prod.getCategory().getDiscount();
            return round(discount);
        }
        return Constants.ZERO;
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID", getId()));
        kvs.add(new KV("State", getState().name()));
        kvs.add(new KV("Close Date", getCloseDateFormatted()));
        kvs.add(new KV("Total", String.valueOf(getTotalPriceView())));
        kvs.add(new KV("Discount", String.valueOf(getTotalDiscountView())));
        kvs.add(new KV("Final", String.valueOf(getFinalPriceView())));
        return kvs;
    }
}
