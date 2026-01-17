package etsisi.upm.models;

import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;
import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Categories;
import etsisi.upm.util.TicketStates;
import etsisi.upm.util.Utilities;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tickets")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ticket <P extends Sellable> implements Presentable {

    @Id
    @Column(name = "id")
    protected String id;

    protected LocalDateTime closeDate;

    @Enumerated(EnumType.STRING)
    protected TicketStates state;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    protected Set<TicketItem> items;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_categories", joinColumns = @JoinColumn(name = "ticket_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "quantity")
    @MapKeyColumn(name = "category")
    protected Map<Categories,Integer> categories;


    public Ticket(String id){
        LocalDateTime now = LocalDateTime.now();
        String formatted = Utilities.formatDate(now);
        this.id = formatted + Constants.HYPEN +id;
        this.items = new HashSet<>();
        this.categories = new HashMap<>();
        this.state = TicketStates.EMPTY;

    }

    public Ticket(){
        this(String.format(Constants.ID_FORMAT, new Random().nextInt(Constants.MAX_RANDOM)));
    }

    // Metodos auxiliares para trabajar con items como si fuera un Map
    protected Map<P, Integer> getListAsMap() {
        Map<P, Integer> map = new TreeMap<>(Comparator.comparing(Sellable::getId));
        for (TicketItem item : items) {
            map.put((P) item.getSellable(), item.getQuantity());
        }
        return map;
    }

    protected TicketItem findItem(Sellable sellable) {
        for (TicketItem item : items) {
            if (item.getSellable().equals(sellable)) {
                return item;
            }
        }
        return null;
    }

    protected void putItem(P sellable, int quantity) {
        TicketItem existing = findItem(sellable);
        if (existing != null) {
            existing.setQuantity(quantity);
        } else {
            items.add(new TicketItem(sellable, quantity));
        }
    }

    protected void addToItem(P sellable, int amount) {
        TicketItem existing = findItem(sellable);
        if (existing != null) {
            existing.addQuantity(amount);
        } else {
            items.add(new TicketItem(sellable, amount));
        }
    }

    protected Integer getItemQuantity(Sellable sellable) {
        TicketItem item = findItem(sellable);
        return item != null ? item.getQuantity() : null;
    }

    protected boolean containsItem(Sellable sellable) {
        return findItem(sellable) != null;
    }

    protected void removeItem(Sellable sellable) {
        items.removeIf(item -> item.getSellable().equals(sellable));
    }

    public abstract Ticket<P> addProduct(Sellable prod, int amount);

    protected int countProducts(){
        int total = Constants.BASE_AMOUNT_OF_PRODUCT;
        for(TicketItem item : this.items){
            total += item.getQuantity();
        }
        return total;
    }

    // Remove a product from the ticket
    public Ticket<P> remove(Sellable prod){
        removeItem(prod);
        if(this.items.isEmpty()) this.state = TicketStates.EMPTY;
        return this;
    }

    public abstract String close();

    protected double totalPrice(){
        double sum=Constants.BASE_PRICE;
        for(TicketItem item : items){
            double price = calculateProductPrice(item.getSellable());
            int amount = item.getQuantity();

            sum += price * amount;
        }
        return Utilities.round(sum);
    }

    protected double totalDiscount(){
        double sum=Constants.BASE_DISCOUNT;
        for(TicketItem item : items){
            Sellable product = item.getSellable();
            int amount = item.getQuantity();

            if (categories.get(product.getCategory()) > Constants.MIN_FOR_DISCOUNT){
                double productBasePrice = calculateProductPrice(product) * amount;
                sum += productBasePrice * product.getCategory().getDiscount();
            }
        }
        return Utilities.round(sum);
    }

    public double calculateProductPrice(Sellable product){
        double price = product.getPrice();

        if (product instanceof ProductPersonalized){
            price += price * Constants.EXTRA_PRICE_PERSONALIZATIONS * ((ProductPersonalized) product).getCustomizationsAmount();
        }

        return price;
    }

    public double getTotalDiscountForProduct(Sellable prod) {
        Integer amount = getItemQuantity(prod);
        double discountPerUnit = getDiscountPerUnit(prod);
        double rawTotalDiscount = discountPerUnit * amount;
        return Utilities.round(rawTotalDiscount);
    }

    public double getDiscountPerUnit(Sellable prod) {
        if (categories.get(prod.getCategory()) > Constants.MIN_FOR_DISCOUNT){
            double priceToUseForDiscount = this.calculateProductPrice(prod);
            double discount = priceToUseForDiscount * prod.getCategory().getDiscount();
            return Utilities.round(discount);
        }
        return Constants.BASE_DISCOUNT;
    }


    // ARCHIVO: etsisi.upm.models.Ticket.java

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (TicketItem item : items) {
            Sellable product = item.getSellable();
            int amount = item.getQuantity();
            boolean hasDiscount = categories.getOrDefault(product.getCategory(), 0) > Constants.MIN_FOR_DISCOUNT;
            double discountPerUnit = 0;
            if (hasDiscount)
                discountPerUnit = getDiscountPerUnit(product);

            int effectiveAmount = amount;
            if (product instanceof ServiceProduct)
                effectiveAmount = 1;
            for (int i = 0; i < effectiveAmount; i++) {
                if (!(product instanceof ServiceProduct) || i == 0) {
                    res.append(product.toString());
                    if (hasDiscount)
                        res.append(Constants.DISCOUNT).append(String.format("%.2f", -discountPerUnit));
                    res.append(Constants.ENTER_KEY);
                }
            }
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

    public Map<P, Integer> getList() {
        return Collections.unmodifiableMap(getListAsMap());
    }

    public Set<TicketItem> getItems() {
        return items;
    }

    public void setItems(Set<TicketItem> items) {
        this.items = items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public void setState(TicketStates state) {
        this.state = state;
    }

    public Map<Categories, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<Categories, Integer> categories) {
        this.categories = categories;
    }

    //look if the product already exists
    public boolean containsProduct(Sellable prod) {
        return containsItem(prod);
    }

    public boolean isClosed(){
        return this.state == TicketStates.CLOSED;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
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
    public List<KV> getDetailedKVsForProductLine(Sellable p, int quantity) {

        List<KV> prodKV = new ArrayList<>(p.toViewKVList());
        if (p instanceof ProductPersonalized) {
            double calculatedPrice = this.calculateProductPrice(p);
            prodKV.removeIf(kv -> kv.key.equals("Price ud."));
            prodKV.add(new KV("Price ud.", String.valueOf(calculatedPrice)));
        }
        prodKV.addAll(p.getPresentableDetails());
        prodKV.add(new KV("Sum Price", String.valueOf(calculateProductPrice(p)*quantity)));
        prodKV.add(new KV("Quantity", String.valueOf(quantity)));
        prodKV.add(new KV("Discount/Unit", "-" + String.valueOf(this.getDiscountPerUnit(p))));
        prodKV.add(new KV("Total Prod. Discount", "-" + String.valueOf(this.getTotalDiscountForProduct(p))));

        return prodKV;
    }
}
