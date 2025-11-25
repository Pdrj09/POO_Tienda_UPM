package etsisi.upm.io;

import etsisi.upm.Constants;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.util.Collection;

public class ViewCLI {


    /// messages and help
    //This is the welcome message it is printed when you start the program
    private static final String WELCOME_MESSAGE = """
            Welcome to the ticket module App.
            Ticket module. Type 'help' to see commands.
        """;


    //printed when you exit the program
    private static final String BYE = """
            Closing application.
            Goodbye!
            """;


    //this is printed when you call 'help'
    private static final String COMMANDS_LIST = """
            Commands:
                    client add "<nombre>" <DNI> <email> <cashId>
                    client remove <DNI>
                    client list
                    cash add [<id>] "<nombre>"<email>
                    cash remove <id>
                    cash list
                    cash tickets <id>
                    ticket new [<id>] <cashId> <userId>
                    ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]\s
                    ticket remove <ticketId><cashId> <prodId>\s
                    ticket print <ticketId> <cashId>\s
                    ticket list
                    prod add <id> "<name>" <category> <price>
                    prod update <id> NAME|CATEGORY|PRICE <value>
                    prod addMeal <id> "<name>" <price/p> <expiration:yyyy-MM-dd HH:mm> <max_people>
                    prod addMeeting <id> "<name>" <price/p> <expiration:yyyy-MM-dd HH:mm> <max_people>
                    prod list
                    prod remove <id>
                    help
                    echo “<text>”
                    exit
            
            """;


    // TODO DE AQUI PARA ABAJO FUERA
    public static void print(String results){
        System.out.println(results);
    }

    public static void printWellcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    public static void printHelp(){
        System.out.println(COMMANDS_LIST);
    }

    public static void printExit(){
        System.out.println(BYE);
    }

    public static void echoCommand(String command) {
        System.out.println(command);
    }


    //PRINTS FOR CLIENT
    public static void printClient(Client client){
        System.out.println(client.toString());
    }

    public static void printClients(Collection<Client> clients){
        if (clients.isEmpty())
            System.out.println("No clients found.");
        else clients.forEach(ViewCLI::printClient);
    }


    //PRINTS FOR CASHIER
    public static void printCashier(Cashier cashier){
        System.out.println(cashier.toString());
    }

    public static void printCashiers(Collection<Cashier> cashiers){
        if (cashiers.isEmpty())
            System.out.println("No cashiers found.");
        else cashiers.forEach(ViewCLI::printCashier);
    }


    //PRINTS FOR PRODUCTS
    public static void printProduct(Product product){
        System.out.println(product.toString());
    }

    public static void printProducts(Collection<Product> products){
        if (products.isEmpty())
            System.out.println(Constants.ERROR_NO_PRODUCTS_FOUND);
        else products.forEach(ViewCLI::printProduct);
    }


    //PRINTS FOR TICKETS
    public static void printTicket (Ticket ticket){
        if (ticket == null) {
            System.out.println("Ticket not found.");
        } else {
            System.out.println(ticket.toString());
        }
    }

    public static void printTickets(Collection<Ticket> tickets){
        if (tickets.isEmpty())
            System.out.println("No products found.");
        else tickets.forEach(ViewCLI::printTicket);
    }
}
