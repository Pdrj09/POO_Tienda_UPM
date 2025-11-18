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
            throw new IllegalArgumentException("Error: La fecha de caducidad debe ser al menos " + getMinimumCreationTime() + " " + timeUnit + " en el futuro.");
        }
    }

    // --- Métodos Abstractos ---
    public abstract int getMinimumCreationTime();
    public abstract ChronoUnit getMinimumTimeUnit();
    public abstract double calculateTotalCost(int participants);

    // --- Lógica de Antelación ---
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

        // USANDO CONSTANTES PROTECTED HEREDADAS DE PRODUCT:
        int lastBrace = builder.lastIndexOf(CLOSE_BRACE);
        int indexPrice = builder.lastIndexOf(STR_PRICE); // <- USAR STR_PRICE (de Product)

        if (lastBrace != -1) {
            if(indexPrice != -1) {
                // El tamaño del label a reemplazar es el de la constante heredada
                builder.replace(indexPrice, indexPrice + STR_PRICE.length(), STR_PRICE_PERSON);
            }
            builder.insert(lastBrace, STR_EXPIRATION + expirationDate);
        }
        return builder.toString();
    }
}
