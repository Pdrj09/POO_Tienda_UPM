package etsisi.upm;

public class Constants {


    public static final int QUERY_SUCCESS = 1;
    public  static final int QUERY_EXIT = 0;

    public static final int QUERY_HELP_POS_INSTRUCTION = 1;

    public static final int QUERY_TICKET_ADD_LENGHT_WITHID = 4;
    public static final int QUERY_TICKET_ADD_LENGHT_WITHOUTID = 3;
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

    public static final int QUERY_CASH_POS_INSTRUCTION = 1;
    public static final int QUERY_CASH_POS_ID = 2;
    public static final int QUERY_CASH_POS_NAME = 3;
    public static final int QUERY_CASH_POS_EMAIL = 4;
    public static final int QUERY_CASH_LENGTH_WITHID = 5;
    public static final int QUERY_CASH_LENGTH_WITHOUTID = 4;

    // numbers
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int HUNDRED = 100;


    public static final int NON_SIZE = -1;
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
    public static final String STR_BLANK_SPACE = " ";
    public static final String STR_DOUBLE_DOT = ":";
    public static final String ENTER_KEY = "\n";
    public static final String TAB_SPACE = "\t";
    public static final String COMMA_SPACE = ", ";
    public static final String PERCENTAGE = "%";
    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String QUOTE = "'";
    public static final String HYPEN = "-";
    public static final String ID_FORMAT = "%05d";
    public static final int MAX_RANDOM = 100_000;


    public static final String OK_STATUS = "ok";  //Ok
    public static final String ERROR_STATUS = "Error"; //error

    public static final String IN_THE_FUTURE = " in the future";

    public static final String DATETIME_FORMAT_WH = "yy-MM-dd-HH:mm";
    public static final String DATETIME_FORMAT= "dd-MM-yy HH:mm";

    public static final String STR_FOOD = "class:Food";
    public static final String STR_MEETING = "class:Meeting";
    public static final String STR_PRICE_PERSON = ", pricePerPerson:";
    public static final String STR_EXPIRATION = ", expiration:";
    public static final String STR_SERVICE_PRODUCT = "class:ServiceProduct";

    public static final String NAME = "NAME";
    public static final String CATEGORY = "CATEGORY";
    public static final String PRICE = "PRICE";
    public static final String CATALOG = "Catalog:\n";

    // regex const
    public static final String REGEX_INIT = "^";
    public static final String REGEX_BLANK_SPACE = "\\s*";
    public static final String REGEX_DOUBLE_QUOTE = "\"";
    public static final String REGEX_TO_SPLIT = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String REGEX_PERSONALIZED = "(?<=--p)";
    public static final String REGEX_CASH_ID = "^UW\\d{7}$";
    public static final String NIE_REGEX = "^[XYZxyz]\\d{7}[A-Za-z]$";

    // products const
    public static final String PRODUCT = "prod";
    public static final String PRODUCT_ADD = "add";
    public static final String PRODUCT_LIST = "list";
    public static final String PRODUCT_UPDATE = "update";
    public static final String PRODUCT_REMOVE = "remove";
    public static final String PRODUCT_ADD_FOOD = "addFood";
    public static final String PRODUCT_ADD_MEETING = "addMeeting";
    public static final String STR_PRODUCT = "class:Product";
    public static final String STR_PROD_NAME = ", name:'";
    public static final String STR_PROD_ID = ", id:";
    public static final String STR_CATEGORY = ", category:";
    public static final String STR_PRICE = ", price:";


    // ticket const
    public static final String TICKET_ADD = "add";
    public static final String TICKET_PRINT = "print";
    public static final String TICKET_NEW = "new";
    public static final String TICKET_REMOVE = "remove";
    public static final String TICKET_LIST = "list";
    public static final int MAX_SIZE_TICKET = 100;
    public static final double EXTRA_PRICE_PERSONALIZATIONS = 0.1;
    public static final int MIN_FOR_DISCOUNT = 1;
    public static final String DISCOUNT = "**discount -";
    public static final String TOTAL_PRICE = "\nTotal price: ";
    public static final String TOTAL_DISCOUNT = "\nTotal discount: ";
    public static final String FINAL_PRICE = "\nFinal price: ";

    // client const
    public static final String CLIENT_ADD = "add";
    public static final String CLIENT_REMOVE = "remove";
    public static final String CLIENT_LIST = "list";
    public static final String CLI_CATEGORIES = "Categories: ";
    public static final String CLI_DISCOUNT ="Discounts if there are ≥2 units in the category: ";
    public static final String STR_CLIENT = "class:Client";
    public static final String STR_DNI = ", dni:'";
    public static final String STR_CLI_NAME = ", name:'";
    public static final String STR_CLIENT_EMAIL = ", email:'";
    public static final String STR_CASH = ", cashCreatorId:'";
    public static final String DNI_REGEX = "\\d{8}";
    public static final String DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    public static final int ALPHABET_NUM = 23;



