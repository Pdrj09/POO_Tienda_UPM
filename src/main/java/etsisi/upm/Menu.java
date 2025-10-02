package etsisi.upm;

public class Menu {

    private static final int QUERY_SUCCESS = 0;

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

    public void menu() {
        System.out.println(WELCOME_MESSAGE);
    }

    public int newQuery(String query) {
        help();
        return QUERY_SUCCESS;
    }

    private void help() {
        System.out.println(COMMANDS_LIST);
    }

    private void echoCommand(String command) {
        System.out.println(command);
    }
}
