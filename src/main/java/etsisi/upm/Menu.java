package etsisi.upm;

public class Menu {
    public void Menu () {
        System.out.println("Welcome to the ticket module App.\nTicket module. Type 'help' to see commands.");
    }

    public void newQuery(String query) {
        help();
    }

    private void help() {
        System.out.println("Commands:");
        System.out.println("\tprod add <id> \"<name>\" <category> <price>");
        System.out.println("\tprod list");
        System.out.println("\tprod update <id> NAME|CATEGORY|PRICE <value>");
        System.out.println("\tprod remove <id>");
        System.out.println("\tticket new");
        System.out.println("\tticket add <prodId> <quantity>");
        System.out.println("\tticket remove <prodId>");
        System.out.println("\tticket print");
        System.out.println("\techo \"<texto>\"");
        System.out.println("\thelp");
        System.out.println("\texit");
    }

    private void echoCommand(String command) {
        System.out.println(command);
    }
}
