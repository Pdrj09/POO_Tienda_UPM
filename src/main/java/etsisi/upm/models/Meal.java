package etsisi.upm.models;

import etsisi.upm.Constants;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Meal extends ServiceProduct {

    private static final String STR_MEAL = "class:Meal";
    private static final String CLOSE_BRACE = "}";

    // Constructor sin menuDescription
    public Meal(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        super(id, name, pricePerPerson, maxPeople, expirationDate);
    }

    // --- Reglas de Antelación (3 Días) ---
    @Override
    public int getMinimumCreationTime() {
        return Constants.TIME_MEAL_PLANNING_DAYS;
    }

    @Override
    public ChronoUnit getMinimumTimeUnit() {
        return ChronoUnit.DAYS;
    }

    // --- Costo ---
    @Override
    public double calculateTotalCost(int participants) {
        return getPricePerPerson() * participants;
    }

    // --- toString() ---
    @Override
    public String toString() {
        String parentToString = super.toString();
        // Solo reemplaza el nombre de la clase
        return parentToString.replace("class:ServiceProduct", STR_MEAL);
    }
}