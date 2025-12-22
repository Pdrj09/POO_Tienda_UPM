package etsisi.upm.models.users;

import etsisi.upm.util.Constants;
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
        validateDniNif(dni);
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

    public static void validateDniNif(String dniNif) {
        if (dniNif == null || dniNif.length() != Constants.DNI_LENGTH)
            throw new IllegalArgumentException(Constants.ERROR_DNI_LENGTH);
        String normalizedNif = dniNif.toUpperCase();
        String core = normalizedNif.substring(Constants.DNINIF_POS_START, Constants.DNINIF_POS_END);
        char finalLetter = normalizedNif.charAt(Constants.DNINIF_POS_END);

        String calculationPart = core;
        if (core.startsWith("X")) {
            calculationPart = core.replaceFirst("X", "0");
        } else if (core.startsWith("Y")) {
            calculationPart = core.replaceFirst("Y", "1");
        } else if (core.startsWith("Z")) {
            calculationPart = core.replaceFirst("Z", "2");
        }

        String numbers = calculationPart.substring(Constants.DNINIF_POS_START, Constants.DNINIF_POS_END);
        if (!numbers.matches(Constants.DNI_REGEX))
            throw new IllegalArgumentException(Constants.ERROR_DNI_DIGITS);
        int num = Integer.parseInt(numbers);
        char expected = Constants.DNI_LETTERS.charAt(num % Constants.ALPHABET_NUM);

        if (finalLetter != expected)
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
        kvs.add(new KV(Constants.CLI_DNI, getId())); //Here we use de DNI as ID for the view
        kvs.add(new KV(Constants.CASHIER_ID, getStrIdCashier()));
        kvs.removeIf(kv -> kv.key.equals(Constants.ID));
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
