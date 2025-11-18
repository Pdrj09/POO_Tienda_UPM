package etsisi.upm.controllers;

import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.*;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.util.List;

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
        // -- customitations?
        Ticket ticket = this.ticketRepository.findById(cahsierId);
        Product product = this.productRepository.findById(productId);
        ticket.add(product,amount);
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
