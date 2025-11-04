package etsisi.upm.models.repositories;

import etsisi.upm.models.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TicketRepository {
    private Map<String, Ticket> ticketsMap;

    public void add(Ticket ticket, String cashierId){
        this.ticketsMap.put(cashierId,ticket);
    }

    public Ticket findById(String id){
        return this.ticketsMap.get(id);
    }

    public Ticket removeById(String id){
        return this.ticketsMap.remove(id);
    }

    public Collection<Ticket> findAll(){
        return this.ticketsMap.values();
    }
}
