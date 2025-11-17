package etsisi.upm;

public class Constants {


    public static final int QUERY_SUCCESS = 1;
    public  static final int QUERY_EXIT = 0;

    // numbers
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;

    // menu const
    public static final String EXIT = "exit";
    public static final String PROD = "prod";
    public static final String TICKET = "ticket";
    public static final String ECHO = "echo";
    public static final String HELP = "help";
    public static final String CLIENT = "client";
    public static final String CASH = "cash";

    // str const
    public static final String STR_EMPTY = "";
    public static final String STR_DOT = ".";
    public static final String STR_COMMA = ",";
    public static final String STR_ERROR = "Error";
    public static final String STR_BLANK_SPACE = " ";
    public static final String STR_DOUBLE_DOT = ":";



    public static final String OK_STATUS = "ok";  //Ok
    public static final String ERROR_STATUS = "Error"; //error


    // regex const
    public static final String REGEX_INIT = "^";
    public static final String REGEX_BLANK_SPACE = "\\s*";
    public static final String REGEX_DOUBLE_QUOTE = "\"";
    public static final String REGEX_TO_SPLIT = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String REGEX_PERSONALIZED = "(?<=--p)";


    // products const
    public static final String PRODUCT_ADD = "add";
    public static final String PRODUCT_LIST = "list";
    public static final String PRODUCT_UPDATE = "update";
    public static final String PRODUCT_REMOVE = "remove";

    // ticket const
    public static final String TICKET_ADD = "add";
    public static final String TICKET_PRINT = "print";
    public static final String TICKET_NEW = "new";
    public static final String TICKET_REMOVE = "remove";

    // client const
    public static final String CLIENT_ADD = "add";
    public static final String CLIENT_REMOVE = "remove";
    public static final String CLIENT_LIST = "list";


    public static String createGeneralRegex(String query) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();

        stringBuilder.append(Constants.REGEX_INIT)
                .append(query)
                .append(Constants.REGEX_BLANK_SPACE);

        return stringBuilder.toString();
    }

}
