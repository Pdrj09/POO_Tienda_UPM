package etsisi.upm.controllers;

import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.*;
import etsisi.upm.models.ServiceProduct;

import java.util.List;

public class TicketController {
    private final TicketRepository ticketRepository;
    //TODO implement -- añadir los repositorios de user y product
    private final ClientRepository clientRepository;
    private final CashierRepository cashierRepository;
    private final ProductRepository productRepository;

    public TicketController(TicketRepository ticketRepository, ClientRepository clientRepository, CashierRepository cashierRepository, ProductRepository productRepository) {
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
        this.cashierRepository = cashierRepository;
        this.productRepository = productRepository;
    }

    public void newTicket(String ticketId, String cashierId, String clientId){
        Ticket ticket = new Ticket(ticketId);
        this.ticketRepository.add(ticket);
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

    public String printTicket(String ticketId, String cahsierId){
        Ticket ticket = this.ticketRepository.findById(ticketId);
        closeTicket(ticket);
        return ticket.toString();
    }

    private void closeTicket(Ticket ticket){
        ticket.close();
    }

}
