package etsisi.upm.io;

import etsisi.upm.Constants;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class View {
    // ANSI colours
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    // Messages
    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;

    // Clase para el formato (la mantenemos aquí, es parte de la presentación)
    public static class KV {
        public String key;
        public String value;
        public KV(String key, String value) { this.key = key; this.value = value; }
    }

    // Main printing method
    public static <T> String getString(T element, String command) {
        if (element == null)
            return MSG_NOTHING_TO_SHOW + "\n";

        List<List<KV>> allKV = new ArrayList<>();
        String tableTitle = "Items";

        if (element instanceof Collection<?> col) {
            if (col.isEmpty())
                return emptyMessage(null) + "\n";
            for (Object o : col) {
                allKV.addAll(objectToKV(o, command));
            }
            if (col.iterator().hasNext()) {
                tableTitle = col.iterator().next().getClass().getSimpleName();
            }
        } else if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0)
                return emptyMessage(null) + "\n";
            for (int i = 0; i < length; i++)
                allKV.addAll(objectToKV(java.lang.reflect.Array.get(element, i), command));
            tableTitle = element.getClass().getComponentType().getSimpleName();
        } else if (element instanceof Ticket ticket) {
            allKV.addAll(objectToKV(ticket, command));
            tableTitle = "Ticket";
        } else {
            allKV.addAll(objectToKV(element, command));
            tableTitle = element.getClass().getSimpleName();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(buildTableFromKV(allKV, tableTitle));
        sb.append(Constants.okStatus(command.split(" ")[0], command.split(" ")[1])).append("\n");
        return sb.toString();
    }

    // Método principal para convertir objeto a Key-Value pairs
    private static List<List<KV>> objectToKV(Object obj, String command) {
        List<List<KV>> result = new ArrayList<>();
        if (obj == null) return result;

        // TIPOS SIMPLES
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj.getClass().isEnum()) {
            List<KV> kvs = new ArrayList<>();
            kvs.add(new KV("value", obj.toString()));
            result.add(kvs);
            return result;
        }

        // PRODUCTOS
        if (obj instanceof Product p) {
            // DELEGACIÓN: El producto sabe qué KVs debe mostrar.
            // Asume que Product implementa public List<KV> toViewKVList()
            result.add(p.toViewKVList());
            return result;
        }

        // TICKETS (Lógica para desglosar productos en la tabla)
        if (obj instanceof Ticket ticket) {
            // Condición de filtrado ampliada para incluir 'cash tickets' y 'ticket list'
            boolean isFilteredList = command.startsWith("cash tickets") || command.startsWith("ticket list");

            // KVs del Ticket (Fila de totales)
            List<KV> kvs = new ArrayList<>();
            kvs.add(new KV("id", ticket.getId()));
            kvs.add(new KV("state", ticket.getState().name()));

            if (!isFilteredList) {
                kvs.add(new KV("closeDate", ticket.getCloseDateFormatted()));
                kvs.add(new KV("totalPrice", String.valueOf(ticket.getTotalPriceView())));
                kvs.add(new KV("totalDiscount", String.valueOf(ticket.getTotalDiscountView())));
                kvs.add(new KV("finalPrice", String.valueOf(ticket.getFinalPriceView())));
            }
            result.add(kvs);

            if (!isFilteredList) {
                // Productos dentro del ticket (Itera una vez por producto único en el mapa)
                for (Map.Entry<Product, List<Object>> entry : ticket.getList().entrySet()) {
                    Product p = entry.getKey();
                    int quantity = (int) entry.getValue().get(0);

                    // *** DELEGACIÓN DE CÁLCULO ***
                    // Obtenemos el descuento ya CALCULADO y REDONDEADO del Modelo.
                    // Asume que Ticket.java tiene getDiscountPerUnit(p)
                    double discount = ticket.getDiscountPerUnit(p);

                    // 1. Obtener los KVs del producto base (delegado)
                    List<KV> prodKV = new ArrayList<>(p.toViewKVList());

                    // 2. Añadir/Sobrescribir los campos específicos del ticket (quantity, discount)
                    prodKV.add(new KV("quantity", String.valueOf(quantity)));
                    prodKV.add(new KV("discount", String.valueOf(discount)));

                    result.add(prodKV);
                }
            }
            return result;
        }

        // MAPS y COLLECTIONS: Utilizamos una estructura similar a la reflexión
        // pero idealmente deberíamos eliminar esta lógica de parsing de KVs.
        if (obj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> e : map.entrySet()) {
                List<KV> kvs = new ArrayList<>();
                kvs.add(new KV("key", e.getKey().toString()));
                kvs.add(new KV("value", e.getValue().toString()));
                result.add(kvs);
            }
            return result;
        }
        if (obj instanceof Collection<?> col) {
            for (Object o : col) {
                result.addAll(objectToKV(o, command));
            }
            return result;
        }

        // REFLEXIÓN: Bloque que DEBE ELIMINARSE en un MVC estricto, ya que acopla.
        // Se mantiene temporalmente para otros objetos no manejados.
        List<KV> kvs = new ArrayList<>();
        try {
            for (Field f : getAllFields(obj.getClass())) {
                f.setAccessible(true);
                Object value = f.get(obj);
                kvs.add(new KV(f.getName(), value != null ? value.toString() : "-"));
            }
        } catch (Exception ignored) {}
        result.add(kvs);
        return result;
    }

    // ... (buildTableFromKV y métodos de utilidad como getAllFields, centerTitle, etc.
    //      se mantienen porque su trabajo es el dibujado de la tabla, que es
    //      responsabilidad de la VISTA)

    private static String buildTableFromKV(List<List<KV>> allLinesKV, String title) {
        // ... código de buildTableFromKV (sin cambios)
        StringBuilder sb = new StringBuilder();

        Set<String> keySet = new LinkedHashSet<>();
        for (List<KV> line : allLinesKV)
            for (KV kv : line)
                keySet.add(kv.key);

        List<String> keys = new ArrayList<>(keySet);
        int[] colWidths = new int[keys.size()];
        for (int i = 0; i < keys.size(); i++)
            colWidths[i] = keys.get(i).length();
        for (List<KV> line : allLinesKV) {
            for (int i = 0; i < keys.size(); i++) {
                String val = "-";
                for (KV kv : line) if (kv.key.equals(keys.get(i))) val = kv.value;
                if (val.length() > colWidths[i])
                    colWidths[i] = val.length();
            }
        }

        int totalWidth = Arrays.stream(colWidths).sum() + keys.size() * 3 + 1;
        sb.append(YELLOW).append("┌").append(centerTitle(totalWidth - 2, title)).append("┐\n");

        sb.append("│ ");
        for (String key : keys)
            sb.append(String.format("%-" + colWidths[keys.indexOf(key)] + "s │ ", key));
        sb.append("\n");

        sb.append("├");
        for (int w : colWidths)
            sb.append("─".repeat(w + 2)).append("┼");
        sb.setLength(sb.length() - 1);
        sb.append("\n");

        sb.append(CYAN);
        for (List<KV> line : allLinesKV) {
            sb.append("│ ");
            for (String key : keys) {
                String val = "-";
                for (KV kv : line) if (kv.key.equals(key)) val = kv.value;
                sb.append(String.format("%-" + colWidths[keys.indexOf(key)] + "s │ ", wrapString(val, 30).replace("\n", " ")));
            }
            sb.append("\n");
        }

        sb.append(CYAN).append("└").append("─".repeat(totalWidth - 2)).append("┘").append(RESET).append("\n");
        return sb.toString();
    }

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        List<String> hiddenFields = List.of(
                "createdTickets", "associatedClients", "associatedTickets",
                "personalizable", "maxPers", "list", "categories"
        );
        while (type != null && type != Object.class) {
            for (Field f : type.getDeclaredFields()) {
                int mods = f.getModifiers();
                if (Modifier.isStatic(mods)) continue;
                if (hiddenFields.contains(f.getName())) continue;
                if (f.getName().equals(f.getName().toUpperCase())) continue;
                fields.add(f);
            }
            type = type.getSuperclass();
        }
        return fields;
    }

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