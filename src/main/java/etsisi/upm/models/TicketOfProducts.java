package etsisi.upm.models;

import etsisi.upm.util.*;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class TicketOfProducts extends Ticket<Product> {
    public TicketOfProducts(String id){
        super(id);
    }

    public TicketOfProducts() {
        super();
    }

    @Override
    public Ticket<Product> addProduct(Sellable prod, int amount) {
        if (!(prod instanceof Product))
            throw new IllegalArgumentException(Constants.ERROR_INVALID_TICKET_PROD_TYPE);

        if (countProducts() + amount > Constants.MAX_SIZE_TICKET)
            throw new IllegalStateException(Constants.ERROR_MAXSIZE_TICKET + Constants.MAX_SIZE_TICKET);

        if (amount < Constants.MIN_AMMOUNT) throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);

        if (this.list.containsKey(prod)) {
            this.list.compute((Product) prod, (k, currentAmount) -> currentAmount + amount);
        }else
            this.list.put((Product) prod, amount);

        Categories category = prod.getCategory();

        this.categories.put(category, this.categories.getOrDefault(category, Constants.BASE_AMOUNT_OF_CATEGORY) + amount);
        this.state = TicketStates.ACTIVE;

        return this;
    }

    @Override
    public String close() {
        if (this.list.isEmpty())
            throw new SecurityException(Constants.ERROR_EMPTY_TICKET);
        this.closeDate = LocalDateTime.now();
        this.state = TicketStates.CLOSED;
        return this.getId();
    }

}
