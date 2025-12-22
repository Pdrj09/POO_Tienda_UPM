package etsisi.upm.models;

import etsisi.upm.util.Constants;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Food extends ServiceProduct {

    public Food(int id, String name, double pricePerPerson, int numPeople, LocalDateTime expirationDate) {
        super(id, name, pricePerPerson, numPeople, expirationDate);
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


    // --- toString() ---
    @Override
    public String toString() {
        String parentToString = super.toString();
        // replace the name of the class
        return parentToString.replace("class:ServiceProduct", Constants.STR_FOOD);
    }
}