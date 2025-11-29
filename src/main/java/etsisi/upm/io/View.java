package etsisi.upm.io;

import etsisi.upm.Constants;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class View {
    //ANSI colours
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    //messages
    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;

    //a class for formating toString
    private static class KV {
        public String key;
        public String value;
        public KV(String key, String value) { this.key = key; this.value = value; }
    }

    //principal method for printing Objects
    public static <T> String getString(T element, String command) {
        StringBuilder sb = new StringBuilder();
        if (element == null)
            return MSG_NOTHING_TO_SHOW + "\n";

        //for ticket (it is a special case)
        if (element instanceof Ticket ticket) {
            //we create a list KW for this special case so we can print the table
            List<KV> ticketKV = new ArrayList<>();
            ticketKV.add(new KV("id", ticket.getId()));
            ticketKV.add(new KV("state", ticket.getState().name()));
            ticketKV.add(new KV("closeDate", ticket.getCloseDateFormatted()));
            ticketKV.add(new KV("totalPrice", String.valueOf(ticket.getTotalPriceView())));
            ticketKV.add(new KV("totalDiscount", String.valueOf(ticket.getTotalDiscountView())));
            ticketKV.add(new KV("finalPrice", String.valueOf(ticket.getFinalPriceView())));

            //list of lists
            sb.append(buildTableFromKV(Collections.singletonList(ticketKV), Constants.TICKET));

            //now we print the products inside the ticket
            if (!ticket.getList().isEmpty()) {
                sb.append(CYAN + "\nProducts inside the ticket:\n" + RESET);

                List<List<KV>> allProductsKV = new ArrayList<>();
                for (Map.Entry<Product, List<Object>> entry : ticket.getList().entrySet()) {
                    Product p = entry.getKey();
                    int quantity = (int) entry.getValue().get(0);
                    double discount = 0.0;
                    if (ticket.getCategories().getOrDefault(p.getCategory(), 0) > 1)
                        discount = p.getPrice() * p.getCategory().getDiscount();

                    List<KV> productKV = new ArrayList<>();
                    productKV.add(new KV("id", String.valueOf(p.getId())));
                    productKV.add(new KV("name", p.getName()));
                    productKV.add(new KV("category", String.valueOf(p.getCategory())));
                    productKV.add(new KV("price", String.valueOf(p.getPrice())));
                    productKV.add(new KV("quantity", String.valueOf(quantity)));
                    productKV.add(new KV("discount", String.valueOf(discount)));

                    allProductsKV.add(productKV);
                }
                sb.append(buildTableFromKV(allProductsKV, Constants.PRODUCT));
            }
            sb.append(Constants.okStatus(command.split(" ")[0], command.split(" ")[1])).append("\n");
            return sb.toString();
        }

        //for collections
        if (element instanceof Collection<?> col) {
            if (col.isEmpty())
                return emptyMessage(null) + "\n";
            sb.append(buildTable(col, col.iterator().next().getClass()));
        }
        //arrays case
        else if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0)
                return emptyMessage(null) + "\n";
            List<Object> arrList = new ArrayList<>();
            for (int i = 0; i < length; i++)
                arrList.add(java.lang.reflect.Array.get(element, i));
            sb.append(buildTable(arrList, arrList.get(0).getClass()));
        }
        //for maps (this case for printing the tickets from cashier), future improve: reflexive access to the name
        else if (element instanceof Map<?, ?> map) {
            if (map.isEmpty())
                return MSG_NOTHING_TO_SHOW + "\n";
            StringBuilder sbStr = new StringBuilder();
            //We convert the entries to a string that our KV regex knows to print the table with recursion
            for (Map.Entry<?, ?> e : map.entrySet()) {
                sbStr.append("{ class:")
                        .append("Ticket")
                        .append(", id: ")
                        .append(String.valueOf(e.getKey()))
                        .append(", state: ")
                        .append(String.valueOf(e.getValue()))
                        .append(" }\n");
            }
            //recursive call after the parse
            return getString(sbStr.toString(), command);
        }
        //for stirngs
        else if (element instanceof String s) {
            s = s.trim();
            String[] lines = s.split("\n");
            boolean anyKV = false;
            List<List<KV>> allLinesKV = new ArrayList<>();
            String tableTitle = "Items"; //default name
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("{") && line.endsWith("}")) {
                    anyKV = true;

                    //here we try to extract the name of the class
                    //it search for "class:NAME" (toString sintsxis)
                    int classIndex = line.indexOf("class:");
                    if (classIndex != -1) {
                        int commaIndex = line.indexOf(",", classIndex);
                        if (commaIndex == -1)
                            commaIndex = line.length() - 1;
                        tableTitle = line.substring(classIndex + 6, commaIndex).trim();
                    }


                    String content = line.substring(1, line.length() - 1);
                    String[] parts = content.split(",(?=(?:[^']*'[^']*')*[^']*$)");
                    List<KV> kvs = new ArrayList<>();
                    for (String p : parts) {
                        String[] kv = p.split(":", 2);
                        if (kv.length == 2 && !kv[0].trim().equals("class"))
                            kvs.add(new KV(kv[0].trim(), kv[1].trim()));
                    }
                    allLinesKV.add(kvs);
                } else
                    sb.append(CYAN).append(tableTitle).append(RESET).append(": ").append(wrapString(line, 50)).append("\n");
            }
            if (anyKV)
                sb.append(buildTableFromKV(allLinesKV, tableTitle));
        } else
            sb.append(buildTable(Collections.singletonList(element), element.getClass()));
        sb.append(Constants.okStatus(command.split(" ")[0], command.split(" ")[1])).append("\n");
        return sb.toString();
    }


    //we build the table
    private static String buildTable(Collection<?> collection, Class<?> itemType) {
        StringBuilder sb = new StringBuilder();
        List<Field> fields = getAllFields(itemType);

        for (Field f : fields)
            f.setAccessible(true);

        //width
        int[] colWidths = new int[fields.size()];
        for (int i = 0; i < fields.size(); i++)
            colWidths[i] = fields.get(i).getName().length();

        for (Object obj : collection) {
            for (int i = 0; i < fields.size(); i++) {
                try {
                    Object value = fields.get(i).get(obj);
                    int len;
                    if (value != null)
                        len = value.toString().length();
                    else
                        len = 1;
                    if (len > colWidths[i])
                        colWidths[i] = len;
                } catch (Exception ignored) {}
            }
        }

        String title = "♥ " + itemType.getSimpleName().toLowerCase() + " ♥";
        int totalWidth = Arrays.stream(colWidths).sum() + fields.size() * 3 + 1;

        //HEADER
        sb.append(YELLOW)
                .append("┌").append(centerTitle(totalWidth - 2, title)).append("┐\n");

        //Column names
        sb.append("│ ");
        for (int i = 0; i < fields.size(); i++)
            sb.append(String.format("%-" + colWidths[i] + "s │ ", fields.get(i).getName()));
        sb.append("\n");

        //Separator
        sb.append("├");
        for (int w : colWidths)
            sb.append("─".repeat(w + 2)).append("┼");
        sb.setLength(sb.length() - 1);
        sb.append("\n");

        //ROWS
        sb.append(CYAN);
        for (Object obj : collection) {
            sb.append("│ ");
            for (int i = 0; i < fields.size(); i++) {
                try {
                    Object value = fields.get(i).get(obj);
                    String val = value != null ? value.toString() : "-";
                    sb.append(String.format("%-" + colWidths[i] + "s │ ",
                            wrapString(val, 30).replace("\n", " ")
                    ));
                } catch (Exception e) {
                    sb.append("ERROR │ ");
                }
            }
            sb.append("\n");
        }

        //FOOTER
        sb.append(CYAN)
                .append("└").append("─".repeat(totalWidth - 2)).append("┘")
                .append(RESET).append("\n");

        return sb.toString();
    }

    //table for strings (k,v)
    private static String buildTableFromKV(List<List<KV>> allLinesKV, String title) {
        StringBuilder sb = new StringBuilder();

        Set<String> keySet = new LinkedHashSet<>();
        for (List<KV> line : allLinesKV) {
            for (KV kv : line)
                keySet.add(kv.key);
        }

        List<String> keys = new ArrayList<>(keySet);
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

        int totalWidth = Arrays.stream(colWidths).sum() + keys.size() * 3 + 1;
        //Header
        sb.append(YELLOW).append("┌").append(centerTitle(totalWidth - 2, title)).append("┐\n");
        sb.append("│ ");
        for (int i = 0; i < keys.size(); i++)
            sb.append(String.format("%-" + colWidths[i] + "s │ ", keys.get(i)));
        sb.append("\n");

        //Separator
        sb.append("├");
        for (int w : colWidths)
            sb.append("─".repeat(w + 2)).append("┼");
        sb.setLength(sb.length() - 1);
        sb.append("\n");

        //Rows
        sb.append(CYAN);
        for (List<KV> line : allLinesKV) {
            sb.append("│ ");
            for (int i = 0; i < keys.size(); i++) {
                String val = "-";
                for (KV kv : line) {
                    if (kv.key.equals(keys.get(i)))
                        val = kv.value;
                }
                sb.append(String.format("%-" + colWidths[i] + "s │ ", wrapString(val, 30).replace("\n", " ")));
            }
            sb.append("\n");
        }

        //Footer
        sb.append(CYAN).append("└").append("─".repeat(totalWidth - 2)).append("┘").append(RESET).append("\n");
        return sb.toString();
    }

    //get parent fields, ignore constants
    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();

        //Fields that must NEVER be shown in the tables
        List<String> hiddenFields = List.of(
                "createdTickets",
                "associatedClients",
                "associatedTickets",
                "personalizable",
                "maxPers",
                "list",
                "categories"
        );
        while (type != null && type != Object.class) {
            for (Field f : type.getDeclaredFields()) {
                int mods = f.getModifiers();
                if (Modifier.isStatic(mods))
                    continue;
                if (hiddenFields.contains(f.getName()))
                    continue;
                if (f.getName().equals(f.getName().toUpperCase()))
                    continue;
                fields.add(f);
            }
            type = type.getSuperclass();
        }
        return fields;
    }

    //Wrap long strings
    private static String wrapString(String s, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < s.length()) {
            sb.append(s, index, Math.min(index + maxWidth, s.length())).append("\n");
            index += maxWidth;
        }
        return sb.toString().trim();
    }

    private static String centerTitle(int width, String title) {
        int padding = (width - title.length()) / 2;
        return "─".repeat(padding) + title + "─".repeat(width - padding - title.length());
    }

    private static String emptyMessage(Class<?> type) {
        if (type == null) return MSG_NOTHING_TO_SHOW + "\n";
        return YELLOW + "[!] No " + type.getSimpleName().toLowerCase() + "s found." + RESET;
    }
}
