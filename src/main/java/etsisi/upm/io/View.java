package etsisi.upm.io;

import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.util.Collection;

public class View {
    //colours ANSI
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    //messages
    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;

    //Here we print an object w/ toString()
    public static <T> StringBuilder print(T element) {
        StringBuilder sb = new StringBuilder();
        if (element == null) {
            sb.append(MSG_NOTHING_TO_SHOW).append("\n");
            return sb;
        }
        //for collections
        if (element instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                sb.append(emptyMessage(null)).append("\n");
                return sb;
            }
            //for knowing the first type of the element
            Class<?> itemType = collection.iterator().next().getClass();
            for (Object item : collection) {
                sb.append(typeHeader(itemType));
                sb.append(item).append("\n");
            }
            return sb;
        }

        //for arrays
        if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0) {
                sb.append(emptyMessage(null)).append("\n");
                return sb;
            }
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(element, i);
                sb.append(typeHeader(item.getClass()));
                sb.append(item).append("\n");
            }
            return sb;
        }

        //normal object
        sb.append(typeHeader(element.getClass()));
        sb.append(element).append("\n");
        return sb;
    }


    //Prints custom empty message based on class type, reflexive
    private static String emptyMessage(Class<?> type) {
        if (type == null)
            return MSG_NOTHING_TO_SHOW + "\n";
        return YELLOW + "[!] No " + type.getSimpleName().toLowerCase() + "s found." + RESET;
    }

    //Prints a header based on the object type.
    private static String typeHeader(Class<?> type) {
        if (type == null)
            return MSG_NOTHING_TO_SHOW + "\n";
        return CYAN + type.getSimpleName().toLowerCase() + RESET + "\n";
    }
}
