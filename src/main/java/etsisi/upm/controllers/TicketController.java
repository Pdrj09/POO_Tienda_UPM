package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.View;
import etsisi.upm.models.Product;
import etsisi.upm.models.ProductPersonalized;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.*;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;
import etsisi.upm.models.ServiceProduct;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketController {
    private final Repository<String, Ticket> ticketRepository;
    private final Repository<String, Client> clientRepository;
    private final Repository<String, Cashier> cashierRepository;
    private final Repository<Integer, Product> productRepository;


    public TicketController(Repository<String, Ticket> ticketRepository, Repository<String, Client> clientRepository, Repository<String, Cashier> cashierRepository, Repository<Integer, Product> productRepository) {
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
        this.cashierRepository = cashierRepository;
        this.productRepository = productRepository;
    }

    public String decodeQuery(String[] querySplit) {
        String ticketId, cashierId, clientId;
        String command = Constants.TICKET + Constants.STR_BLANK_SPACE + querySplit[Constants.QUERY_TICKET_POS_INSTRUCTION];
        int prodId, amount;
        switch (querySplit[Constants.QUERY_TICKET_POS_INSTRUCTION]){
            case Constants.TICKET_NEW:
                int index;
                if(querySplit.length == Constants.QUERY_TICKET_ADD_LENGHT_WITHID){
                    ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                    index = Constants.TICKET_WITH_ID_INDEX;
                } else if (querySplit.length == Constants.QUERY_TICKET_ADD_LENGHT_WITHOUTID) {
                    ticketId = null;
                    index = Constants.TICKET_WITHOUT_ID_INDEX;
                }else throw new IllegalArgumentException(Constants.ERROR_TOOMANY_ARGUMENTS);

                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID-index];
                clientId = querySplit[Constants.QUERY_TICKET_POS_USERID-index];

                return View.getString(this.newTicket(ticketId, cashierId, clientId), command);

            case Constants.TICKET_ADD:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                prodId = Integer.parseInt(querySplit[Constants.QUERY_TICKET_POS_PRODID]);
                amount = Integer.parseInt(querySplit[Constants.QUERY_TICKET_POS_AMOUNT]);
                List<String> customizations = null;

                if (querySplit.length > Constants.QUERY_TICKET_POS_CUSTOMIZATIONS) {
                    if (querySplit[Constants.QUERY_TICKET_POS_CUSTOMIZATIONS].contains("--p")) {
                        customizations = new ArrayList<>();
                        for (int i = Constants.QUERY_TICKET_POS_CUSTOMIZATIONS; i < querySplit.length; i++) {
                            String arg = querySplit[i];
                            if (arg.toLowerCase().startsWith("--p")) {
                                String customName = arg.substring(3).trim();
                                if (!customName.isEmpty()) {
                                    customizations.add(customName);
                                }
                            }
                        }
                    }
                }
                return View.getString(this.addProductToTicket(ticketId, cashierId, prodId, amount, customizations), command);

            case Constants.TICKET_REMOVE:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                prodId = Integer.parseInt(querySplit[Constants.QUERY_TICKET_POS_PRODID]);

                return View.getString(this.removeProductFromTicket(ticketId,cashierId,prodId), command);

            case Constants.TICKET_PRINT:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                return View.getString(this.printTicket(ticketId,cashierId), command);

            case Constants.TICKET_LIST:

                return View.getString(this.getTicketList(), command);

            default:
                throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    private Ticket newTicket(String ticketId, String cashierId, String clientId){
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);
        Client client = this.clientRepository.findByIdOrThrow(clientId);

        Ticket ticket;
        if(ticketId != null) ticket = new Ticket(ticketId);
        else ticket = new Ticket();

        this.ticketRepository.add(ticketId, ticket);
        cashier.addTicket(ticket);
        client.addAssociatedTicket(ticket);

        return ticket;
    }

    private Ticket getTicket(String ticketId){
        return this.ticketRepository.findByIdOrThrow(ticketId);
    }

    private List<Ticket> getTicketList(){
        List<Ticket> ticketList = new ArrayList<Ticket>();
        TreeMap<String, Cashier> sortedCashiers = new TreeMap<>(this.cashierRepository.getMap());

        for (Map.Entry<String, Cashier> entry : sortedCashiers.entrySet()){
            ticketList.addAll(entry.getValue().getTickets());
        }
        return ticketList;
    }

    private void closeTicket(Ticket ticket){
        ticket.close();
    }

    private Ticket removeTicket(String ticketId, String cashierId, String clientId){
        Ticket ticket = this.ticketRepository.removeById(ticketId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);
        Client client = this.clientRepository.findByIdOrThrow(clientId);
        cashier.deleteTicket(ticket);
        client.deleteTicket(ticket);
        return ticket;
    }


    private Ticket addProductToTicket(String ticketId, String cashierId, Integer productId, int amount, List<String> customizations){
        Ticket ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Product product = this.productRepository.findByIdOrThrow(productId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);

        if (!cashier.getTickets().contains(ticket)) throw new IllegalArgumentException(Constants.ERROR_INVALID_ID);

        switch (product) {
            case null -> throw new IllegalArgumentException(Constants.ERROR_NO_PRODUCTS_FOUND);


            //cant exist 2 same serviceProduct
            case ServiceProduct serviceProduct when ticket.containsProduct(product) ->
                    throw new IllegalStateException(Constants.ERROR_SERVICE_ALREADY_EXIST);


            //limit participants validation.
            case ServiceProduct service -> {
                if (amount <= 0 || amount > Constants.MAX_PERSONALIZATIONS_ALLOWED) {
                    throw new IllegalArgumentException(Constants.ERROR_INVALID_SERVICE_PEOPLE_1 + amount + Constants.ERROR_INVALID_SERVICE_PEOPLE_2);
                }
            }
            default -> {
            }
        }
        Product finalProduct;
        if (customizations != null && !customizations.isEmpty()){
            if (product.isPersonalizable())
                finalProduct = new ProductPersonalized(product, customizations);
            else
                throw new IllegalStateException(Constants.ERROR_NONPERSONALIZABLE);
        } else
            finalProduct = product;
        return ticket.addProduct(finalProduct,amount);
    }

    private Ticket removeProductFromTicket(String ticketId, String cashierId, Integer productId){
        Ticket ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Product product = this.productRepository.findByIdOrThrow(productId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);

        if (!cashier.getTickets().contains(ticket)) throw new IllegalArgumentException(Constants.ERROR_INVALID_ID);

        return ticket.remove(product);
    }

    //method were the ticked is closed and prepared for printing it (the view manage that)
    private Ticket printTicket(String ticketId, String cashierId){
        Ticket ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);

        if (!cashier.getTickets().contains(ticket)) throw new IllegalArgumentException(Constants.ERROR_INVALID_ID);

        this.closeTicket(ticket);
        return ticket;
    }
}
