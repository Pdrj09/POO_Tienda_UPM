package etsisi.upm.models;

import etsisi.upm.util.*;

import java.time.LocalDateTime;
import java.util.*;

public class TicketOfServices extends Ticket<ServiceProduct> {
    public TicketOfServices(String id){
        super(id);
    }

    @Override
    public Ticket<ServiceProduct> addProduct(Sellable prod, int amount) {
        if (prod instanceof ServiceProduct product) {
            if (countProducts() + amount > Constants.MAX_SIZE_TICKET)
                throw new IllegalStateException(Constants.ERROR_MAXSIZE_TICKET + Constants.MAX_SIZE_TICKET);

            if (amount < Constants.MIN_AMMOUNT) throw new IllegalStateException(Constants.ERROR_ZERO_AMOUNT);

            if (this.list.containsKey(prod)) {
                this.list.compute(product, (k, currentAmount) -> currentAmount + amount);
            } else
                this.list.put(product, amount);

            double calculatedTotal = product.getPricePerPerson() * amount;
            product.setFinalPrice(calculatedTotal);

            Categories category = prod.getCategory();

            this.categories.put(category, this.categories.getOrDefault(category, Constants.BASE_AMOUNT_OF_CATEGORY) + amount);
            this.state = TicketStates.ACTIVE;
        } else {
            throw new IllegalArgumentException(Constants.ERROR_INVALID_TICKET_PROD_TYPE);
        }
        return this;
    }

    @Override
    public String close() {
        if (!list.isEmpty()) {
            int quantity;
            Set<ServiceProduct> p = list.keySet();
            Set<ServiceProduct> products = new HashSet<>(p);
            for (ServiceProduct prod : products) {
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


