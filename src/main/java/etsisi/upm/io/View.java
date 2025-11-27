package etsisi.upm.io;

import java.util.Collection;

public class View {
    //colours ANSI
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;
    private static final String MSG_INFO_PREFIX = CYAN + "[INFO]" + RESET + " ";

    public static <T> String print(T element) {
        StringBuilder sb = new StringBuilder();

        if (element == null) {
            sb.append(MSG_NOTHING_TO_SHOW).append("\n");
            return sb.toString();
        }

        //collections
        if (element instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                sb.append(MSG_NOTHING_TO_SHOW).append("\n");
                return sb.toString();
            }
            for (Object item : collection) {
                sb.append(typeHeader(item));
                sb.append(MSG_INFO_PREFIX).append(item.toString()).append("\n");
            }
            return sb.toString();
        }

        //arrays
        if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0) {
                sb.append(MSG_NOTHING_TO_SHOW).append("\n");
                return sb.toString();
            }
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(element, i);
                sb.append(typeHeader(item));
                sb.append(MSG_INFO_PREFIX).append(item.toString()).append("\n");
            }
            return sb.toString();
        }

        //single object
        sb.append(typeHeader(element));
        sb.append(MSG_INFO_PREFIX).append(element.toString()).append("\n");
        return sb.toString();
    }

    //reflexive headers
    private static String typeHeader(Object element) {
        if (element == null) return MSG_NOTHING_TO_SHOW + "\n";
        String className = element.getClass().getSimpleName();
        return CYAN + "--- " + className + " info ---" + RESET + "\n";
    }
}
