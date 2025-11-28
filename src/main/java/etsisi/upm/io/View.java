package etsisi.upm.io;

import etsisi.upm.Constants;

import java.util.Collection;

public class View {
    //colours ANSI
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    private static final String MSG_NOTHING_TO_SHOW = YELLOW + "[!] No items to display." + RESET;

    public static <T> String print(T element) {
        StringBuilder builder = new StringBuilder();

        if (element == null) {
            builder.append(MSG_NOTHING_TO_SHOW).append(Constants.ENTER_KEY);
            return builder.toString();
        }

        //collections
        if (element instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                builder.append(MSG_NOTHING_TO_SHOW);
                builder.append(Constants.ENTER_KEY);
                return builder.toString();
            }
            for (Object item : collection) {
                builder.append(item.toString()).append(Constants.ENTER_KEY);
            }
            return builder.toString();
        }

        //arrays
        if (element.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(element);
            if (length == 0) {
                builder.append(MSG_NOTHING_TO_SHOW).append(Constants.ENTER_KEY);
                return builder.toString();
            }
            for (int i = 0; i < length; i++) {
                Object item = java.lang.reflect.Array.get(element, i);

                builder.append(item.toString());
                builder.append(Constants.ENTER_KEY);
            }
            return builder.toString();
        }

        //single object
        builder.append(element).append(Constants.ENTER_KEY);
        return builder.toString();
    }

    //reflexive headers

}
