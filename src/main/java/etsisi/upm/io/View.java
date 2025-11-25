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
    private static final String MSG_NULL_ELEMENT = RED + "[X] Element not found." + RESET;

    private static final String MSG_SUCCESS_PREFIX = GREEN + "[OK]" + RESET + " ";
    private static final String MSG_ERROR_PREFIX = RED + "[ERROR]" + RESET + " ";
    private static final String MSG_INFO_PREFIX = CYAN + "[INFO]" + RESET + " ";

    //Here we print an object w/ toString()
    public static <T> void print(T element, Class<?> type) {
        if (element == null) {
            printEmptyMessage(type);
            return;
        }

        //for collections
        if (element instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                printEmptyMessage(type);
                return;
            }
            for (Object item : collection)
                print(item, item.getClass());
            return;
        }

        //for arrays
        if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0) {
                printEmptyMessage(type);
                return;
            }
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(element, i);
                print(item, item.getClass());
            }
            return;
        }

        //normal object
        System.out.println(MSG_INFO_PREFIX + element.toString());
    }


    //Prints custom empty message based on class type
    private static void printEmptyMessage(Class<?> type) {
        if (type == null)
            System.out.println(MSG_NOTHING_TO_SHOW);
        else if (Cashier.class.equals(type))
            noCashiersFound();
        else if (Client.class.equals(type))
            noClientsFound();
        else if (Product.class.equals(type))
            noProductsFound();
        else if (Ticket.class.equals(type))
            noTicketsFound();
        else
            System.out.println(MSG_NOTHING_TO_SHOW);
    }

    //Prints a header based on the object type.
    private static void printTypeHeader(Object element) {
        if (element instanceof Cashier)
            System.out.println(CYAN + "--- Cashier info ---" + RESET);
        else if (element instanceof Client)
            System.out.println(CYAN + "--- Client info ---" + RESET);
        else if (element instanceof Product)
            System.out.println(CYAN + "--- Product info ---" + RESET);
        else if (element instanceof Ticket)
            System.out.println(CYAN + "--- Ticket info ---" + RESET);
        else
            System.out.println(CYAN + "--- Item ---" + RESET);
    }

    /**custom messages for specific things*/
    /*Prints a message when no cashiers are found. */
    public static void noCashiersFound(){
        System.out.println(YELLOW + "[!] No cashiers registered." + RESET);
    }

    /*Prints a message when no clients are found. */
    public static void noClientsFound(){
        System.out.println(YELLOW + "[!] No clients registered." + RESET);
    }

    /*Prints a message when no products exist in the system. */
    public static void noProductsFound(){
        System.out.println(YELLOW + "[!] No products available in the inventory." + RESET);
    }

    /*Prints a message when no tickets exist in the system. */
    public static void noTicketsFound(){
        System.out.println(YELLOW + "[!] No tickets found." + RESET);
    }

}
