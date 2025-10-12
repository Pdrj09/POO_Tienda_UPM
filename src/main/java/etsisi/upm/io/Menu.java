package etsisi.upm.io;

import etsisi.upm.controllers.Controller;

public class Menu {

    /// Global variables
    private final Controller controller;  //gobal variable called controller

    // status code
    private static final int QUERY_SUCCESS = 1;
    private static final int QUERY_EXIT = 0;

    // numbers
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;

    /// messages and help
    //This is the welcome message it is printed when you start the program
    private static final String WELCOME_MESSAGE = """
            Welcome to the ticket module App.
            Ticket module. Type 'help' to see commands.
        """;

    //this is printed when you call 'help'
    private static final String COMMANDS_LIST = """
            Commands:
              prod add <id> "<name>" <category> <price>
              prod list
              prod update <id> NAME|CATEGORY|PRICE <value>
              prod remove <id>
              ticket new
              ticket add <prodId><quantity>
              ticket remove <prodId>
              ticket print
              echo "<texto>"
              help
              exit
            
             Categories: MERCH,STATIONERY,CLOTHES,BOOK,ELECTRONICS
             Discounts if there are ≥2 units in the category: MERCH 0%,STATIONERY 5%,CLOTHES 7%,BOOK 10%,
             ELECTRONICS 3%.
            """;
    //printed when you exit the program
    private static final String BYE = """
            Closing application.
            Goodbye!
            """;

    private static final String OK_STATUS = "ok";  //Ok
    private static final String ERROR_STATUS = "Error"; //error

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
    private static final String STR_ERROR = "Error";
    private static final String STR_BLANK_SPACE = " ";
    private static final String STR_DOUBLE_DOT = ":";

    // regex const
    private static final String REGEX_INIT = "^";
    private static final String REGEX_BLANK_SPACE = "\\s*";
    private static final String REGEX_DOUBLE_QUOTE = "\"";
    private static final String REGEX_TO_SPLIT = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";


    // products const
    private static final String PRODUCT_ADD = "add";
    private static final String PRODUCT_LIST = "list";
    private static final String PRODUCT_UPDATE = "update";
    private static final String PRODUCT_REMOVE = "remove";

    // ticket const
    private static final String TICKET_ADD = "add";
    private static final String TICKET_PRINT = "print";
    private static final String TICKET_NEW = "new";
    private static final String TICKET_REMOVE = "remove";

    public Menu() {
        System.out.println(WELCOME_MESSAGE);
        this.controller = new Controller();
    }

    public int newQuery(String query) {
        //Check if the query starts with PROD command keyword
        if (query.startsWith(PROD)) {
            this.prodQuery(deleteSubstring(query, createGeneralRegex(PROD)));
            //if the query starts with TICKET, we handle using ticketQuery().
        } else if (query.startsWith(TICKET)) {
            this.ticketQuery(deleteSubstring(query, createGeneralRegex(TICKET)));
            //if query starts with ECHO,it echoes back the input
        } else if (query.startsWith(ECHO)) {
            this.echoCommand(query);
            //if query starts with HELP, displays help information available
        } else if (query.startsWith(HELP)) {
            this.help();
            //if query starts with EXIT prints goodbye message
        } else if (query.startsWith(EXIT)) {
            System.out.println(BYE);
            return QUERY_EXIT;
            //returns 0
        }

        return QUERY_SUCCESS;
        //returns 1
    }

