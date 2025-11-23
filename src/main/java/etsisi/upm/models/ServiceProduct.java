package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.util.Categories;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.lang.Math;

public abstract class ServiceProduct extends Product {
    private final LocalDateTime expirationDate;

    private static final String STR_PRICE_PERSON = ", pricePerPerson:";
    private static final String STR_SERVICE_PRODUCT = "class:ServiceProduct";
    private static final String STR_EXPIRATION = ", expiration:";

    public ServiceProduct(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        super(id,
                name,
                pricePerPerson,
                Categories.EMPTY,
                Math.min(maxPeople, Constants.TIME_MAX_PEOPLE_SERVICE));

        this.expirationDate = expirationDate;

        if (!isFeasible(LocalDateTime.now())) {
            String timeUnit = getMinimumTimeUnit() == ChronoUnit.HOURS ? "horas" : "días";
            throw new IllegalArgumentException("Error: The date must be  " + getMinimumCreationTime() + " " + timeUnit + " in the future.");
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
        String parentToString = super.toString();

        String currentToString = parentToString.replace("class:Product", STR_SERVICE_PRODUCT);
        StringBuilder builder = new StringBuilder(currentToString);

        // using constants inhered from Product:
        int lastBrace = builder.lastIndexOf(CLOSE_BRACE);
        int indexPrice = builder.lastIndexOf(STR_PRICE);

        if (lastBrace != -1) {
            if(indexPrice != -1) {
                // The size of the label to be replaced is that of the inherited constant
                builder.replace(indexPrice, indexPrice + STR_PRICE.length(), STR_PRICE_PERSON);
            }
            builder.insert(lastBrace, STR_EXPIRATION + expirationDate);
        }
        return builder.toString();
    }
}