    //cash const
    public static final String CASH_ADD = "add";
    public static final String CASH_REMOVE = "remove";
    public static final String CASH_LIST = "list";
    public static final String CASH_TICKETS = "tickets";
    public static final String CASHIER_PREFIX = "UW";
    public static final String CASH_REGEX = "%s%07d";
    public static final String STR_CASHIER = "class:Cashier";
    public static final String STR_ID = ", id:";
    public static final String STR_NAME = ", name:";
    public static final String STR_CASH_EMAIL = ", emailCompany:";
    public static final String STR_TICKETS = ", tickets:";

    // Time const
    public static final int TIME_FOOD_PLANNING_DAYS = 3;
    public static final int TIME_MEETING_PLANNING_HOURS = 12;
    public static final int TIME_MAX_PEOPLE_SERVICE = 100;
    public static final String HOURS = "hours";
    public static final String DAYS = "days";


    //Error const
    public static final String ERROR_DATE = "date wrong";
    public static final String ERROR_NO_PRODUCTS_FOUND  = "No products found";
    public static final String DUPLICATED_ID_ERROR  = "The id given already exists please try again using a new one";
    public static final String ERROR_MAXSIZE = "Repository full, maximum size of ";
    public static final String ERROR_SERVICE_DATE_FEASIBILITY = "Error: The date must be at least ";
    public static final int QUERY_CLIENT_POS_MAXARGS = 6;
    public static final String ERROR_NO_CLIENTS_FOUND = "No clients found";
    public static final int QUERY_CLIENT_POS_INSTRUCTION = 1;
    public static final int QUERY_CLIENT_POS_CLASS = 0;
    public static final int QUERY_CASH_POS_CLASS = 0;
    public static final String ERROR_MAXSIZE_TICKET = "Ticket filled, maximum size of ";
    public static final String ERROR_NONPERSONALIZABLE = "You cant personalice a product that is not personalizable";
    public static final String ERROR_ZERO_AMOUNT = "The min amount to add is 1";
    public static final String ERROR_SERVICE_ALREADY_EXIST = "the same service can't be added twice in the same ticket.";
    public static final String ERROR_INVALID_SERVICE_PEOPLE_1 = "The number of participants (";
    public static final String ERROR_INVALID_SERVICE_PEOPLE_2 = ") isn't valid for this service.";

    public static final String ERROR_CREATE_PRODUCT = "Error creating product, category does not exist";
    public static final String ERROR_DELETE_PRODUCT = "Error deleting product";
    public static final String ERROR_ID_NONEXISTENT = "The id passed as a parameter does not exist";

    public static final String ERROR_ID_EMPTY = "The ID cannot be empty.";
    public static final String ERROR_NAME_EMPTY = "The name cannot be empty.";
    public static final String ERROR_EMAIL_EMPTY = "The email cannot be empty.";
    public static final String ERROR_MAIL_FORMAT = "please enter a valid email address";

    public static final String ERROR_DNI_LENGTH = "Invalid DNI: wrong length (must be 9 chars)";
    public static final String ERROR_CASHIER_NULL = "The cashier ID cannot be null";
    public static final String ERROR_DNI_DIGITS = "Invalid DNI: first 8 characters must be numbers";
    public static final String ERROR_INVALID_DNI_1 = "Invalid DNI: wrong letter. Expected ";
    public static final String ERROR_INVALID_DNI_2= " for ";
    public static final String ERROR_NIE_FORMAT = "please introduce a correct ID document (NIE/NIF)";

    public static final String ERROR_PRICE = "No price for product";
    public static final String ERROR_INVALID_ID = "UW format incorrect";

    public static final String ERROR_TOOMANY_ARGUMENTS = "too many arguments";
    public static final String ERROR_TOOMANY_PEOPLE = "Invalid  max people number";

    public static final String ERROR_INVALID_OPTION = "invalid option";
    public static final String ERROR_FEW_PARAMS = "more params required";
    public static final String ERROR_NONEXISTEN_ID = "id nonexistent";
    public static final String ERROR_FILE_NOTFOUND = "file not found";

    //private regex
    private static final String MAIL_REGEX = "^[^@]+@[^@]+\\.[A-Za-z]{2,}$";


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

    public static boolean checkEmail(String email) {
        return email.matches(MAIL_REGEX);
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
