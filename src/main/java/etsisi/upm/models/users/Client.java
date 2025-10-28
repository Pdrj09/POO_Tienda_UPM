package etsisi.upm.models.users;

import java.util.Objects;

//REPRESENTS DE CLIENT IN THE SYSTEM
public class Client implements Comparable<Client> {
    private final String dni; //unique id for client
    private String name;
    private String email;
    private final String cashCreatorId; //ID of the cashier who created THIS client

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
    public Client(String dni, String name, String email, String cashCreatorId) {
        if(dni == null || dni.isEmpty())
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        this.dni = dni;
        this.name = name;
        this.email = email;
        this.cashCreatorId = cashCreatorId;
    }

    //GETTERS AND SETTERS, public methods
    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCashCreatorId() {
        return cashCreatorId;
    }

    //COMPARABLE OF THE NAME
    @Override
    public int compareTo(Client o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    //EQUALS AND HASHCODE
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(dni, client.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }

    //TO STRING

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OPEN_BRACE)
                .append(STR_CLIENT).append(STR_DNI).append(dni).append(SINGLE_QUOTE)
                .append(STR_NAME).append(name).append(SINGLE_QUOTE)
                .append(STR_EMAIL).append(email).append(SINGLE_QUOTE)
                .append(STR_CASH).append(cashCreatorId).append(SINGLE_QUOTE)
                .append(CLOSE_BRACE);
        return sb.toString();
    }
}
