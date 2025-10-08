package etsisi.upm.io;

import etsisi.upm.controllers.ProductsController;
import etsisi.upm.models.Product;

public class Menu {

    private ProductsController prodController;

    private static final int QUERY_SUCCESS = 0;
    private static final int QUERY_EXIT = 1;


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

    // MENU CONST
    private static final String EXIT = "exit";
    private static final String PROD = "prod";
    private static final String TICKET = "ticket";
    private static final String ECHO = "echo";
    private static final String HELP = "help";

    // REGEX CONST
    private static final String REGEX_INIT = "^";
    private static final String REGEX_BLANK_SPACE = "\\s*";
    private static final String REGEX_TO_SPLIT = " (?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

    // PRODUCTS CONST
    private static final String PRODUCT_ADD =  "add";
    private static final String PRODUCT_LIST =  "list";
    private static final String PRODUCT_UPDATE = "update";
    private static final String PRODUCT_REMOVE = "remove";

    // TICKET CONST
    private static final String TICKET_ADD = "add";
    private static final String TICKET_PRINT = "print";
    private static final String TICKET_NEW = "new";
    private static final String TICKET_REMOVE = "remove";

    // TODO use controlers
    public void menu() {
        System.out.println(WELCOME_MESSAGE);
        this.prodController = new ProductsController();
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
        System.out.println(query);
        if (query.contains(PRODUCT_ADD)){

            String[] querySplit = query.split(REGEX_TO_SPLIT);

            int id = Integer.parseInt(querySplit[1]);

            // Quitar comillas del nombre
            String nombre = querySplit[2].replace("\"", "");

            // Reemplazar coma por punto para convertirlo a float
            float precio = Float.parseFloat(querySplit[3].replace(",", "."));



        }else if (query.contains(PRODUCT_LIST)){

            System.out.println(prodController.toString());

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
        return query.replaceFirst(regex, "");
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
