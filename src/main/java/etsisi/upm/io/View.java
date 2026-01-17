package etsisi.upm.io;

import etsisi.upm.util.Constants;
import etsisi.upm.models.Ticket;
import java.util.*;
import static etsisi.upm.util.Constants.*;

/*The View class is responsible for presenting data to the user via CLI.
 * it receives Model objects and formats them into a readable table structure.
 * this class STRICTLY adheres to MVC principles by relying on Presentable (INTERFACE)
 * and avoiding reflection, ensuring no coupling with the model's internal structure.
 * Assumes: KV class (Key-Value) is defined externally and a Presentable interface*/
public class View {
    private View(){}
    /*Entry point for printing any element, it handles collections/arrays and singular objects.
     *it delegates the conversion to KV pairs before building the final table*/
    public static <T> String getString(T element, String command) {
        if (element == null)
            return MSG_NOTHING_TO_SHOW + "\n";

        List<List<KV>> allKV = new ArrayList<>();
        String tableTitle = "Items";
        //handle Collections
        if (element instanceof Collection<?> col) {
            if (col.isEmpty())
                return emptyMessage(null) + "\n";
            for (Object o : col)//use objectToKV to process each element in the collection
                allKV.addAll(objectToKV(o, command));
            if (col.iterator().hasNext()) //get the simple class name of the first element for the table title
                tableTitle = col.iterator().next().getClass().getSimpleName();

        }//handle Arrays
        else if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0)
                return emptyMessage(null) + "\n";
            for (int i = 0; i < length; i++)
                allKV.addAll(objectToKV(java.lang.reflect.Array.get(element, i), command));
            tableTitle = element.getClass().getComponentType().getSimpleName();
        }//handle single objts.
        else {
            allKV.addAll(objectToKV(element, command));
            tableTitle = element.getClass().getSimpleName();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(buildTableFromKV(allKV, tableTitle));
        //we append the OK status message
        sb.append(Constants.okStatus(command.split(" ")[0], command.split(" ")[1])).append("\n");
        return sb.toString();
    }

    /*Converts a Model object into a list of Key-Value pairs, ready for table drawing.
     *This method is the core of the delegation process WITH PRESENTABLE.*/
    private static List<List<KV>> objectToKV(Object obj, String command) {
        List<List<KV>> result = new ArrayList<>();
        if (obj == null) return result;
        //SIMPLE TYPES (not Presentable)
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj.getClass().isEnum()) {
            List<KV> kvs = new ArrayList<>();
            kvs.add(new KV("value", obj.toString()));
            result.add(kvs);
            return result;
        }
        //TICKETS (IS A SPECIAL CASE BECAUSE REQUIRES DETAILED SHOWN OF PRODUCTS)
        if (obj instanceof Ticket<?> ticket) {
            //Check if we are viewing a list or printing a detailed ticket.
            boolean isFilteredList = command.startsWith("cash tickets") || command.startsWith("ticket list");
            if (isFilteredList) //we use the resume
                result.add(ticket.toViewKVListSummary());
            else
                result.add(ticket.toViewKVList());
            if (!isFilteredList) {
                //detailed breakdown of the products
                for (Map.Entry<? extends Sellable, Integer> entry : ticket.getList().entrySet()) {
                    Sellable p = (Sellable) entry.getKey();
                    int quantity = entry.getValue();
                    List<KV> prodKV = ticket.getDetailedKVsForProductLine(p, quantity);
                    result.add(prodKV);
                }
            }
            return result;
        }
        //general case: POJOSSSS
        //if the object isnt a Ticket BUTTT implements Presentable, we delegate.
        if (obj instanceof Presentable p) {
            result.add(p.toViewKVList());
            return result;
        }
        //CASE OF MAPS AND COLLECTIONS ->>>> (Recursive iteration 4 structure)
        if (obj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> e : map.entrySet()) {
                List<KV> kvs = new ArrayList<>();
                kvs.add(new KV("Key", e.getKey().toString()));
                kvs.add(new KV("Value", e.getValue().toString()));
                result.add(kvs);
            }
            return result;
        }
        if (obj instanceof Collection<?> col) {
            for (Object o : col) //it use objectToKV recursively to handle Presentable elements within the collection
                result.addAll(objectToKV(o, command));
            return result;
        }
        //if it comes here, object is unpresentable
        return result;
    }

    /*Builds the final formatted table string, graphical representation logic of the View.*/
    private static String buildTableFromKV(List<List<KV>> allLinesKV, String title) {
        StringBuilder sb = new StringBuilder();
        //we collect all unique keys to form column headers in order
        Set<String> keySet = new LinkedHashSet<>();
        for (List<KV> line : allLinesKV)
            for (KV kv : line)
                keySet.add(kv.key);

        List<String> keys = new ArrayList<>(keySet);
        //we determine column widths based on the header and content length, DYNAMIC TABLE
        int[] colWidths = new int[keys.size()];
        for (int i = 0; i < keys.size(); i++)
            colWidths[i] = keys.get(i).length();
        for (List<KV> line : allLinesKV) {
            for (int i = 0; i < keys.size(); i++) {
                String val = "-";
                for (KV kv : line) {
                    if (kv.key.equals(keys.get(i)))
                        val = kv.value;
                }
                if (val.length() > colWidths[i])
                    colWidths[i] = val.length();
            }
        }
        //we calculate total width and then, draw the title
        int totalWidth = Arrays.stream(colWidths).sum() + keys.size() * 3 + 1;
        sb.append(YELLOW).append("┌").append(centerTitle(totalWidth - 2, title)).append("┐\n");
        //headers
        sb.append("│ ");
        for (String key : keys)
            sb.append(String.format("%-" + colWidths[keys.indexOf(key)] + "s │ ", key));
        sb.append("\n");
        //separator
        sb.append("├");
        for (int w : colWidths)
            sb.append("─".repeat(w + 2)).append("┼");
        sb.setLength(sb.length() - 1);
        sb.append("\n");
        //rows
        sb.append(CYAN);
        for (List<KV> line : allLinesKV) {
            sb.append("│ ");
            for (String key : keys) {
                String val = "-";
                for (KV kv : line) {
                    if (kv.key.equals(key))
                        val = kv.value;
                }
                //WE USE WRAPSTRING() TO ENSURE THAT LONG VALUES DON'T BREAK THE STRUCTURE OF THE TABLE
                sb.append(String.format("%-" + colWidths[keys.indexOf(key)] + "s │ ", wrapString(val, 30).replace("\n", " ")));
            }
            sb.append("\n");
        }
        //bottom border of the table
        sb.append(CYAN).append("└").append("─".repeat(totalWidth - 2)).append("┘").append(RESET).append("\n");
        return sb.toString();
    }

    /*Helper method to wrap long strings to prevent structure breakage*/
    private static String wrapString(String s, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < s.length()) {
            sb.append(s, index, Math.min(index + maxWidth, s.length())).append("\n");
            index += maxWidth;
        }
        return sb.toString().trim();
    }

    /*Helper method to center the title string for the table header.*/
    private static String centerTitle(int width, String title) {
        int padding = (width - title.length()) / 2;
        return "─".repeat(padding) + title + "─".repeat(width - padding - title.length());
    }

    /*Helper method to generate an empty message.*/
    private static String emptyMessage(Class<?> type) {
        if (type == null) return MSG_NOTHING_TO_SHOW + "\n";
        return YELLOW + "[!] No " + type.getSimpleName().toLowerCase() + "s found." + RESET;
    }
}