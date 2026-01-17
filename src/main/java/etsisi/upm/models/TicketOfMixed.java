package etsisi.upm.models;

import etsisi.upm.util.Categories;
import etsisi.upm.util.Constants;
import etsisi.upm.util.TicketStates;
import etsisi.upm.util.Utilities;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class TicketOfMixed extends Ticket<Sellable> {
    public TicketOfMixed(String id) {
        super(id);
    }

    public TicketOfMixed() {
        super();
    }

    @Override
    public Ticket<Sellable> addProduct(Sellable prod, int amount) {
        if (countProducts() + amount > Constants.MAX_SIZE_TICKET)
            throw new IllegalStateException(Constants.ERROR_MAXSIZE_TICKET + Constants.MAX_SIZE_TICKET);

        if (amount < Constants.MIN_AMMOUNT) throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);

        if (this.list.containsKey(prod)) {
            this.list.compute(prod, (k, currentAmount) -> currentAmount + amount);
        }else
            this.list.put(prod, amount);

        if (prod instanceof ServiceProduct service) {
            double calculatedTotal = service.getPricePerPerson() * amount;
            service.setFinalPrice(calculatedTotal);
        }

        Categories category = prod.getCategory();

        this.categories.put(category, this.categories.getOrDefault(category, Constants.BASE_AMOUNT_OF_CATEGORY) + amount);
        this.state = TicketStates.ACTIVE;

        return this;
    }

    @Override
    public String close() {
        if (list.isEmpty()) throw new SecurityException(Constants.ERROR_EMPTY_TICKET);

        // Validación simplificada con Stream
        boolean hasProduct = list.keySet().stream().anyMatch(s -> s instanceof Product);
        boolean hasService = list.keySet().stream().anyMatch(s -> s instanceof ServiceProduct);

        if (!(hasProduct && hasService)) {
            throw new IllegalArgumentException(Constants.ERROR_INVALID_PRINT_MIXED_TICKET);
        }

        // NO MODIFICAR EL ID AQUÍ. Solo el estado y la fecha.
        this.closeDate = LocalDateTime.now();
        this.state = TicketStates.CLOSED;
        return this.getId();
    }

    @Override
    protected double totalDiscount() {
        double baseDiscount= super.totalDiscount();
        long numServices = list.keySet().stream()
                .filter(item -> item instanceof ServiceProduct)
                .count();
        if (numServices >0) {
            double productTotal = list.entrySet().stream()
                    .filter(entry ->entry.getKey() instanceof Product)
                    .mapToDouble(entry-> calculateProductPrice(entry.getKey()) *entry.getValue())
                    .sum();
            baseDiscount += productTotal * (0.15 *numServices);
        }
        return Utilities.round(baseDiscount);
    }
}