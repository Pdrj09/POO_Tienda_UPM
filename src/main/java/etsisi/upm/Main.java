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

public class Main {
    private static final String CURSOR = "tUPM> ";

    public static void main(String [] args) {
        //We create the repositories and the controllers with them
        Repository<Integer, Sellable> productRepo = new Repository<>(Constants.MAX_SIZE);
        Repository<String, Ticket<?>> ticketRepo = new Repository<>();
        Repository<String, Client> clientRepo = new Repository<>();
        Repository<String, Cashier> cashierRepo = new Repository<>();

        ProductController productController = new ProductController(productRepo, ticketRepo);
        TicketController ticketController = new TicketController(ticketRepo, clientRepo, cashierRepo, productRepo);
        ClientController clientController = new ClientController(clientRepo, cashierRepo);
        CashierController cashierController = new CashierController(cashierRepo, clientRepo);

        CLI cli = new CLI(productController, ticketController, clientController, cashierController);

        int status = Constants.QUERY_SUCCESS;

        Scanner sc = null;
        boolean fromFile = false;

        // Check args for a file
        if (args.length > 0) {
            File file = new File(args[0]);
            try {
                sc = new Scanner(file);
                fromFile = true;
            } catch (FileNotFoundException e) {
                System.out.println(Constants.ERROR_FILE_NOTFOUND);
                return; // salir si el archivo no existe
            }
        } else {
            sc = new Scanner(System.in);
        }

        while (status == Constants.QUERY_SUCCESS) {
            if (!fromFile) {
                System.out.print(CURSOR);
            }

            if (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (fromFile) {
                    System.out.println(CURSOR + line);
                }
                status = cli.newQuery(line);
            } else {
                break; // si no hay más líneas en el archivo, salir
            }
        }

        sc.close();
    }
}
