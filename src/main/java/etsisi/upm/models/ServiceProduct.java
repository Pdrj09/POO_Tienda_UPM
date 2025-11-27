package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.util.Categories;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.lang.Math;

public abstract class ServiceProduct extends Product {
    private final LocalDateTime expirationDate;

    private static final String STR_PRICE_PERSON = ", pricePerPerson:";
    private static final String STR_EXPIRATION = ", expiration:";
    private static final String STR_SERVICE_PRODUCT = "class:ServiceProduct";

    public ServiceProduct(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        super(id,
                name,
                pricePerPerson,
                Categories.EMPTY);

        int limitedMaxPeople = Math.min(maxPeople, Constants.TIME_MAX_PEOPLE_SERVICE);
        super.setMaxPers(limitedMaxPeople);

        this.expirationDate = expirationDate;

        // time validation
        if (!isFeasible(LocalDateTime.now())) {
            String timeUnit = getMinimumTimeUnit() == ChronoUnit.HOURS ? "horas" : "días";
            String errorMessage = Constants.ERROR_SERVICE_DATE_FEASIBILITY + getMinimumCreationTime() + " " + timeUnit + " en el futuro.";
            throw new IllegalArgumentException(errorMessage);
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
        builder.append(STR_SERVICE_PRODUCT);
        builder.append(STR_ID).append(getId());
        builder.append(STR_NAME).append(getName()).append(SINGLE_QUOTE);
        builder.append(STR_CATEGORY).append(getCategory());

        // use of the constant price per person
        builder.append(STR_PRICE_PERSON).append(getPrice());

        // add of the specific attribute for service product
        builder.append(STR_EXPIRATION).append(expirationDate);

        builder.append(CLOSE_BRACE);

        return builder.toString();
    }
}