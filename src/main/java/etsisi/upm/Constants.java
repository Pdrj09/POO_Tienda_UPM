package etsisi.upm;

public class Constants {


    public static final int QUERY_SUCCESS = 1;
    public  static final int QUERY_EXIT = 0;

    public static final int QUERY_TICKET_POS_INSTRUCTION = 0;
    public static final int QUERY_TICKET_POS_TICKETID = 1;
    public static final int QUERY_TICKET_POS_CASHID = 2;
    public static final int QUERY_TICKET_POS_PRODID = 3;
    public static final int QUERY_TICKET_POS_USERID = 3;
    public static final int QUERY_TICKET_POS_AMOUNT = 4;
    public static final int QUERY_TICKET_POS_CUSTOMIZATIONS = 5;

    public static final int QUERY_PRODUCT_POS_INSTRUCTION = 0;
    public static final int QUERY_PRODUCT_POS_PRODUCTID = 1;
    public static final int QUERY_PRODUCT_POS_NAME = 2;
    public static final int QUERY_PRODUCT_POS_CATEGORY = 3;
    public static final int QUERY_PRODUCT_POS_PRICE = 4;
    public static final int QUERY_PRODUCT_POS_MAXPERS = 5;
    public static final int QUERY_PRODUCT_POS_FIELD = 2;
    public static final int QUERY_PRODUCT_POS_NEWCONTENT = 3;
    public static final int QUERY_PRODUCT_POS_PRICE_FOODMEETING = 3;
    public static final int QUERY_PRODUCT_POS_EXPIRATION = 4;
    public static final int QUERY_PRODUCT_POS_MAXPEOPLE = 5;
    // numbers
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int NINE = 9;

    public static final int MAX_SIZE = 200;

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
    public static final String ENTER_KEY = "\n";


    public static final String OK_STATUS = "ok";  //Ok
    public static final String ERROR_STATUS = "Error"; //error

    public static final String ERROR_INVALID_OPTION = "invalid option";
    public static final String ERROR_FEW_PARAMS = "more params required";
    public static final String ERROR_NONEXISTEN_ID = "id nonexistent";
    public static final String ERROR_FILE_NOTFOUND = "file not found";
    public static final String ERROR_PRICE = "No price for product";
    public static final String ERROR_INVALID_ID = "UW format incorrect";
    // regex const
    public static final String REGEX_INIT = "^";
    public static final String REGEX_BLANK_SPACE = "\\s*";
    public static final String REGEX_DOUBLE_QUOTE = "\"";
    public static final String REGEX_TO_SPLIT = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String REGEX_PERSONALIZED = "(?<=--p)";
    public static final String REGEX_CASH_ID = "^\\UW{7}$";

    // products const
    public static final String PRODUCT = "prod";
    public static final String PRODUCT_ADD = "add";
    public static final String PRODUCT_LIST = "list";
    public static final String PRODUCT_UPDATE = "update";
    public static final String PRODUCT_REMOVE = "remove";
    public static final String PRODUCT_ADD_FOOD = "addFood";
    public static final String PRODUCT_ADD_MEETING = "addMeeting";


    // ticket const
    public static final String TICKET_ADD = "add";
    public static final String TICKET_PRINT = "print";
    public static final String TICKET_NEW = "new";
    public static final String TICKET_REMOVE = "remove";
    public static final String TICKET_LIST = "list";

    // client const
    public static final String CLIENT_ADD = "add";
    public static final String CLIENT_REMOVE = "remove";
    public static final String CLIENT_LIST = "list";

    //cash const
    public static final String CASH_ADD = "add";
    public static final String CASH_REMOVE = "remove";
    public static final String CASH_LIST = "list";
    public static final String CASH_TICKETS = "tickets";

    // Time const
    public static final int TIME_FOOD_PLANNING_DAYS = 3;
    public static final int TIME_MEETING_PLANNING_HOURS = 12;
    public static final int TIME_MAX_PEOPLE_SERVICE = 100;


    //Error const
    public static final String ERROR_NO_PRODUCTS_FOUND  = "No products found";
    public static final String DUPLICATED_ID_ERROR  = "The id given already exists please try again using a new one";

    public static String createGeneralRegex(String query) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();

        stringBuilder.append(Constants.REGEX_INIT)
                .append(query)
                .append(Constants.REGEX_BLANK_SPACE);

        return stringBuilder.toString();
    }
    public static String okStatus(String type, String comand) {
        StringBuilder builder;
        builder = new StringBuilder();

        builder.append(type)
                .append(Constants.STR_BLANK_SPACE)
                .append(comand)
                .append(Constants.STR_DOUBLE_DOT)
                .append(Constants.STR_BLANK_SPACE)
                .append(Constants.OK_STATUS);

        return builder.toString();
    }


    public static String errorStatus(String type, String comand) {
        StringBuilder builder = new StringBuilder();

        builder.append(type)
                .append(Constants.STR_BLANK_SPACE)
                .append(comand)
                .append(Constants.STR_DOUBLE_DOT)
                .append(Constants.STR_BLANK_SPACE)
                .append(Constants.ERROR_STATUS);

        return builder.toString();
    }


    public static String  errorStatus(String type, String comand, String message) {
        StringBuilder builder;
        builder = new StringBuilder();

        builder.append(type)
                .append(Constants.STR_BLANK_SPACE)
                .append(comand)
                .append(Constants.STR_DOUBLE_DOT)
                .append(Constants.STR_BLANK_SPACE)
                .append(Constants.ERROR_STATUS)
                .append(Constants.STR_DOUBLE_DOT)
                .append(Constants.STR_BLANK_SPACE)
                .append(message);

        return (builder.toString());
    }
    public static String deleteSubstring(String query, String regex) {
        return query.replaceFirst(regex, Constants.STR_EMPTY);
    }

}
