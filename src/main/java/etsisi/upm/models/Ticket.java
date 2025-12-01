package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Categories;
import etsisi.upm.util.TicketStates;
import etsisi.upm.util.Utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket implements Presentable {

    //Stores the list of products and their quantities in the current transaction
    private String id;
    private LocalDateTime closeDate;
    private TicketStates state;

    private final Map<Product,Integer> list;
    private final Map<Categories,Integer> categories;


    public Ticket(String id){
        LocalDateTime now = LocalDateTime.now();
        String formatted = Utilities.formatDate(now);
        this.id = formatted + Constants.HYPEN +id;
        this.list = new TreeMap<>();
        this.categories = new HashMap<>();
        this.state = TicketStates.EMPTY;

    }

    public Ticket(){
        this(String.format(Constants.ID_FORMAT, new Random().nextInt(Constants.MAX_RANDOM)));
    }

    public Ticket addProduct(Product prod, int amount) {
        if (countProducts() + amount > Constants.MAX_SIZE_TICKET)  throw new IllegalStateException(Constants.ERROR_MAXSIZE_TICKET + Constants.MAX_SIZE_TICKET);

        if (amount < Constants.MIN_AMMOUNT) throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);

        if (this.list.containsKey(prod)) {
            int currentAmount = this.list.get(prod);
            this.list.put(prod, currentAmount + amount);
        }else
            this.list.put(prod, amount);

        Categories category = prod.getCategory();
        this.categories.put(category, this.categories.getOrDefault(category, Constants.BASE_AMOUNT_OF_CATEGORY) + amount);
        this.state = TicketStates.ACTIVE;

        return this;
    }

    private int countProducts(){
        int total = Constants.BASE_AMOUNT_OF_PRODUCT;
        for(Integer amount : this.list.values()){
            total += amount;
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
        String date = Utilities.formatDate(this.closeDate);
        this.id+=Constants.HYPEN+date;
        this.state = TicketStates.CLOSED;
        return this.getId();
    }

    private double totalPrice(){
        double sum=Constants.BASE_PRICE;
        for(Map.Entry<Product, Integer> entry : list.entrySet()){
            double price = calculateProductPrice(entry.getKey());
            int amount = entry.getValue();

            sum += price * amount;
        }
        return Utilities.round(sum);
    }

    private double totalDiscount(){
        double sum=Constants.BASE_DISCOUNT;
        for(Map.Entry<Product, Integer> entry : list.entrySet()){
            Product product = entry.getKey();
            int amount = entry.getValue();

            if (categories.get(product.getCategory()) > Constants.MIN_FOR_DISCOUNT){
                double productBasePrice = calculateProductPrice(product) * amount;
                sum += productBasePrice * product.getCategory().getDiscount();
            }
        }
        return Utilities.round(sum);
    }

    public double calculateProductPrice(Product product){
        double price = product.getPrice();

        if (product instanceof ProductPersonalized){
            price += price * Constants.EXTRA_PRICE_PERSONALIZATIONS * ((ProductPersonalized) product).getCustomizationsAmount();
        }

        return price;
    }

    public double getTotalDiscountForProduct(Product prod) {
        Integer amount = this.list.get(prod);
        double discountPerUnit = getDiscountPerUnit(prod);
        double rawTotalDiscount = discountPerUnit * amount;
        return Utilities.round(rawTotalDiscount);
    }

    public double getDiscountPerUnit(Product prod) {
        if (categories.get(prod.getCategory()) > Constants.MIN_FOR_DISCOUNT){
            double priceToUseForDiscount = this.calculateProductPrice(prod);
            double discount = priceToUseForDiscount * prod.getCategory().getDiscount();
            return Utilities.round(discount);
        }
        return Constants.BASE_DISCOUNT;
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : list.entrySet()) {
            res.append(entry.getKey().toString());
            if (categories.get(entry.getKey().getCategory()) > Constants.MIN_FOR_DISCOUNT) {
                double individualDiscount = entry.getKey().getPrice() * entry.getKey().getCategory().getDiscount();
                res.append(Constants.DISCOUNT).append(Utilities.round(individualDiscount));
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


    public double getTotalPriceView() {
        return Utilities.round(totalPrice());
    }

    public double getTotalDiscountView() {
        return Utilities.round(totalDiscount());
    }

    public double getFinalPriceView() {
        return Utilities.round(getTotalPriceView() - getTotalDiscountView());
    }

    public Map<Product, Integer> getList() {
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

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID_Ticket", getId()));
        kvs.add(new KV("Total price", String.valueOf(getTotalPriceView())));
        kvs.add(new KV("Total discount", String.valueOf(getTotalDiscountView())));
        kvs.add(new KV("Final price", String.valueOf(getFinalPriceView())));
        return kvs;
    }
    //Returns only id and state, it is used for cash tickets and ticket list commands
    public List<KV> toViewKVListSummary() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID", getId()));
        kvs.add(new KV("State", getState().name()));
        return kvs;
    }

    //for personalized products
    public List<KV> getDetailedKVsForProductLine(Product p, int quantity) {
        List<KV> prodKV = new ArrayList<>(p.toViewKVList());
        if (p instanceof ProductPersonalized) {
            double calculatedPrice = this.calculateProductPrice(p);
            prodKV.removeIf(kv -> kv.key.equals("Price W/Pers"));
            prodKV.add(new KV("Price W/Pers", String.valueOf(calculatedPrice)));
        }
        prodKV.addAll(p.getPresentableDetails());
        prodKV.add(new KV("Sum Price", String.valueOf(calculateProductPrice(p)*quantity)));
        prodKV.add(new KV("Quantity", String.valueOf(quantity)));
        prodKV.add(new KV("Discount/Unit", "-" + String.valueOf(this.getDiscountPerUnit(p))));
        prodKV.add(new KV("Total Prod. Discount", "-" + String.valueOf(this.getTotalDiscountForProduct(p))));

        return prodKV;
    }
}
