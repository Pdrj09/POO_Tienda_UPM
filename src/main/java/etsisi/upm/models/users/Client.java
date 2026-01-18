package etsisi.upm.models.users;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.models.Ticket;
import jakarta.persistence.*;


import java.util.*;

@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
public class Client extends User {

    @ManyToOne
    @JoinColumn(name = "cashier_db_id", nullable = false)
    private Cashier cashier;

    @OneToMany(targetEntity = Ticket.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_associated_tickets",
            joinColumns = @JoinColumn(name = "client_db_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private Set<Ticket<?>> associatedTickets;

    public Client(){}

    //CONSTRUCTOR W/ ALL PARAMETERS
    public Client(String dni, String name, String email, Cashier cashier) {
        super(dni, name, email);
        validateDniNif(dni);
        this.associatedTickets = new HashSet<>();
        if (cashier == null)
            throw new IllegalArgumentException(Constants.ERROR_CASHIER_NULL);
        this.cashier = cashier;
    }

    //GETTERS, public methods
    public Set<Ticket<?>> getAssociatedTickets() {
        return associatedTickets;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public void addAssociatedTicket(Ticket<?> ticket){
        this.associatedTickets.add(ticket);
    }

    public void deleteTicket(Ticket<?> ticket){
        this.associatedTickets.remove(ticket);
    }

    public Cashier getCashier() {
        return this.cashier;
    }

    public static void validateDniNif(String dniNif) {
        if (dniNif == null || dniNif.length() != Constants.DNI_LENGTH)
            throw new IllegalArgumentException(Constants.ERROR_DNI_LENGTH);

        String normalizedId = dniNif.toUpperCase();

        boolean startsWithLetter = Character.isLetter(normalizedId.charAt(Constants.DNINIF_POS_START));
        boolean endsWithLetter   = Character.isLetter(normalizedId.charAt(Constants.DNINIF_POS_END));

        if (startsWithLetter == endsWithLetter) {
                throw new IllegalArgumentException(Constants.ERROR_INVALID_DNI_NIF_FORMAT);
        }

        if (startsWithLetter && !endsWithLetter) {
            String nifNumbers = normalizedId.substring(1);
            if (!nifNumbers.matches(Constants.DNI_REGEX)) {
                throw new IllegalArgumentException(Constants.ERROR_COMPANY_ID);
            }
            return;
        }

        String core = normalizedId.substring(Constants.DNINIF_POS_START, Constants.DNINIF_POS_END);
        char finalLetter = normalizedId.charAt(Constants.DNINIF_POS_END);

        String calculationPart = core;
        if (core.startsWith("X")) {
            calculationPart = core.replaceFirst("X", "0");
        } else if (core.startsWith("Y")) {
            calculationPart = core.replaceFirst("Y", "1");
        } else if (core.startsWith("Z")) {
            calculationPart = core.replaceFirst("Z", "2");
        }

        String numbers = calculationPart.substring(0, calculationPart.length());
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
        kvs.add(new KV(Constants.CASHIER_ID, getCashier().getId()));
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
                .append(Constants.STR_CASH).append(Constants.QUOTE).append(getCashier().getId()).append(Constants.QUOTE)
                .append(Constants.CLOSE_BRACE);
        return sb.toString();
    }
}
