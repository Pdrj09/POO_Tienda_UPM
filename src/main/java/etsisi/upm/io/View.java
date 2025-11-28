package etsisi.upm.io;

import java.lang.reflect.Field;
import java.util.*;

public class View {
    // colores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    // mensajes
    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;

    // Clase mínima para key-value de strings
    private static class KV {
        public String key;
        public String value;
        public KV(String key, String value) { this.key = key; this.value = value; }
    }

    // Método principal
    public static <T> String getString(T element) {
        if (element == null) return MSG_NOTHING_TO_SHOW + "\n";

        // Colecciones
        if (element instanceof Collection<?> col) {
            if (col.isEmpty()) return emptyMessage(null) + "\n";
            return buildTable(col, col.iterator().next().getClass());
        }

        // Arrays
        if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0) return emptyMessage(null) + "\n";
            List<Object> arrList = new ArrayList<>();
            for (int i = 0; i < length; i++) arrList.add(java.lang.reflect.Array.get(element, i));
            return buildTable(arrList, arrList.get(0).getClass());
        }

        // Strings
        if (element instanceof String s) {
            s = s.trim();
            StringBuilder sb = new StringBuilder();
            String[] lines = s.split("\n");
            boolean anyKV = false;
            List<List<KV>> allLinesKV = new ArrayList<>();

            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("{") && line.endsWith("}")) {
                    anyKV = true;
                    String content = line.substring(1, line.length() - 1);
                    String[] parts = content.split(",(?=(?:[^']*'[^']*')*[^']*$)");
                    List<KV> kvs = new ArrayList<>();
                    for (String p : parts) {
                        String[] kv = p.split(":", 2);
                        if (kv.length == 2) kvs.add(new KV(kv[0].trim(), kv[1].trim()));
                    }
                    allLinesKV.add(kvs);
                } else {
                    sb.append(CYAN).append("String").append(RESET).append(": ").append(wrapString(line, 50)).append("\n");
                }
            }

            // Construir tabla si hay KV
            if (anyKV) sb.append(buildTableFromKV(allLinesKV));
            return sb.toString();
        }

        // Objeto individual
        return buildTable(Collections.singletonList(element), element.getClass());
    }

    // Construir tabla de objetos
    private static String buildTable(Collection<?> collection, Class<?> itemType) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = Arrays.stream(itemType.getDeclaredFields())
                .filter(f -> !java.lang.reflect.Modifier.isStatic(f.getModifiers()))
                .toArray(Field[]::new);
        for (Field f : fields) f.setAccessible(true);

        // Calcular ancho de columnas
        int[] colWidths = new int[fields.length];
        for (int i = 0; i < fields.length; i++) colWidths[i] = fields[i].getName().length();
        for (Object obj : collection) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    int len = value != null ? value.toString().length() : 4;
                    if (len > colWidths[i]) colWidths[i] = len;
                } catch (Exception ignored) {}
            }
        }

        String title = " " + itemType.getSimpleName().toLowerCase() + " ";
        int totalWidth = Arrays.stream(colWidths).sum() + fields.length * 3 + 1;

        // Header
        sb.append(YELLOW).append("┌").append(centerTitle(totalWidth - 2, title)).append("┐\n");

        // Column names
        sb.append("│ ");
        for (int i = 0; i < fields.length; i++) sb.append(String.format("%-" + colWidths[i] + "s │ ", fields[i].getName()));
        sb.append("\n");

        // Separator
        sb.append("├");
        for (int w : colWidths) sb.append("─".repeat(w + 2)).append("┼");
        sb.setLength(sb.length() - 1);
        sb.append("\n");

        // Rows
        sb.append(CYAN);
        for (Object obj : collection) {
            sb.append("│ ");
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    String val = value != null ? value.toString() : "-";
                    sb.append(String.format("%-" + colWidths[i] + "s │ ", wrapString(val, 30).replace("\n", " ")));
                } catch (Exception e) {
                    sb.append("ERROR │ ");
                }
            }
            sb.append("\n");
        }

        // Footer
        sb.append(CYAN).append("└").append("─".repeat(totalWidth - 2)).append("┘").append(RESET).append("\n");
        return sb.toString();
    }

    // Construir tabla de KV
    private static String buildTableFromKV(List<List<KV>> allLinesKV) {
        StringBuilder sb = new StringBuilder();

        // Obtener todas las claves
        Set<String> keySet = new LinkedHashSet<>();
        for (List<KV> line : allLinesKV) for (KV kv : line) keySet.add(kv.key);

        List<String> keys = new ArrayList<>(keySet);
        int[] colWidths = new int[keys.size()];
        for (int i = 0; i < keys.size(); i++) colWidths[i] = keys.get(i).length();
        for (List<KV> line : allLinesKV) {
            for (int i = 0; i < keys.size(); i++) {
                String val = "-";
                for (KV kv : line) if (kv.key.equals(keys.get(i))) val = kv.value;
                if (val.length() > colWidths[i]) colWidths[i] = val.length();
            }
        }

        String title = " KV ";
        int totalWidth = Arrays.stream(colWidths).sum() + keys.size() * 3 + 1;

        // Header
        sb.append(YELLOW).append("┌").append(centerTitle(totalWidth - 2, title)).append("┐\n");
        sb.append("│ ");
        for (int i = 0; i < keys.size(); i++) sb.append(String.format("%-" + colWidths[i] + "s │ ", keys.get(i)));
        sb.append("\n");

        // Separator
        sb.append("├");
        for (int w : colWidths) sb.append("─".repeat(w + 2)).append("┼");
        sb.setLength(sb.length() - 1);
        sb.append("\n");

        // Rows
        sb.append(CYAN);
        for (List<KV> line : allLinesKV) {
            sb.append("│ ");
            for (int i = 0; i < keys.size(); i++) {
                String val = "-";
                for (KV kv : line) if (kv.key.equals(keys.get(i))) val = kv.value;
                sb.append(String.format("%-" + colWidths[i] + "s │ ", wrapString(val, 30).replace("\n", " ")));
            }
            sb.append("\n");
        }

        // Footer
        sb.append(CYAN).append("└").append("─".repeat(totalWidth - 2)).append("┘").append(RESET).append("\n");
        return sb.toString();
    }

    // Wrap strings largos
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
