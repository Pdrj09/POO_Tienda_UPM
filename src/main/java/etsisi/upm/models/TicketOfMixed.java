package etsisi.upm.models;

import etsisi.upm.util.Categories;
import etsisi.upm.util.Constants;
import etsisi.upm.util.TicketStates;
import etsisi.upm.util.Utilities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class TicketOfMixed extends Ticket<Sellable> {
    public TicketOfMixed(String id) {
        LocalDateTime now = LocalDateTime.now();
        String formatted = Utilities.formatDate(now);
        super.id = formatted + Constants.HYPEN + id;
        super.list = new TreeMap<Sellable, Integer>();
        super.categories = new HashMap<>();
        super.state = TicketStates.EMPTY;
    }
    public TicketOfMixed(){
        super(String.format(Constants.ID_FORMAT, new Random().nextInt(Constants.MAX_RANDOM)));
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
            Set<Sellable> products = list.keySet();
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

