package etsisi.upm.controllers;

import etsisi.upm.models.*;
import etsisi.upm.util.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.repositories.*;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;
import etsisi.upm.util.Utilities;

import java.util.*;

public class TicketController {
    private final Repository<String, Ticket<?>> ticketRepository;
    private final Repository<String, Client> clientRepository;
    private final Repository<String, Cashier> cashierRepository;
    private final Repository<Integer, Sellable> productRepository;


    public TicketController(Repository<String, Ticket<?>> ticketRepository, Repository<String, Client> clientRepository,
                            Repository<String, Cashier> cashierRepository,
                            Repository<Integer, Sellable> productRepository) {
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
        this.cashierRepository = cashierRepository;
        this.productRepository = productRepository;
    }

    public String decodeQuery(String[] querySplit) {
        String ticketId, cashierId, clientId;
        String command = Constants.TICKET + Constants.STR_BLANK_SPACE +
                querySplit[Constants.QUERY_TICKET_POS_INSTRUCTION];
        int prodId, amount;
        switch (querySplit[Constants.QUERY_TICKET_POS_INSTRUCTION]){
            case Constants.TICKET_NEW:
                int index;

                if(Utilities.isPositiveInteger(querySplit[Constants.QUERY_TICKET_POS_TICKETID])) {
                    ticketId = querySplit[Constants.QUERY_TICKET_POS_TICKETID];
                    index = Constants.TICKET_WITH_ID_INDEX;
                } else{
                    do {
                        ticketId = String.format(Constants.ID_FORMAT, new Random().nextInt(Constants.MAX_RANDOM));
                    } while (ticketRepository.hasKey(ticketId));
                    index = Constants.TICKET_WITHOUT_ID_INDEX;

                }

                cashierId = querySplit[Constants.QUERY_TICKET_POS_CASHID-index];
                clientId = querySplit[Constants.QUERY_TICKET_POS_USERID-index];

                String ticketType;
                if (querySplit[querySplit.length - 1].matches(Constants.REGEX_TICKET_OPT)) {
                    ticketType = querySplit[querySplit.length - 1];
                } else if (querySplit[querySplit.length - 1].matches(Constants.REGEX_IS_DNI)) {
                    ticketType = Constants.P_OPTION;
                } else {
                    throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
                }

                if(querySplit.length <= Constants.QUERY_TICKET_MAX_LENGTH){
                    this.newTicket(ticketId, cashierId, clientId, ticketType);
                    return Constants.okStatus(command.split(" ")[0], command.split(" ")[1]);
                }

                throw new IllegalArgumentException(Constants.ERROR_TOOMANY_ARGUMENTS);

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
                return View.getString(this.addProductToTicket(ticketId, cashierId, prodId, amount, customizations),
                        command);

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
    private Ticket<?> newTicket(String ticketId, String cashierId, String clientId, String ticketType){
        // NEW ticket function that is going to work with the ticket types
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);
        Client client = this.clientRepository.findByIdOrThrow(clientId);

        Ticket<?> ticket;

        if(ticketType.equals("-s")) {

            ticket = new TicketOfServices(ticketId);

        } else if (ticketType.equals("-p")){

                ticket = new TicketOfProducts(ticketId);

        } else if (ticketType.equals("-c")) {

                ticket = new TicketOfMixed(ticketId);
        }
        else{
            throw new IllegalArgumentException(Constants.ERROR_TICKET_NONEXISTENT_TYPE);
        }

        this.ticketRepository.add(ticketId, ticket);
        cashier.addTicket(ticket);
        client.addAssociatedTicket(ticket);  //    ticket new UW7258278 11100154D

        this.cashierRepository.update(cashier);
        this.clientRepository.update(client);

        return ticket;
    }

    private Ticket<?> getTicket(String ticketId){
        return this.ticketRepository.findByIdOrThrow(ticketId);
    }

    private List<Ticket<?>> getTicketList(){
        List<Ticket<?>> ticketList = new ArrayList<Ticket<?>>();
        TreeMap<String, Cashier> sortedCashiers = new TreeMap<>(this.cashierRepository.getMap());

        for (Map.Entry<String, Cashier> entry : sortedCashiers.entrySet()){
            ticketList.addAll(entry.getValue().getTickets());
        }
        return ticketList;
    }

    private void closeTicket(Ticket<?> ticket){
        ticket.close();
    }

    private Ticket<?> removeTicket(String ticketId, String cashierId, String clientId){
        Ticket<?> ticket = this.ticketRepository.removeById(ticketId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);
        Client client = this.clientRepository.findByIdOrThrow(clientId);
        cashier.deleteTicket(ticket);
        client.deleteTicket(ticket);
        return ticket;
    }


    private Ticket<?> addProductToTicket(String ticketId, String cashierId, Integer productId, int amount,
                                      List<String> customizations){
        Ticket<?> ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Sellable product = this.productRepository.findByIdOrThrow(productId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);

        if (!cashier.getTickets().contains(ticket)) throw new IllegalArgumentException(Constants.ERROR_INVALID_ID);

        switch (product) {
            case null -> throw new IllegalArgumentException(Constants.ERROR_NO_PRODUCTS_FOUND);
            //it doesnt allow empty tickets


            //cant exist 2 same serviceProduct
            case ServiceProduct serviceProduct when ticket.containsProduct(product) ->
                    throw new IllegalStateException(Constants.ERROR_SERVICE_ALREADY_EXIST);


            //limit participants validation.
            case ServiceProduct service -> {
                if (amount <= 0 || amount > Constants.MAX_PERSONALIZATIONS_ALLOWED) {
                    throw new IllegalArgumentException(Constants.ERROR_INVALID_SERVICE_PEOPLE_1 + amount +
                            Constants.ERROR_INVALID_SERVICE_PEOPLE_2);
                }
            }
            default -> {
            }
        }
        Sellable finalProduct;
        if (customizations != null && !customizations.isEmpty() && product instanceof Product personalized ) {
            if (personalized.isPersonalizable())
                finalProduct = new ProductPersonalized(personalized, customizations);
            else
                throw new IllegalStateException(Constants.ERROR_NONPERSONALIZABLE);
        } else
            finalProduct = product;
        return ticket.addProduct(finalProduct, amount);
    }

    private Ticket<?> removeProductFromTicket(String ticketId, String cashierId, Integer productId){
        Ticket<?> ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Sellable product = this.productRepository.findByIdOrThrow(productId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);

        if (!cashier.getTickets().contains(ticket)) throw new IllegalArgumentException(Constants.ERROR_INVALID_ID);

        return ticket.remove(product);
    }

    //method were the ticked is closed and prepared for printing it (the view manage that)
    private Ticket<?> printTicket(String ticketId, String cashierId){
        Ticket<?> ticket = this.ticketRepository.findByIdOrThrow(ticketId);
        Cashier cashier = this.cashierRepository.findByIdOrThrow(cashierId);

        if (!cashier.getTickets().contains(ticket)) throw new IllegalArgumentException(Constants.ERROR_INVALID_ID);

        this.closeTicket(ticket);
        return ticket;
    }
}
