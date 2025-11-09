package etsisi.upm.models.users;

import etsisi.upm.models.Ticket;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

//REPRESENTS DE CLIENT IN THE SYSTEM
public class Client extends User implements Comparable<Client> {
    private Cashier cashier;
    private final Set<Ticket> associatedTickets;

    //CONSTANTS
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";
    private static final String STR_CLIENT = "class:Client";
    private static final String STR_DNI = ", dni:'";
    private static final String STR_NAME = ", name:'";
    private static final String STR_EMAIL = ", email:'";
    private static final String STR_CASH = ", cashCreatorId:'";
    private static final String SINGLE_QUOTE = "'";


    //CONSTRUCTOR W/ ALL PARAMETERS
    public Client(String dni, String name, String email, Cashier cashier, Set<Ticket> associatedTickets) {
        super(dni, name, email);
        if(dni == null || dni.isEmpty())
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        if (cashier == null)
            throw new IllegalArgumentException("El cajero asociado no puede ser nulo");
        if (associatedTickets == null)
            this.associatedTickets = new TreeSet<>();
        else
            this.associatedTickets = new TreeSet<>(associatedTickets);
        this.cashier = cashier;
        this.cashier.addClient(this);
    }

    //GETTERS, public methods
    public Set<Ticket> getAssociatedTickets() {
        return associatedTickets;
    }
    public Cashier getCashier() {
        return cashier;
    }

    //METHODS FOR MANAGING TICKETS
    public void addTicket(Ticket ticket){
        if (ticket != null)
            associatedTickets.add(ticket);
    }

    public void removeTicket(Ticket ticket){
        associatedTickets.remove(ticket);
    }

    void assignCashier (Cashier cashier){
        if (this.cashier == null)
            this.cashier = cashier;
    }


    //COMPARABLE BY NAME
    @Override
    public int compareTo(Client o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    //EQUALS AND HASHCODE
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    //TO STRING

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OPEN_BRACE)
                .append(STR_CLIENT).append(STR_DNI).append(getId()).append(SINGLE_QUOTE)
                .append(STR_NAME).append(getName()).append(SINGLE_QUOTE)
                .append(STR_EMAIL).append(getEmail()).append(SINGLE_QUOTE)
                .append(STR_CASH).append(getCashier().getId()).append(SINGLE_QUOTE)
                .append(CLOSE_BRACE);
        return sb.toString();
    }
}
