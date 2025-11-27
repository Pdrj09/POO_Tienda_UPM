package etsisi.upm;

//import etsisi.upm.controllers.CashierController;
import etsisi.upm.controllers.ClientController;
import etsisi.upm.controllers.ProductController;
import etsisi.upm.controllers.TicketController;
import etsisi.upm.io.CLI;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.util.Scanner;

public class Main {
    private static final String CURSOR = "tUPM> ";

    public static void main(String [] args) {
        //We create the repositories and the controllers with them
        Repository<Integer, Product> productRepo = new Repository<>(Constants.MAX_SIZE);
        Repository<String, Ticket> ticketRepo = new Repository<>();
        Repository<String, Client> clientRepo = new Repository<>();
        Repository<String, Cashier> cashierRepo = new Repository<>();

        ProductController productController = new ProductController(productRepo,ticketRepo);
        TicketController ticketController = new TicketController(ticketRepo, clientRepo, cashierRepo, productRepo);
        ClientController clientController = new ClientController(clientRepo, cashierRepo);
//        CashierController cashierController = new CashierController(cashierRepo);

        //We call scanner
        Scanner sc = new Scanner(System.in);
        int status;
        CLI cli = new CLI(productController, ticketController, clientController, null);// we create a menu

        do {
            System.out.print(CURSOR);
            status = cli.newQuery(sc.nextLine());
         } while (status == Constants.QUERY_SUCCESS);
    }
}
