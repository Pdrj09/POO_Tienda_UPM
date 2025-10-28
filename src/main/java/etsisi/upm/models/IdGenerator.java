package etsisi.upm.models;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {
    //set tokeep track of used IDS
    private static final Set<String> usedIds = new HashSet<>();
    private static final Random RANDOM = new Random(); //for generate a random id

    //for cashier
    private static final int CASHIER_ID_LENGHT = 7;
    private static final String CASHIER_PREFIX = "UW";

    //CONSTANTS FOR FORMAT
    private static final String CASH_REGEX = "%s%07d";


    /*Generates a unique ID for a Cashier in the format UW + 7 digits.
    * ENSURES UNIQUENESSS,
    * returns de unique ID*/
    public static String generateCashierId(){
        String id;
        do{
            int num = RANDOM.nextInt(10_000_000); //range between 0 and 9.999.999
            id = String.format(CASH_REGEX, CASHIER_PREFIX, num);
        }while(usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    //MANUAL REGISTRATION OF AN ID TO AVOID DUPLICATES
    public static void registerId(String id){
        usedIds.add(id);
    }

    //CHECKS IF AN ID HAS ALREADY BEEN USED
    public static boolean isUsed(String id){
        return usedIds.contains(id);
    }

}
