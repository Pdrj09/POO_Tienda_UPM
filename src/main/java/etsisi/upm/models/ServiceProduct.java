package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.util.Categories;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.lang.Math;

public abstract class ServiceProduct extends Product {
    private final LocalDateTime expirationDate;
    private int numPeople;

    public ServiceProduct(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        super(id,
                name,
                pricePerPerson,
                Categories.EMPTY);

        numPeople = Math.min(maxPeople, Constants.TIME_MAX_PEOPLE_SERVICE);

        this.expirationDate = expirationDate;

        // time validation
        if (!isFeasible(LocalDateTime.now())) {
            String timeUnit = getMinimumTimeUnit() == ChronoUnit.HOURS ? "horas" : "días";
            StringBuilder error = new StringBuilder();
            error.append(Constants.ERROR_SERVICE_DATE_FEASIBILITY);
            error.append(getMinimumCreationTime());
            error.append(Constants.STR_BLANK_SPACE);
            error.append(timeUnit);
            error.append(Constants.IN_THE_FUTURE);
            throw new IllegalArgumentException(String.valueOf(error));
        }
    }

    // Abstract methods
    public abstract int getMinimumCreationTime();

    public abstract ChronoUnit getMinimumTimeUnit();

    public abstract double calculateTotalCost(int participants);

    // logic
    public boolean isFeasible(LocalDateTime creationTime) {
        long timeDifference = creationTime.until(expirationDate, getMinimumTimeUnit());
        return timeDifference >= getMinimumCreationTime();
    }

    public double getPricePerPerson() {
        return getPrice();
    }

    // --- toString() ---
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(OPEN_BRACE);
        builder.append(Constants.STR_SERVICE_PRODUCT);
        builder.append(STR_ID).append(getId());
        builder.append(STR_NAME).append(getName()).append(SINGLE_QUOTE);
        builder.append(STR_CATEGORY).append(getCategory());

        // use of the constant price per person
        builder.append(Constants.STR_PRICE_PERSON).append(getPrice());

        // add of the specific attribute for service product
        builder.append(Constants.STR_EXPIRATION).append(expirationDate);

        builder.append(CLOSE_BRACE);

        return builder.toString();
    }
}