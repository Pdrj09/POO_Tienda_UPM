package etsisi.upm;

import etsisi.upm.controllers.CashierController;
import etsisi.upm.controllers.ClientController;
import etsisi.upm.controllers.ProductController;
import etsisi.upm.controllers.TicketController;
import etsisi.upm.io.CLI;
import etsisi.upm.models.Product;
import etsisi.upm.models.Sellable;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;
import etsisi.upm.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
    private static final String CURSOR = "tUPM> ";

    public static void main(String [] args) {
        //We create the repositories and the controllers with them
        Repository<Integer, Sellable> productRepo = new Repository<>(Constants.MAX_SIZE);
        Repository<String, Ticket<?>> ticketRepo = new Repository<>();
        Repository<String, Client> clientRepo = new Repository<>();
        Repository<String, Cashier> cashierRepo = new Repository<>();

        ProductController productController = new ProductController(productRepo,ticketRepo);
        TicketController ticketController = new TicketController(ticketRepo, clientRepo, cashierRepo, productRepo);
        ClientController clientController = new ClientController(clientRepo, cashierRepo);
        CashierController cashierController = new CashierController(cashierRepo, clientRepo);

        File file = new File("src/main/java/etsisi/upm/io/input.txt");
        int status = Constants.QUERY_SUCCESS;
        CLI cli = new CLI(productController, ticketController, clientController, cashierController);// we create a menu


        try {
            Scanner sc = new Scanner(file);

            while(sc.hasNext() && status == Constants.QUERY_SUCCESS){
                String line = sc.nextLine();
                System.out.println(CURSOR+line);
                status = cli.newQuery(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println(Constants.ERROR_FILE_NOTFOUND);
        }
    }
}
