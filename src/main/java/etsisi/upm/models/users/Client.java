package etsisi.upm.models.users;

import etsisi.upm.models.Ticket;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

//REPRESENTS DE CLIENT IN THE SYSTEM
public class Client extends User implements Comparable<Client> {
    private final String strIdCashier;
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

    //Validation messages
    private static final String ERR_DNI_LENGTH = "El DNI tiene que tener 9 digitos";
    private static final String ERR_CASHIER_NULL = "El id del cajero no puede ser null";


    //CONSTRUCTOR W/ ALL PARAMETERS
    public Client(String dni, String name, String email, String idCashier) {
        super(dni, name, email);
        if(dni.length() != 9)
            throw new IllegalArgumentException(ERR_DNI_LENGTH);
        this.associatedTickets = new TreeSet<>();
        if (idCashier == null)
            throw new IllegalArgumentException(ERR_CASHIER_NULL);
        this.strIdCashier = idCashier;
    }

    //GETTERS, public methods
    public Set<Ticket> getAssociatedTickets() {
        return associatedTickets;
    }

    public void deleteTicket(Ticket ticket){
        this.associatedTickets.remove(ticket);
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
                .append(STR_CLIENT).append(SINGLE_QUOTE).append(STR_DNI).append(getId()).append(SINGLE_QUOTE)
                .append(STR_NAME).append(SINGLE_QUOTE).append(getName()).append(SINGLE_QUOTE)
                .append(STR_EMAIL).append(SINGLE_QUOTE).append(getEmail()).append(SINGLE_QUOTE)
                .append(STR_CASH).append(SINGLE_QUOTE).append(getStrIdCashier()).append(SINGLE_QUOTE)
                .append(CLOSE_BRACE);
        return sb.toString();
    }
}
