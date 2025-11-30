package etsisi.upm.models.users;

import etsisi.upm.Constants;
import etsisi.upm.io.Presentable;
import etsisi.upm.models.Ticket;

import java.util.*;

//REPRESENTS A CASHIER IN THE SYSTEM
public class Cashier extends User {
    private final Set<Ticket> createdTickets;
    private final Set<Client> associatedClients;

    //CONSTRUCTOR W/ AUTOMATIC ID GENERATION
    private Cashier(String id, String emailCompany, String name) {
        super(id, name, emailCompany);
        this.createdTickets = new HashSet<>(); //for sorted it
        this.associatedClients = new TreeSet<>();
    }

    //PUBLIC METHODS
    //this two methods returns an INMUTABLE copy for more protection
    public Set<Ticket> getTickets() {
        return this.createdTickets;
    }

    public Set<Client> getAssociatedClients() {
        return Set.copyOf(associatedClients);
    }

    public void deleteTicket(Ticket ticket){
        this.createdTickets.remove(ticket);
    }

    //Double validation safety, factory method
    public static Cashier create(String id, String emailCompany, String name) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_ID_EMPTY);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_NAME_EMPTY);
        if (emailCompany == null || emailCompany.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_EMAIL_EMPTY);
        return new Cashier(id, emailCompany, name);
    }

    public void addTicket(Ticket ticket){
        this.createdTickets.add(ticket);
    }

    //COMPARABLE (name)
    @Override
    public int compareTo(User o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    //EQUALS AND HASHCODE
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cashier c = (Cashier) o;
        return getId().equals(c.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    //TO STRING, getter for maintaining "encapsulation"
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.OPEN_BRACE)
                .append(Constants.STR_CASHIER).append(Constants.QUOTE).append(Constants.STR_ID).append(getId())
                .append(Constants.STR_NAME).append(Constants.QUOTE).append(getName()).append(Constants.QUOTE)
                .append(Constants.STR_CASH_EMAIL).append(Constants.QUOTE).append(getEmail()).append(Constants.QUOTE)
                .append(Constants.STR_TICKETS).append(getTickets())
                .append(Constants.CLOSE_BRACE);
        return sb.toString();
    }
}
