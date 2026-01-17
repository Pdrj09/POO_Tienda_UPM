package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.util.Categories;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

//We asign the variables
public class Product extends Sellable {

    protected boolean personalizable;
    protected int maxPers;

    //this is the constructor that creates a product
    public Product(int id, String name, double price, Categories category) {
        super(id, name, price, category);
        this.category = category;
        this.personalizable = false;
    }
    //if it has maxpers we consider that the product can be personalized
    public Product(int id, String name, double price, Categories category, int maxPers ) {
        super(id, name, price, category);
        this.category = category;
        this.personalizable = false;
        this.maxPers = maxPers;
        this.personalizable = true;
    }
    // update certain characteristics of product
    public int update(String name, Categories category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.OPEN_BRACE);
        builder.append(Constants.STR_PRODUCT);
        builder.append(Constants.STR_PROD_ID).append(id);
        builder.append(Constants.STR_PROD_NAME).append(name).append(Constants.QUOTE);
        builder.append(Constants.STR_CATEGORY).append(category);
        builder.append(Constants.STR_PRICE).append(price);
        builder.append(Constants.CLOSE_BRACE);
        return builder.toString();
    }

    //It returns the value of id characters
    public boolean isPersonalizable(){
       return  this.personalizable;
    }


    //getters and setters
    public int getMaxPers() {
        return maxPers;
    }


    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV("ID_Product", String.valueOf(this.id)));
        kvs.add(new KV("Name", this.name));
        kvs.add(new KV("Category", String.valueOf(this.category)));
        kvs.add(new KV("Price ud.", String.valueOf(this.getPrice())));
        if (this.isPersonalizable())
            kvs.add(new KV("Max Personalizations", String.valueOf(this.getMaxPers())));
        return kvs;
    }

    // Factory methods for Food and Meeting products
    public static Product createFood(int id, String name, double price, int maxPeople, LocalDateTime expirationDate) {
        // Validate time feasibility (3 days in the future)
        validateFeasibility(expirationDate, Constants.TIME_FOOD_PLANNING_DAYS, ChronoUnit.DAYS, Constants.DAYS);
        
        // Validate people count
        validatePeopleCount(maxPeople);
        
        // Create a regular product with EMPTY category (no category for food/meeting)
        return new Product(id, name, price, Categories.EMPTY);
    }

    public static Product createMeeting(int id, String name, double price, int maxPeople, LocalDateTime expirationDate) {
        // Validate time feasibility (12 hours in the future)
        validateFeasibility(expirationDate, Constants.TIME_MEETING_PLANNING_HOURS, ChronoUnit.HOURS, Constants.HOURS);
        
        // Validate people count
        validatePeopleCount(maxPeople);
        
        // Create a regular product with EMPTY category (no category for food/meeting)
        return new Product(id, name, price, Categories.EMPTY);
    }

    private static void validateFeasibility(LocalDateTime expirationDate, int minimumTime, ChronoUnit timeUnit, String timeUnitStr) {
        LocalDateTime now = LocalDateTime.now();
        long timeDifference = now.until(expirationDate, timeUnit);
        
        if (timeDifference < minimumTime) {
            StringBuilder error = new StringBuilder();
            error.append(Constants.ERROR_SERVICE_DATE_FEASIBILITY);
            error.append(minimumTime);
            error.append(Constants.STR_BLANK_SPACE);
            error.append(timeUnitStr);
            error.append(Constants.IN_THE_FUTURE);
            throw new IllegalArgumentException(error.toString());
        }
    }

    private static void validatePeopleCount(int maxPeople) {
        if (maxPeople <= Constants.SERVICE_PROD_MINPEOPLE) {
            throw new IllegalArgumentException(Constants.ERROR_TOOMANY_PEOPLE);
        }
        // Note: We don't enforce the max limit here since we're not storing numPeople
    }
}