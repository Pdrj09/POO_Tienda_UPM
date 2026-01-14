package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.util.TicketStates;
import etsisi.upm.util.Utilities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class TicketOfProducts extends Ticket {
    public TicketOfProducts(String id){
        LocalDateTime now = LocalDateTime.now();
        String formatted = Utilities.formatDate(now);
        super.id = formatted + Constants.HYPEN +id;
        super.list = new TreeMap<Product, Integer>();
        super.categories = new HashMap<>();
        super.state = TicketStates.EMPTY;
    }
    public TicketOfProducts(){
        super(String.format(Constants.ID_FORMAT, new Random().nextInt(Constants.MAX_RANDOM)));
    }
}
