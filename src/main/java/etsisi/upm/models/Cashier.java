package etsisi.upm.models;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

//REPRESENTS A CASHIER IN THE SYSTEM
public class Cashier implements Comparable<Cashier>{
    private final String id; //unique id (UW + 7digits)
    private String name;
    private String emailCompany;
    private final Set<String> tickets; //tickets' IDs created by the cashier

    //CONSTANT FOR THE toSTRING
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";
    private static final String STR_CASHIER = "class:Cashier";
    private static final String STR_ID = ", id:";
    private static final String STR_NAME = ", name:";
    private static final String STR_EMAIL = ", emailCompany:";
    private static final String STR_TICKETS = ", tickets:";
    private static final String QUOTE = "'";
    private static final String NEXT_LINE = "\n";


    //CONSTRUCTOR W/ AUTOMATIC ID GENERATION
    public Cashier(String emailCompany, String name) {
        this.id = IdGenerator.generateCashierId();
        this.name = name;
        this.emailCompany = emailCompany;
        this.tickets = new TreeSet<>(); //for sorted it
    }

    //CONSTRUCTOR W/ MANUAL ID
    public Cashier(String id, Set<String> tickets, String emailCompany, String name) {
        if (id == null || id.isEmpty())
            this.id = IdGenerator.generateCashierId();
        else{
            if (IdGenerator.isUsed(id))
                throw new IllegalArgumentException("Ya existe un cajero con ID: " + id);
            this.id = id;
            IdGenerator.registerId(id);
        }
        this.tickets = new TreeSet<>();
        this.emailCompany = emailCompany;
        this.name = name;
    }

    //PUBLIC METHODS
    public String getId() {
        return id;
    }

    public Set<String> getTickets() {
        return tickets;
    }

    public String getEmailCompany() {
        return emailCompany;
    }

    public void setEmailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //COMPARABLE (name)
    @Override
    public int compareTo(Cashier o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    //EQUALS AND HASHCODE
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cashier c = (Cashier) o;
        return id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //TO STRING
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OPEN_BRACE)
                .append(STR_CASHIER).append(STR_ID).append(id)
                .append(STR_NAME).append(name).append(QUOTE)
                .append(STR_EMAIL).append(emailCompany).append(QUOTE)
                .append(STR_TICKETS).append(tickets)
                .append(CLOSE_BRACE);
        return sb.toString();
    }
}
