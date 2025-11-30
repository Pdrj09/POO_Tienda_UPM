package etsisi.upm.util;

import etsisi.upm.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {
    public static double round(double value) {
        long factor = (long) Math.pow(10, 2); //2 decimals
        value *= factor;
        long tmp = Math.round(value);
        return (double) tmp/factor;
    }

    public static String formatDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_WH);
        return date.format(formatter);
    }
}
