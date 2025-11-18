package etsisi.upm.models.users;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

//REPRESENTS A CASHIER IN THE SYSTEM
public class Cashier extends User implements Comparable<Cashier>{
    private final SortedSet<String> tickets; //tickets' IDs created by the cashier
    private final Set<Client> associatedClients;

    //CONSTANT FOR THE toSTRING
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";
    private static final String STR_CASHIER = "class:Cashier";
    private static final String STR_ID = ", id:";
    private static final String STR_NAME = ", name:";
    private static final String STR_EMAIL = ", emailCompany:";
    private static final String STR_TICKETS = ", tickets:";
    private static final String QUOTE = "'";

    //Validation messages
    private static final String ERR_ID_EMPTY = "El ID no puede estar vacío";
    private static final String ERR_NAME_EMPTY = "El nombre no puede estar vacío";
    private static final String ERR_EMAIL_EMPTY = "El email no puede estar vacío";


    //CONSTRUCTOR W/ AUTOMATIC ID GENERATION
    private Cashier(String id, String emailCompany, String name) {
        super(id, name, emailCompany);
        this.tickets = new TreeSet<>(); //for sorted it
        this.associatedClients = new TreeSet<>();
    }

    //PUBLIC METHODS
    //this two methods returns an INMUTABLE copy for more protection
    public Set<String> getTickets() {
        return Set.copyOf(tickets);
    }
    public Set<Client> getAssociatedClients() {
        return Set.copyOf(associatedClients);
    }

    //Double validation safety, factory method
    public static Cashier create(String id, String emailCompany, String name) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException(ERR_ID_EMPTY);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException(ERR_NAME_EMPTY);
        if (emailCompany == null || emailCompany.isBlank())
            throw new IllegalArgumentException(ERR_EMAIL_EMPTY);
        return new Cashier(id, emailCompany, name);
    }

    //COMPARABLE (name)
    @Override
    public int compareTo(Cashier o) {
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

    //TO STRING, getter for mantaining "encapsulacion"
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OPEN_BRACE)
                .append(STR_CASHIER).append(QUOTE).append(STR_ID).append(getId())
                .append(STR_NAME).append(QUOTE).append(getName()).append(QUOTE)
                .append(STR_EMAIL).append(QUOTE).append(getEmail()).append(QUOTE)
                .append(STR_TICKETS).append(getTickets())
                .append(CLOSE_BRACE);
        return sb.toString();
    }
}