    private void prodQuery(String query) {
        if (query.contains(PRODUCT_ADD)) {
            try {

                String[] querySplit = query.split(REGEX_TO_SPLIT);

                int id = Integer.parseInt(querySplit[ONE]);
                String name = querySplit[TWO].replace(REGEX_DOUBLE_QUOTE, STR_EMPTY);

                float price = Float.parseFloat(querySplit[FOUR].replace(STR_COMMA, STR_DOT));


                String response = controller.addProduct(name, querySplit[THREE], price, id);
                System.out.println(response);

                if (!response.startsWith(STR_ERROR)) {
                    System.out.println(okStatus(PROD, PRODUCT_ADD));
                }
            } catch (Exception e) {
                System.out.println(errorStatus(PROD, PRODUCT_ADD, e.toString()));
            }

        } else if (query.contains(PRODUCT_LIST)) {
            try {

                System.out.println(controller.prodList());

                System.out.println(okStatus(PROD, PRODUCT_LIST));
            } catch (Exception e) {
                System.out.println(errorStatus(PROD, PRODUCT_ADD, e.toString()));
            }

        } else if (query.contains(PRODUCT_REMOVE)) {
            try {
                int id = Integer.parseInt(deleteSubstring(query, createGeneralRegex(PRODUCT_REMOVE)));


                String deletedProd = controller.deleteProduct(id);

                if (deletedProd != null) {
                    System.out.println(deletedProd);
                    System.out.println(okStatus(PROD, PRODUCT_REMOVE));
                } else {
                    System.out.println(errorStatus(PROD, PRODUCT_REMOVE));
                }

            } catch (Exception e) {
                System.out.println(errorStatus(PROD, PRODUCT_REMOVE, e.toString()));
            }
        } else if (query.contains(PRODUCT_UPDATE)) {
            try {
                String[] querySplit = query.split(REGEX_TO_SPLIT);

                int id = Integer.parseInt(querySplit[ONE]);


                String productEdited = controller.updateProduct(id, querySplit[TWO], querySplit[THREE]);

                if (productEdited != null) {
                    System.out.println(productEdited);
                    System.out.println(okStatus(TICKET, TICKET_NEW));
                } else {
                    System.out.println(errorStatus(TICKET, TICKET_NEW));
                }
            } catch (Exception e) {
                System.out.println(errorStatus(PROD, PRODUCT_UPDATE, e.toString()));
            }
        }
    }

    private void ticketQuery(String query) {
        if (query.contains(TICKET_ADD)) {

            String[] querySplit = query.split(REGEX_TO_SPLIT);

            int id = Integer.parseInt(querySplit[ONE]);

            int quantity = Integer.parseInt(querySplit[TWO]);


            String newTicket = controller.addProductToTicket(id, quantity);

            if (newTicket != null) {
                System.out.println(newTicket);
                System.out.println(okStatus(TICKET, TICKET_ADD));
            } else {
                System.out.println(errorStatus(TICKET, TICKET_ADD));
            }

        } else if (query.contains(TICKET_NEW)) {

            controller.ticketNew();
            System.out.println(okStatus(TICKET, TICKET_NEW));


        } else if (query.contains(TICKET_PRINT)) {

            System.out.println(controller.ticketPrint());
            System.out.println(okStatus(TICKET, TICKET_PRINT));

        } else if (query.contains(TICKET_REMOVE)) {
            int id = Integer.parseInt(deleteSubstring(query, createGeneralRegex(PRODUCT_REMOVE)));

            if (controller.removeProductFromTicket(id)) {
                System.out.println(okStatus(TICKET, TICKET_REMOVE));
            } else {
                System.out.println(errorStatus(TICKET, TICKET_REMOVE));
            }
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

    private String okStatus(String type, String comand) {
        StringBuilder builder;
        builder = new StringBuilder();

        builder.append(type)
                .append(STR_BLANK_SPACE)
                .append(comand)
                .append(STR_DOUBLE_DOT)
                .append(STR_BLANK_SPACE)
                .append(OK_STATUS);

        return builder.toString();
    }

    private String errorStatus(String type, String comand, String message) {
        StringBuilder builder;
        builder = new StringBuilder();

        builder.append(type)
                .append(STR_BLANK_SPACE)
                .append(comand)
                .append(STR_DOUBLE_DOT)
                .append(STR_BLANK_SPACE)
                .append(ERROR_STATUS)
                .append(STR_DOUBLE_DOT)
                .append(STR_BLANK_SPACE)
                .append(message);

        return builder.toString();
    }

    private String errorStatus(String type, String comand) {
        StringBuilder builder = new StringBuilder();

        builder.append(type)
                .append(STR_BLANK_SPACE)
                .append(comand)
                .append(STR_DOUBLE_DOT)
                .append(STR_BLANK_SPACE)
                .append(ERROR_STATUS);

        return builder.toString();
    }
}
