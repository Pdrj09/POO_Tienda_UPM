package etsisi.upm.io;

public class Menu {

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

    private static final String EXIT = "exit";
    private static final String PROD =  "prod";
    private static final String TICKET =  "ticket";
    private static final String ECHO =  "echo";
    private static final String HELP = "help";

    private static final String TICKET_ADD =  "add";
    private static final String TICKET_PRINT =  "print";
    private static final String TICKET_NEW = "new";
    private static final String TICKET_REMOVE = "remove";


    private static final String PRODUCT_ADD =  "add";
    private static final String PRODUCT_LIST =  "list";
    private static final String PRODUCT_UPDATE = "update";
    private static final String PRODUCT_REMOVE = "remove";

    public void menu() {
        System.out.println(WELCOME_MESSAGE);
    }

    public int newQuery(String query) {
        if (query.startsWith(PROD)) {

        } else if (query.startsWith(TICKET)) {
            this.ticketQuery(query);
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

        }else if (query.contains(PRODUCT_LIST)){

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
}
