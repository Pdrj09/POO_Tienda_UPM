package etsisi.upm.controllers;

import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.*;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;
import etsisi.upm.models.ServiceProduct;

import java.util.*;

public class TicketController {
    private final Repository<String,Ticket> ticketRepository;
    private final Repository<String, Client> clientRepository;
    private final Repository<String, Cashier> cashierRepository;
    private final Repository<String, Product> productRepository;

    public TicketController(Repository<String,Ticket> ticketRepository, Repository<String, Client> clientRepository, Repository<String, Cashier> cashierRepository, Repository<String, Product> productRepository) {
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
        this.cashierRepository = cashierRepository;
        this.productRepository = productRepository;
    }

    public void newTicket(String ticketId, String cashierId, String clientId){
        Ticket ticket = new Ticket(ticketId);
        this.ticketRepository.add(ticketId,ticket);
    }

    public void addProductToTicket(String ticketId, String cahsierId, String productId, int amount, List<String> customizations){
        Ticket ticket = this.ticketRepository.findById(ticketId);
        Product product = this.productRepository.findById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Can't find product.");
        }

        //validation

        //cant exist 2 same serviceProduct
        if (product instanceof ServiceProduct && ticket.containsProduct(product)) {
            throw new IllegalStateException("the same service can't be added twice in the same ticket.");
        }

        //limit participants validation.
        if (product instanceof ServiceProduct) {
            ServiceProduct service = (ServiceProduct) product;
            if (amount <= 0 || amount > service.getMaxPers()) { // getMaxPers() has to exist in product
                throw new IllegalArgumentException("The number of participants (" + amount + ") isn't valid for this service.");
            }
        }

        // customize execution
        if (customizations != null && !customizations.isEmpty()) {
            // calls personalized products method
            ticket.addPersonalized(product, amount, customizations.toArray(new String[0]));
        } else {
            // calls general products method
            ticket.add(product, amount);
        }
    }

    public void removeProductFromTicket(String ticketId, String cahsierId, String productId){
        Ticket ticket = this.ticketRepository.findById(cahsierId);
        Product product = this.productRepository.findById(productId);
        ticket.remove(product);
    }

    /* TODO: EL ticket print debería gestionarlo el view, que el controlador le devuelva un ticket y él lo convierta*/
    public String printTicket(String ticketId, String cahsierId){
        Ticket ticket = this.ticketRepository.findById(ticketId);
        closeTicket(ticket);
        return ticket.toString();
    }

    // TODO método para usar en el view en vez de llamar a printTicket
    public Ticket getTicket(String ticketId){
        return this.ticketRepository.findById(ticketId);
    }

    private void closeTicket(Ticket ticket){
        ticket.close();
    }

    public Ticket removeTicket(String ticketId, String cashierId, String clientId){
        Ticket ticket = this.ticketRepository.removeById(ticketId);
        Cashier cashier = this.cashierRepository.findById(cashierId);
        Client client = this.clientRepository.findById(clientId);
        cashier.deleteTicket(ticket);
        client.deleteTicket(ticket);
        return ticket;
    }

    public List<Ticket> getTicketList(){
        List<Ticket> ticketList = new ArrayList<Ticket>();
        TreeMap<String, Cashier> sortedCashiers = new TreeMap<>(this.cashierRepository.getMap());

        for (Map.Entry<String, Cashier> entry : sortedCashiers.entrySet()){
            Set<String> ticketIds = entry.getValue().getTickets();
            for (String ticketId : ticketIds){
                ticketList.add(this.ticketRepository.findById(ticketId));
            }
        }
        return ticketList;
    }


}
