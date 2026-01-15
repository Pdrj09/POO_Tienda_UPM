package etsisi.upm.models;

import etsisi.upm.util.Categories;
import etsisi.upm.util.Constants;
import etsisi.upm.util.TicketStates;
import etsisi.upm.util.Utilities;

import java.time.LocalDateTime;
import java.util.*;

public class TicketOfMixed extends Ticket<Sellable> {
    public TicketOfMixed(String id) {
        LocalDateTime now = LocalDateTime.now();
        String formatted = Utilities.formatDate(now);
        id = formatted + Constants.HYPEN +id;
        super(id);
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
        if (!list.isEmpty()) {
            int quantity;
            Set<Sellable> p = list.keySet();
            Set<Sellable> products = new HashSet<>(p);

            boolean hasProduct = false;
            boolean hasService = false;

            for (Sellable s : products) {
                if (s instanceof Product) {
                    hasProduct = true;
                } else if (s instanceof ServiceProduct) {
                    hasService = true;
                }

                if (hasProduct && hasService) break;
            }

            if (!(hasProduct && hasService)) {
                throw new IllegalArgumentException(Constants.ERROR_INVALID_PRINT_MIXED_TICKET);
            }

            for (Sellable prod : products) {
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

