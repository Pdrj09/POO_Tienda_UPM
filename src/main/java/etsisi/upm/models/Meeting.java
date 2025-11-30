package etsisi.upm.models;

import etsisi.upm.Constants;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Meeting extends ServiceProduct {

    public Meeting(int id, String name, double pricePerPerson, int numPeople, LocalDateTime expirationDate) {
        super(id, name, pricePerPerson, numPeople, expirationDate);
    }

    // --- Date rules ---
    @Override
    public int getMinimumCreationTime() {
        return Constants.TIME_MEETING_PLANNING_HOURS;
    }

    @Override
    public ChronoUnit getMinimumTimeUnit() {
        return ChronoUnit.HOURS;
    }


    // --- toString() ---
    @Override
    public String toString() {
        String parentToString = super.toString();
        // replace the name of the class
        return parentToString.replace("class:ServiceProduct", Constants.STR_MEETING);
    }
}