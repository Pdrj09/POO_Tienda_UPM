package etsisi.upm.models.repositories;

import etsisi.upm.models.Ticket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TicketRepository implements RepositoryInterface<Ticket> {
    private final Map<String, Ticket> ticketsMap;

    public TicketRepository() {
        ticketsMap = new HashMap<>();
    }

    @Override
    public void add(Ticket ticket) {
        this.ticketsMap.put(ticket.getId(), ticket);
    }

    @Override
    public Ticket findById(String id){
        return this.ticketsMap.get(id);
    }

    @Override
    public Ticket removeById(String id){
        return this.ticketsMap.remove(id);
    }

    @Override
    public Collection<Ticket> findAll(){
        return this.ticketsMap.values();
    }
}
