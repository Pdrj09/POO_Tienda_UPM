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


    public void menu() {
        System.out.println(WELCOME_MESSAGE);
    }

    public int newQuery(String query) {
        if (query.startsWith(PROD)) {

        } else if (query.startsWith(TICKET)) {

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

    }

    private void ticketQuery(String query) {
    }

    private void help() {
        System.out.println(COMMANDS_LIST);
    }

    private void echoCommand(String command) {
        System.out.println(command);
    }
}
