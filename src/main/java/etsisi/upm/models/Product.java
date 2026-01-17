package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Categories;
import etsisi.upm.util.Utilities;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("PRODUCT")
public class Product extends Sellable {
    protected boolean personalizable;
    protected int maxPers;
    
    @Column(name = "expiration_date")
    protected LocalDateTime expirationDate;
    
    @Column(name = "num_people")
    protected Integer numPeople;
    
    @Column(name = "min_creation_time")
    private Integer minimumCreationTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "min_time_unit")
    private ChronoUnit minimumTimeUnit;
    
    @Column(name = "product_type")
    private String productType;

    public Product(){
        super();
    }

    //this is the constructor that creates a product
    public Product(String id, String name, double price, Categories category) {
        super(id, name, price, category);
        this.category = category;
        this.personalizable = false;
        this.active = true;
    }
    //if it has maxpers we consider that the product can be personalized
    public Product(String id, String name, double price, Categories category, int maxPers ) {
        super(id, name, price, category);
        this.maxPers = maxPers;
        this.personalizable = true;
    }
    
    // Constructor for Food and Meeting
    private Product(String id, String name, double pricePerPerson, int maxPeople, 
                   LocalDateTime expirationDate, int minimumCreationTime, ChronoUnit minimumTimeUnit, String productType) {
        super(id, name, pricePerPerson, Categories.EMPTY);
        this.personalizable = false;
        this.active = true;
        this.numPeople = maxPeople;
        this.expirationDate = expirationDate;
        this.minimumCreationTime = minimumCreationTime;
        this.minimumTimeUnit = minimumTimeUnit;
        this.productType = productType;
        
        // Validation for people logic
        if (maxPeople <= Constants.SERVICE_PROD_MINPEOPLE)
            throw new IllegalArgumentException(Constants.ERROR_TOOMANY_PEOPLE);
        else if (maxPeople > Constants.TIME_MAX_PEOPLE_SERVICE)
            this.numPeople = Constants.TIME_MAX_PEOPLE_SERVICE;
        
        // Time validation
        if (!isFeasible(LocalDateTime.now())) {
            String timeUnit = getMinimumTimeUnit() == ChronoUnit.HOURS ? Constants.HOURS : Constants.DAYS;
            StringBuilder error = new StringBuilder();
            error.append(Constants.ERROR_SERVICE_DATE_FEASIBILITY);
            error.append(getMinimumCreationTime());
            error.append(Constants.STR_BLANK_SPACE);
            error.append(timeUnit);
            error.append(Constants.IN_THE_FUTURE);
            throw new IllegalArgumentException(String.valueOf(error));
        }
    }
    
    // Factory method for creating Meeting products
    public static Product createMeeting(String id, String name, double price, int people, LocalDateTime date) {
        return new Product(id, name, price, people, date, 
                          Constants.TIME_MEETING_PLANNING_HOURS, ChronoUnit.HOURS, Constants.STR_MEETING);
    }
    
    // Factory method for creating Food products
    public static Product createFood(String id, String name, double price, int people, LocalDateTime date) {
        return new Product(id, name, price, people, date, 
                          Constants.TIME_FOOD_PLANNING_DAYS, ChronoUnit.DAYS, Constants.STR_FOOD);
    }
    
    // Helper methods for Food and Meeting
    private boolean isFeasible(LocalDateTime creationTime) {
        if (expirationDate == null || minimumTimeUnit == null || minimumCreationTime == null) {
            return true; // Not a time-constrained product
        }
        long timeDifference = creationTime.until(expirationDate, minimumTimeUnit);
        return timeDifference >= minimumCreationTime;
    }
    
    private int getMinimumCreationTime() {
        return minimumCreationTime != null ? minimumCreationTime : 0;
    }
    
    private ChronoUnit getMinimumTimeUnit() {
        return minimumTimeUnit != null ? minimumTimeUnit : ChronoUnit.DAYS;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    
    public Integer getNumPeople() {
        return numPeople;
    }
    
    public boolean isFoodOrMeeting() {
        return productType != null && (productType.equals(Constants.STR_FOOD) || productType.equals(Constants.STR_MEETING));
    }
    // update certain characteristics of product
    public String update(String name, Categories category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.OPEN_BRACE);
        
        // Handle Food and Meeting types
        if (isFoodOrMeeting()) {
            builder.append(productType); // Will be "class:Food" or "class:Meeting"
            builder.append(Constants.STR_PROD_ID).append(id);
            builder.append(Constants.STR_PROD_NAME).append(name).append(Constants.QUOTE);
            builder.append(Constants.STR_CATEGORY).append(Categories.EMPTY);
            builder.append(Constants.STR_PRICE_PERSON).append(price);
            builder.append(Constants.STR_EXPIRATION).append(expirationDate);
            builder.append(Constants.STR_MAX_PEOPLE_ALLOWED).append(numPeople);
        } else {
            // Regular product
            builder.append(Constants.STR_PRODUCT);
            builder.append(Constants.STR_PROD_ID).append(id);
            builder.append(Constants.STR_PROD_NAME).append(name).append(Constants.QUOTE);
            builder.append(Constants.STR_CATEGORY).append(category);
            builder.append(Constants.STR_PRICE).append(price);
        }
        
        builder.append(Constants.CLOSE_BRACE);
        return builder.toString();
    }

    //It returns the value of id characters
    public boolean isPersonalizable(){
       return  this.personalizable;
    }


    //getters and setters
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMaxPers(int maxPers) {
        this.maxPers = maxPers;
    }

    public double getPrice() {
        return Utilities.round(price);
    }

    public int getMaxPers() {
        return maxPers;
    }

    public String getName(){
        return name;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public List<KV> getPresentableDetails() {
        return new ArrayList<>();
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID_Product", String.valueOf(this.id)));
        kvs.add(new KV("Name", this.name));
        
        // For Food and Meeting products
        if (isFoodOrMeeting()) {
            kvs.add(new KV("Max Persons", String.valueOf(this.numPeople)));
            kvs.add(new KV("Price ud.", String.valueOf(this.getPrice())));
            if (expirationDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = expirationDate.format(formatter);
                kvs.add(new KV("Date of Event", formattedDate));
            }
        } else {
            // For regular products
            kvs.add(new KV("Category", String.valueOf(this.category)));
            kvs.add(new KV("Price ud.", String.valueOf(this.getPrice())));
            if (this.isPersonalizable())
                kvs.add(new KV("Max Personalizations", String.valueOf(this.getMaxPers())));
        }
        
        return kvs;
    }
}