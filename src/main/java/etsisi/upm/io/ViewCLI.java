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


    // TODO DE AQUI PARA ABAJO FUERA

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
