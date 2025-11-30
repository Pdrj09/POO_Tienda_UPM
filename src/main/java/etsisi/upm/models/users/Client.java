package etsisi.upm.models.users;

import etsisi.upm.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.models.Ticket;

import java.util.*;

//REPRESENTS DE CLIENT IN THE SYSTEM
public class Client extends User {
    private final String strIdCashier;
    private final Set<Ticket> associatedTickets;


    //CONSTRUCTOR W/ ALL PARAMETERS
    public Client(String dni, String name, String email, String idCashier) {
        super(dni, name, email);
        validateDni(dni);
        this.associatedTickets = new HashSet<>();
        if (idCashier == null)
            throw new IllegalArgumentException(Constants.ERROR_CASHIER_NULL);
        this.strIdCashier = idCashier;
    }

    //GETTERS, public methods
    public Set<Ticket> getAssociatedTickets() {
        return associatedTickets;
    }

    public void addAssociatedTicket(Ticket ticket){
        this.associatedTickets.add(ticket);
    }

    public void deleteTicket(Ticket ticket){
        this.associatedTickets.remove(ticket);
    }

    public String getStrIdCashier() {
        return strIdCashier;
    }

    public static void validateDni(String dni) {
        if (dni == null || dni.length() != Constants.NINE)
            throw new IllegalArgumentException(Constants.ERROR_DNI_LENGTH);
        String numbers = dni.substring(Constants.ZERO, Constants.EIGHT);
        char letter = Character.toUpperCase(dni.charAt(Constants.EIGHT));
        //check numeric part
        if (!numbers.matches(Constants.DNI_REGEX))
            throw new IllegalArgumentException(Constants.ERROR_DNI_DIGITS);
        //expected letter
        int num = Integer.parseInt(numbers);
        char expected = Constants.DNI_LETTERS.charAt(num % Constants.ALPHABET_NUM);
        if (letter != expected)
            throw new IllegalArgumentException(
                    Constants.ERROR_INVALID_DNI_1 + expected + Constants.ERROR_INVALID_DNI_2 + numbers
            );
    }


    //COMPARABLE BY NAME
    @Override
    public int compareTo(User o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = super.toViewKVList(); //id, name, email
        kvs.add(new KV("DNI", getId())); //Here we use de DNI as ID for the view
        kvs.add(new KV("Cashier ID", getStrIdCashier()));
        kvs.removeIf(kv -> kv.key.equals("ID"));
        return kvs;
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
        sb.append(Constants.OPEN_BRACE)
                .append(Constants.STR_CLIENT).append(Constants.QUOTE).append(Constants.STR_DNI).append(getId()).append(Constants.QUOTE)
                .append(Constants.STR_CLI_NAME).append(Constants.QUOTE).append(getName()).append(Constants.QUOTE)
                .append(Constants.STR_CLIENT_EMAIL).append(Constants.QUOTE).append(getEmail()).append(Constants.QUOTE)
                .append(Constants.STR_CASH).append(Constants.QUOTE).append(getStrIdCashier()).append(Constants.QUOTE)
                .append(Constants.CLOSE_BRACE);
        return sb.toString();
    }
}
