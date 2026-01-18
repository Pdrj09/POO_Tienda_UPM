package etsisi.upm.models;

import etsisi.upm.util.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tickets_of_products")
public class TicketOfProducts extends Ticket<Product> {
    public TicketOfProducts(String id){
        super(id);
    }

    public TicketOfProducts() {
        super();
    }

    @Override
    public Ticket<Product> addProduct(Sellable prod, int amount) {
        if (!(prod instanceof Product product))
            throw new IllegalArgumentException(Constants.ERROR_INVALID_TICKET_PROD_TYPE);

        if (countProducts() + amount > Constants.MAX_SIZE_TICKET)
            throw new IllegalStateException(Constants.ERROR_MAXSIZE_TICKET + Constants.MAX_SIZE_TICKET);

        if (amount < Constants.MIN_AMMOUNT) throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);

        addToItem(product, amount);

        Categories category = prod.getCategory();

        this.categories.put(category, this.categories.getOrDefault(category, Constants.BASE_AMOUNT_OF_CATEGORY) + amount);
        this.state = TicketStates.ACTIVE;

        return this;
    }

    @Override
    public String close() {
        if (this.items.isEmpty())
            throw new SecurityException(Constants.ERROR_EMPTY_TICKET);
        this.closeDate = LocalDateTime.now();
        this.state = TicketStates.CLOSED;
        return this.getId();
    }

}
