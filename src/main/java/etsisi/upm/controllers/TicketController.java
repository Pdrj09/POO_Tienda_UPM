package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.models.Product;
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

    public Object decodeQuery(String[] querySplit) {
        String ticketId, cashierId, clientId;
        int prodId, amount;
        switch (querySplit[Constants.QUERY_TICKET_POS_INSTRUCTION]){
            case Constants.TICKET_NEW:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                clientId = querySplit[Constants.QUERY_TICKET_POS_USERID];

                return this.newTicket(ticketId, cashierId, clientId);

            case Constants.TICKET_ADD:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                prodId = Integer.parseInt(querySplit[Constants.QUERY_TICKET_POS_PRODID]);
                amount = Integer.parseInt(querySplit[Constants.QUERY_TICKET_POS_AMOUNT]);
                List<String> customizations = null;

                if (querySplit.length>Constants.QUERY_TICKET_POS_CUSTOMIZATIONS &&
                        querySplit[Constants.QUERY_TICKET_POS_CUSTOMIZATIONS].contains("--p")){

                    customizations = new ArrayList<>();

                    //If it has customizations search for the customizations one by one
                    for (int i = Constants.QUERY_TICKET_POS_CUSTOMIZATIONS; i< querySplit.length; i++){
                        //Fin for customization
                        Matcher matcher = Pattern.compile(Constants.REGEX_PERSONALIZED).matcher(querySplit[i]);

                        //Founded customization
                        if (matcher.find()) customizations.add(matcher.group());
                    }
                }

                return this.addProductToTicket(ticketId, cashierId, prodId, amount, customizations);

            case Constants.TICKET_REMOVE:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                prodId = Integer.parseInt(querySplit[Constants.QUERY_TICKET_POS_PRODID]);

                return this.removeProductFromTicket(ticketId,cashierId,prodId);

            case Constants.TICKET_PRINT:

                ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID];
                this.printTicket(ticketId,cashierId);

                break;
            case Constants.TICKET_LIST:

                return this.getTicketList();

            default:
                throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
        return null;
    }

    private Ticket newTicket(String ticketId, String cashierId, String clientId){
        Ticket ticket = new Ticket(ticketId);
        this.ticketRepository.add(ticketId, ticket);
        return ticket;
    }

    private Ticket getTicket(String ticketId){
        return this.ticketRepository.findByIdOrThrow(ticketId);
    }
    private List<Ticket> getTicketList(){
        List<Ticket> ticketList = new ArrayList<Ticket>();
        TreeMap<String, Cashier> sortedCashiers = new TreeMap<>(this.cashierRepository.getMap());

        for (Map.Entry<String, Cashier> entry : sortedCashiers.entrySet()){
            Set<String> ticketIds = entry.getValue().getTickets();
            for (String ticketId : ticketIds){
                ticketList.add(this.ticketRepository.findByIdOrThrow(ticketId));
            }
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


    private Ticket addProductToTicket(String ticketId, String cahsierId, Integer productId, int amount, List<String> customizations){
        Ticket ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Product product = this.productRepository.findByIdOrThrow(productId);

        if (product == null) {
            throw new IllegalArgumentException("Can't find product.");
        }

        //cant exist 2 same serviceProduct
        if (product instanceof ServiceProduct && ticket.containsProduct(product)) {
            throw new IllegalStateException("the same service can't be added twice in the same ticket.");
        }

        //limit participants validation.
        if (product instanceof ServiceProduct) {
            ServiceProduct service = (ServiceProduct) product;
            if (amount <= 0 || amount > Product.maxPeople) {
                throw new IllegalArgumentException("The number of participants (" + amount + ") isn't valid for this service.");
            }
        }

        return ticket.addProduct(product,amount,customizations);
    }

    private Ticket removeProductFromTicket(String ticketId, String cahsierId, Integer productId){
        Ticket ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Product product = this.productRepository.findByIdOrThrow(productId);
        return ticket.remove(product);
    }

    //method were the ticked is closed and prepared for printing it (the view manage that)
    private Ticket printTicket(String ticketId, String cahsierId){
        Ticket ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        this.closeTicket(ticket);
        return ticket;
    }





}
