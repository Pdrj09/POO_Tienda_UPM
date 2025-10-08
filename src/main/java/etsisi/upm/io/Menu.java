package etsisi.upm.io;

import etsisi.upm.controllers.Controller;
import etsisi.upm.models.Product;

public class Menu {

    private Controller controller;

    // status code
    private static final int QUERY_SUCCESS = 0;
    private static final int QUERY_EXIT = 1;

    // numbers
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;


    // messages and help
    private static final String WELCOME_MESSAGE =
            "Welcome to the ticket module App.\nTicket module. Type 'help' to see commands.";

    private static final String COMMANDS_LIST = """
        Commands:
            prod add <id> "<name>" <category> <price>
            prod list
            prod update <id> NAME|CATEGORY|PRICE <value>
            prod remove <id>
            ticket new
            ticket add <prodId> <quantity>
            ticket remove <prodId>
            ticket print
            echo "<texto>"
            help
            exit
            Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%,
                ELECTRONICS 3%.
        """;

    private static final String BYE = """
            Closing application.
            Goodbye!
            """;

    // menu const
    private static final String EXIT = "exit";
    private static final String PROD = "prod";
    private static final String TICKET = "ticket";
    private static final String ECHO = "echo";
    private static final String HELP = "help";

    // str const
    private static final String STR_EMPTY = "";
    private static final String STR_DOT = ".";
    private static final String STR_COMMA = ",";


    // regex const
    private static final String REGEX_INIT = "^";
    private static final String REGEX_BLANK_SPACE = "\\s*";
    private static final String REGEX_DOUBLE_QUOTE = "\"";
    private static final String REGEX_TO_SPLIT = " (?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

    // products const
    private static final String PRODUCT_ADD =  "add";
    private static final String PRODUCT_LIST =  "list";
    private static final String PRODUCT_UPDATE = "update";
    private static final String PRODUCT_REMOVE = "remove";

    // ticket const
    private static final String TICKET_ADD = "add";
    private static final String TICKET_PRINT = "print";
    private static final String TICKET_NEW = "new";
    private static final String TICKET_REMOVE = "remove";

    // TODO use controlers
    public void menu() {
        System.out.println(WELCOME_MESSAGE);
        this.controller = new Controller();
    }

    public int newQuery(String query) {
        if (query.startsWith(PROD)) {

            this.prodQuery(deleteSubstring(query, createGeneralRegex(PROD)));

        } else if (query.startsWith(TICKET)) {

            this.ticketQuery(deleteSubstring(query, createGeneralRegex(TICKET)));

        } else if (query.startsWith(ECHO)) {

            this.echoCommand(query);

        } else if (query.startsWith(HELP)) {

            this.help();

        } else if (query.startsWith(EXIT)) {

            System.out.println(BYE);
            return QUERY_EXIT;
        }


        return QUERY_SUCCESS;
    }

    private void prodQuery(String query) {
        if (query.contains(PRODUCT_ADD)){

            String[] querySplit = query.split(REGEX_TO_SPLIT);

            int id = Integer.parseInt(querySplit[ONE]);
            String name = querySplit[TWO].replace(REGEX_DOUBLE_QUOTE, STR_EMPTY);

            float price = Float.parseFloat(querySplit[FOUR].replace(STR_COMMA, STR_DOT));


            String request = controller.addProduct(name, querySplit[THREE], price, id);
            System.out.println(request);

        }else if (query.contains(PRODUCT_LIST)){

            System.out.println(controller.prodList());

        }else if (query.contains(PRODUCT_REMOVE)){



        }else if(query.contains(PRODUCT_UPDATE)){

        }
    }

    private void ticketQuery(String query) {
        if (query.contains(TICKET_ADD)){

        }else if (query.contains(TICKET_NEW)){

        }else if (query.contains(TICKET_PRINT)){

        }else if(query.contains(TICKET_REMOVE)){

        }
    }

    private void help() {
        System.out.println(COMMANDS_LIST);
    }

    private void echoCommand(String command) {
        System.out.println(command);
    }

    private String deleteSubstring(String query, String regex) {
        return query.replaceFirst(regex, STR_EMPTY);
    }

    private String createGeneralRegex(String query) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();

        stringBuilder.append(REGEX_INIT)
                .append(query)
                .append(REGEX_BLANK_SPACE);

        return stringBuilder.toString();
    }
}
