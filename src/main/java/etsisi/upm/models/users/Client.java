package etsisi.upm.models.users;

import etsisi.upm.models.Ticket;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

//REPRESENTS DE CLIENT IN THE SYSTEM
public class Client extends User implements Comparable<Client> {
    private String strIdCashier;
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
    public Client(String dni, String name, String email, String idCashier) {
        super(dni, name, email);
        if(dni == null || dni.isEmpty())
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        this.associatedTickets = new TreeSet<>();
        this.strIdCashier = idCashier;
    }

    //GETTERS, public methods
    public Set<Ticket> getAssociatedTickets() {
        return associatedTickets;
    }

    public String getStrIdCashier() {
        return strIdCashier;
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
                .append(STR_CASH).append(getStrIdCashier()).append(SINGLE_QUOTE)
                .append(CLOSE_BRACE);
        return sb.toString();
    }
}
