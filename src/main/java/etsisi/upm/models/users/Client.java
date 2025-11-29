package etsisi.upm.models.users;

import etsisi.upm.Constants;
import etsisi.upm.models.Ticket;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

//REPRESENTS DE CLIENT IN THE SYSTEM
public class Client extends User implements Comparable<Client> {
    private final String strIdCashier;
    private final Set<Ticket> associatedTickets;
    private static final String DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

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
    private static final String ERR_DNI_LENGTH = "Invalid DNI: wrong length (must be 9 chars)";
    private static final String ERR_CASHIER_NULL = "El id del cajero no puede ser null";
    private static final String ERR_DNI_DIGITS = "Invalid DNI: first 8 characters must be numbers";
    private static final String ERR_NIE_FORMAT = "The NIE must start with X, Y or Z";
    private static final String DNI_REGEX = "\\d{8}";
    private static final String NIE_REGEX = "^[XYZxyz]\\d{7}[A-Za-z]$";

    //CONSTRUCTOR W/ ALL PARAMETERS
    public Client(String dni, String name, String email, String idCashier) {
        super(dni, name, email);
        validateDni(dni);
        this.associatedTickets = new HashSet<>();
        if (idCashier == null)
            throw new IllegalArgumentException(ERR_CASHIER_NULL);
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
        if (dni == null || dni.length() != 9)
            throw new IllegalArgumentException(ERR_DNI_LENGTH);
        String numbers = dni.substring(0, 8);
        char letter = Character.toUpperCase(dni.charAt(8));
        //check numeric part

        if (dni.matches(NIE_REGEX)) {
            char letra =  dni.charAt(0);
            switch (letra) {
                case 'X':
                    dni = dni.replaceFirst(String.valueOf(letra), String.valueOf(Constants.ZERO));
                    break;
                case 'Y':
                    dni = dni.replaceFirst(String.valueOf(letra), String.valueOf(Constants.ONE));
                    break;
                case 'Z':
                    dni = dni.replaceFirst(String.valueOf(letra), String.valueOf(Constants.TWO));
                    break;
                    default:
                        throw new IllegalArgumentException(ERR_NIE_FORMAT);
            }
            numbers = dni.substring(0, 8);
        }

        if (!numbers.matches(DNI_REGEX) )
            throw new IllegalArgumentException(ERR_DNI_DIGITS);
        //expected letter
        int num = Integer.parseInt(numbers);
        char expected = DNI_LETTERS.charAt(num % 23);
        if (letter != expected)
            throw new IllegalArgumentException(
                    "Invalid DNI: wrong letter. Expected " + expected + " for " + numbers
            );
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
