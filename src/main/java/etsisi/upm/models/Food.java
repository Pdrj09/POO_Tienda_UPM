package etsisi.upm.models;

import etsisi.upm.Constants;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Food extends ServiceProduct {

    private static final String STR_MEAL = "class:Meal";
    private static final String CLOSE_BRACE = "}";

    public Food(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        super(id, name, pricePerPerson, maxPeople, expirationDate);
    }

    // Date rules
    @Override
    public int getMinimumCreationTime() {
        return Constants.TIME_FOOD_PLANNING_DAYS;
    }

    @Override
    public ChronoUnit getMinimumTimeUnit() {
        return ChronoUnit.DAYS;
    }

    // cost
    @Override
    public double calculateTotalCost(int participants) {
        return getPricePerPerson() * participants;
    }

    // --- toString() ---
    @Override
    public String toString() {
        String parentToString = super.toString();
        // replace the name of the class
        return parentToString.replace("class:ServiceProduct", STR_MEAL);
    }
}