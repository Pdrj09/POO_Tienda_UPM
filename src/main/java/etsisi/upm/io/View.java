package etsisi.upm.io;

import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.util.Collection;

public class View {
    //colours ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    //messages
    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;
    private static final String MSG_INFO_PREFIX = CYAN + "[INFO]" + RESET + " ";

    //Here we print an object w/ toString()
    public static <T> StringBuilder print(T element, Class<?> type) {
        StringBuilder sb = new StringBuilder();
        if (element == null) {
            sb.append(emptyMessage(type));
            return sb;
        }
        //for collections
        if (element instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                sb.append(emptyMessage(type));
                return sb;
            }
            for (Object item : collection) {
                sb.append(typeHeader(item));
                sb.append(item.toString()).append("\n");
            }
            return sb;
        }

        //for arrays
        if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0) {
                sb.append(emptyMessage(type));
                return sb;
            }
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(element, i);
                sb.append(typeHeader(item));
                sb.append(item.toString()).append("\n");
            }
            return sb;
        }

        //normal object
        sb.append(typeHeader(element));
        sb.append(element.toString()).append("\n");
        return sb;
    }


    //Prints custom empty message based on class type
    private static String emptyMessage(Class<?> type) {
        if (type == null)
            return MSG_NOTHING_TO_SHOW + "\n";
        else if (Cashier.class.equals(type))
            return YELLOW + "[!] No cashiers registered." + RESET + "\n";
        else if (Client.class.equals(type))
            return YELLOW + "[!] No clients registered." + RESET + "\n";
        else if (Product.class.equals(type))
            return YELLOW + "[!] No products available in the inventory." + RESET + "\n";
        else if (Ticket.class.equals(type))
            return YELLOW + "[!] No tickets found." + RESET + "\n";
        else
            return MSG_NOTHING_TO_SHOW + "\n";
    }

    //Prints a header based on the object type.
    private static String typeHeader(Object element) {
        if (element instanceof Cashier)
            return MSG_NOTHING_TO_SHOW + "\n";
        else if (element instanceof Client)
            return CYAN + "--- Client info ---" + RESET + "\n";
        else if (element instanceof Product)
            return CYAN + "--- Product info ---" + RESET + "\n";
        else if (element instanceof Ticket)
            return CYAN + "--- Ticket info ---" + RESET + "\n";
        else
            return CYAN + "--- Ticket info ---" + RESET + "\n";
    }

}
