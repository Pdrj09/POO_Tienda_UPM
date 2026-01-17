package etsisi.upm.util;

import etsisi.upm.models.repositories.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {

    private Utilities(){};

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

    //for cleaning "name" and 'name'
    public static String cleanName(String rawName) {
        if (rawName == null) return null;
        if (rawName.startsWith("\"") && rawName.endsWith("\"") && rawName.length() >= 2)
            return rawName.substring(1, rawName.length() - 1);
        if (rawName.startsWith("'") && rawName.endsWith("'") && rawName.length() >= 2)
            return rawName.substring(1, rawName.length() - 1);
        return rawName;
    }

    public static boolean isPositiveInteger(String s) {
        try {
            int value = Integer.parseInt(s);
            if (value < Constants.BASE_PROD_ID) {
                throw  new IllegalArgumentException(Constants.NOT_VALID_ID);
            }else {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }
    public static <T> int generateAutomaticId(Repository<Integer, T> repository) {
        int id = Constants.BASE_PROD_ID;

        while (repository.findById(id) != null) {
            id++;
        }

        if (id < Constants.BASE_PROD_ID) {
            throw new IllegalArgumentException("You have reached the max number of elements");
        }

        return id;
    }
}
