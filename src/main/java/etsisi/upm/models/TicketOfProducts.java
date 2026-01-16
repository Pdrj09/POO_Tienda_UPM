package etsisi.upm.models;

import etsisi.upm.util.*;

import java.time.LocalDateTime;
import java.util.*;

public class TicketOfProducts extends Ticket<Product> {
    public TicketOfProducts(String id){
        super(id);
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
        if (!list.isEmpty()) {
            int quantity;
            Set<Product> p = list.keySet();
            Set<Product> products = new HashSet<>(p);
            for (Product prod : products) {
                quantity = list.get(prod);

                list.remove(prod);
                list.put(prod.copy(), quantity);
            }
            this.closeDate = LocalDateTime.now();
            String date = Utilities.formatDate(this.closeDate);
            this.id += Constants.HYPEN + date;
            this.state = TicketStates.CLOSED;

            return this.getId();
        } else {
            throw new SecurityException(Constants.ERROR_EMPTY_TICKET);
        }
    }

}
