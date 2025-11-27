package etsisi.upm.io;

import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.lang.reflect.Field;
import java.util.Arrays;
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
            sb.append(buildTable(collection, itemType));
            return sb;
        }

        sb.append(buildTable(java.util.List.of(element), element.getClass()));
        return sb;
    }

    private static String buildTable(Collection<?> collection, Class<?> itemType) {
        StringBuilder sb = new StringBuilder();

        Field[] fields = itemType.getDeclaredFields();
        for (Field f :fields) f.setAccessible(true);
        //we calculate the widths
        int[] colWidths = new int[fields.length];
        for (int i = 0; i <fields.length; i++)
            colWidths[i] = fields[i].getName().length();
        for (Object obj :collection) {
            for (int i = 0; i < fields.length;i++) {
                try {
                    Object value = fields[i].get(obj);
                    int len = value!=null?value.toString().length(): 4; //for null
                    if (len >colWidths[i]) colWidths[i] = len;
                } catch (Exception ignored) {}
            }
        }
        
        String title = " " + itemType.getSimpleName().toLowerCase() + " ";
        int totalWidth = Arrays.stream(colWidths).sum()+fields.length * 3 + 1;

        //header
        sb.append(YELLOW)
                .append("┌")
                .append(centerTitle(totalWidth - 2, title))
                .append("┐")
                .append("\n");

        //columns names
        sb.append("│ ");
        for (int i = 0; i <fields.length; i++) {
            sb.append(String.format("%-" + colWidths[i] + "s │ ", fields[i].getName()));
        }
        sb.append("\n");

        //separator
        sb.append("├");
        for (int width :colWidths) {
            sb.append("─".repeat(width +2)).append("┼");
        }
        sb.setLength(sb.length()- 1); //the last one out
        sb.append("\n");

        //rows
        sb.append(CYAN);
        for (Object obj : collection) {
            sb.append("│ ");
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value =fields[i].get(obj);
                    String val = value != null ? value.toString() : "null";
                    sb.append(String.format("%-" + colWidths[i] + "s │ ", val));
                } catch (Exception e) {
                    sb.append("ERROR │ ");
                }
            }
            sb.append("\n");
        }

        //footer
        sb.append(CYAN)
                .append("└")
                .append("─".repeat(totalWidth - 2))
                .append("┘")
                .append(RESET)
                .append("\n");

        return sb.toString();
    }

    private static String centerTitle(int i, String title) {
        int padding = (i-title.length())/2;
        return "─".repeat(padding) +title +"─".repeat(i - padding - title.length());
    }


    //Prints custom empty message based on class type, reflexive
    private static String emptyMessage(Class<?> type) {
        if (type == null)
            return MSG_NOTHING_TO_SHOW + "\n";
        return YELLOW + "[!] No " + type.getSimpleName().toLowerCase() + "s found." + RESET;
    }
}
