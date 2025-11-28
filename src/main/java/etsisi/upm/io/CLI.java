package etsisi.upm.io;

import etsisi.upm.controllers.CashierController;
import etsisi.upm.controllers.ClientController;
import etsisi.upm.controllers.ProductController;
import etsisi.upm.controllers.TicketController;
import etsisi.upm.Constants;
import etsisi.upm.models.Product;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

public class CLI {

    /// Global variables
    private final ProductController productController;  //gobal variable called controller
    private final TicketController ticketController;
    private final ClientController clientController;
    private final CashierController cashierController;
    // constants

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
                    prod addFood <id> "<name>" <price/p> <expiration:yyyy-MM-dd HH:mm> <max_people>
                    prod addMeeting <id> "<name>" <price/p> <expiration:yyyy-MM-dd HH:mm> <max_people>
                    prod list
                    prod remove <id>
                    help
                    echo “<text>”
                    exit
            
            """;



    public CLI(ProductController productController, TicketController ticketController, ClientController clientController, CashierController cashierController) {
        printWelcomeMessage();
        this.productController = productController;
        this.ticketController = ticketController;
        this.clientController = clientController;
        this.cashierController = cashierController;
    }

    public int newQuery(String query) {
        //Check if the query starts with PROD command keyword
        if (query.startsWith(Constants.PROD)) {
            this.prodQuery(Constants.deleteSubstring(query, Constants.createGeneralRegex(Constants.PROD)));
            //if the query starts with TICKET, we handle using ticketQuery().
        } else if (query.startsWith(Constants.TICKET)) {
            this.ticketQuery(Constants.deleteSubstring(query, Constants.createGeneralRegex(Constants.TICKET)));
            //if query starts with ECHO,it echoes back the input
        } else if (query.startsWith(Constants.ECHO)) {
            echoCommand(query);
            //if query starts with HELP, displays help information available
        } else if (query.startsWith(Constants.HELP)) {
            printHelp();
            //if query starts with EXIT prints goodbye message
        } else if (query.startsWith(Constants.EXIT)) {
            printExit();
            return Constants.QUERY_EXIT;
            //returns 0
        } else if (query.startsWith(Constants.CLIENT)) {
            this.clientQuery(query);
        } else if (query.startsWith(Constants.CASH)) {
            this.cashQuery(query);
        }
        return Constants.QUERY_SUCCESS;
        //returns 1
    }

    private void clientQuery(String query){
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        try {
            if (query.contains(Constants.CLIENT_ADD)) {
                System.out.println(clientController.clientAddControl(querySplit));
            } else if (query.contains(Constants.CLIENT_REMOVE)) {
                String id = querySplit[1];
                clientController.removeClients(id);
                System.out.println(Constants.okStatus(Constants.CLIENT, Constants.CLIENT_REMOVE));
            } else if (query.contains(Constants.CLIENT_LIST)) {
                //View.toString(clientController.listClients());
                System.out.println(Constants.okStatus(Constants.CLIENT, Constants.CLIENT_LIST));
            }
        } catch (Exception e) {
            System.out.println(Constants.errorStatus(Constants.CLIENT, "Error", e.getMessage()));
        }
    }

    private void prodQuery(String query) {
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        try {
            System.out.println(this.productController.decodeQuery(querySplit));
        }catch (Exception e) {
            System.out.println(Constants.errorStatus(Constants.PROD, e.toString()));
        }
    }

    private void ticketQuery(String query) {
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);

        try {
            System.out.println(this.ticketController.decodeQuery(querySplit));
        }catch (IndexOutOfBoundsException e){
            System.out.println(Constants.errorStatus(Constants.TICKET,Constants.ERROR_STATUS,Constants.ERROR_FEW_PARAMS));
        }catch (Exception e) {
            System.out.println(Constants.errorStatus(Constants.TICKET, Constants.ERROR_STATUS, e.getMessage()));
        }
    }


    private void cashQuery(String query){
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);

        try {
            System.out.println(this.cashierController.cashierQuery(querySplit));
        }catch (IndexOutOfBoundsException _){
            System.out.println(Constants.errorStatus(Constants.CASH, Constants.ERROR_FEW_PARAMS));
        }
        catch (Exception e) {
            System.out.println(Constants.errorStatus(Constants.CASH, e.toString()));
        }
    }

    private static void printWelcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    private static void printHelp(){
        System.out.println(COMMANDS_LIST);
    }

    private static void printExit(){
        System.out.println(BYE);
    }

    private static void echoCommand(String command) {
        System.out.println(command);
    }

}
