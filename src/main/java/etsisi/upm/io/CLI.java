package etsisi.upm.io;

import etsisi.upm.controllers.CashierController;
import etsisi.upm.controllers.ClientController;
import etsisi.upm.controllers.ProductController;
import etsisi.upm.controllers.TicketController;
import etsisi.upm.Constants;
import etsisi.upm.models.Ticket;

public class CLI {

    /// Global variables
    private final ProductController productController;  //gobal variable called controller
    private final TicketController ticketController;
    private final ClientController clientController;
    private final CashierController cashierController;
    // status code

    public CLI(ProductController productController, TicketController ticketController, ClientController clientController, CashierController cashierController) {
        ViewCLI.printWellcomeMessage();
        this.productController = productController;
        this.ticketController = ticketController;
        this.clientController = clientController;
        this.cashierController = cashierController;
    }

    public int newQuery(String query) {
        //Check if the query starts with PROD command keyword
        if (query.startsWith(Constants.PROD)) {
            this.prodQuery(Constants.deleteSubstring(query,  Constants.createGeneralRegex(Constants.PROD)));
            //if the query starts with TICKET, we handle using ticketQuery().
        } else if (query.startsWith(Constants.TICKET)) {
            this.ticketQuery(Constants.deleteSubstring(query,  Constants.createGeneralRegex(Constants.TICKET)));
            //if query starts with ECHO,it echoes back the input
        } else if (query.startsWith(Constants.ECHO)) {
            ViewCLI.echoCommand(query);
            //if query starts with HELP, displays help information available
        } else if (query.startsWith(Constants.HELP)) {
            ViewCLI.printHelp();
            //if query starts with EXIT prints goodbye message
        } else if (query.startsWith(Constants.EXIT)) {
            ViewCLI.printExit();
            return Constants.QUERY_EXIT;
            //returns 0
        }else if (query.startsWith(Constants.CLIENT)){
            this.clientQuery(query);
        }else if (query.startsWith(Constants.CASH)){
            this.cashQuery(query);
        }
        return Constants.QUERY_SUCCESS;
        //returns 1
    }


    //client add "<nombre>" <DNI> <email> <cashId>
    //client remove <DNI>
    //client list

    private void clientQuery(String query){
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        try {
            if (query.contains(Constants.CLIENT_ADD)) {
                ViewCLI.print(clientController.clientAddControl(querySplit));
            } else if (query.contains(Constants.CLIENT_REMOVE)) {
                String id = querySplit[1];
                clientController.removeClients(id);
                ViewCLI.print(Constants.okStatus(Constants.CLIENT, Constants.CLIENT_REMOVE));
            } else if (query.contains(Constants.CLIENT_LIST)) {
                ViewCLI.printClients(clientController.listClients());
                ViewCLI.print(Constants.okStatus(Constants.CLIENT, Constants.CLIENT_LIST));
            }
        }catch(Exception e) {
            ViewCLI.print(Constants.errorStatus(Constants.CLIENT, "Error", e.getMessage()));
        }
    }

    private void prodQuery(String query) {
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        try {
            if (query.contains(Constants.PRODUCT_ADD)) {
                 ViewCLI.print(ProductController.productAdder(querySplit , productController));
            } else if (query.contains(Constants.PRODUCT_LIST)) {
                ViewCLI.print(productController.prodList());
                ViewCLI.print(Constants.okStatus(Constants.PROD, Constants.PRODUCT_LIST));
            } else if (query.contains(Constants.PRODUCT_REMOVE)) {
                productController.prodDelete(productController, query);
            } else if (query.contains(Constants.PRODUCT_UPDATE)) {
                ViewCLI.print(productController.editProduct(querySplit));
            }
        } catch (Exception e) {
            ViewCLI.print(Constants.errorStatus(Constants.PROD, Constants.PRODUCT_ADD, e.toString()));
        }
    }
    /*ticket new [<id>] <cashId> <userId>
    ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]
    ticket remove <ticketId><cashId> <prodId>
    ticket print <ticketId> <cashId>
    ticket list*/

    private void ticketQuery(String query) {
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        String ticketId = querySplit[Constants.ONE];
        try{
            if (query.contains(Constants.TICKET_ADD)) {
                //ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]

                String cashId = querySplit[Constants.TWO];
                int id = Integer.parseInt(querySplit[Constants.THREE]);

                int quantity = Integer.parseInt(querySplit[Constants.FOUR]);

                String newTicket = "";
                //if it is a personalized prod

                if (querySplit [querySplit.length-1].contains("--p") ){
                    String[] queryPersonalized = querySplit[Constants.FIVE].split(Constants.REGEX_TO_SPLIT);
                    //newTicket = ProductController.addProductToTicket(ticketId, cashId, id, quantity, queryPersonalized);
                }else {
                    newTicket =  ProductController.addProductToTicket(ticketId, cashId, id, quantity);
                }
                if (newTicket != null) {
                    System.out.println(newTicket);
                    ViewCLI.print(Constants.okStatus(Constants.TICKET, Constants.TICKET_ADD));
                } else {
                    Constants.errorStatus(Constants.TICKET,Constants. TICKET_ADD);
                }

            } else if (query.contains(Constants.TICKET_NEW)) {
                ViewCLI.printTickets(ticketController.getTicketList());
                ViewCLI.print(Constants.okStatus(Constants.TICKET,Constants.TICKET_NEW));
            } else if (query.contains(Constants.TICKET_PRINT)) {
                ViewCLI.printTicket(ticketController.getTicket(ticketId));
                ViewCLI.print(Constants.okStatus(Constants.TICKET, Constants.TICKET_PRINT));

            } else if (query.contains(Constants.TICKET_REMOVE)) {
                int id = Integer.parseInt(Constants.deleteSubstring(query,  Constants.createGeneralRegex(Constants.PRODUCT_REMOVE)));

                // if (controller.removeProductFromTicket(ticketId,id)) {
                //     System.out.println(okStatus(TICKET, TICKET_REMOVE));
                // } else {
                //    errorStatus(TICKET, TICKET_REMOVE);
                //}
            }
        }catch (Exception e) {
            ViewCLI.print(Constants.errorStatus(Constants.TICKET, "Error", e.getMessage()));
        }
    }


    private void cashQuery(String query){
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        try {
            if (query.contains(Constants.CASH_ADD)) {
                //cash add [<id>] "<nombre>" <email>
                //TODO pasarle los parámetros por cachitos hasta añadirlo
                //String response = cashierController.addCashier(query);
                //ViewCLI.print(response);
                ViewCLI.print(Constants.okStatus(Constants.CASH, Constants.CASH_ADD));

            } else if (query.contains(Constants.CASH_REMOVE)) {
                if (cashierController.removeCashier(querySplit[Constants.ONE]) != null)
                    ViewCLI.print(Constants.okStatus(Constants.CASH, Constants.CASH_REMOVE));
                else
                    ViewCLI.print(Constants.errorStatus(Constants.CASH, Constants.CASH_REMOVE, "Cashier not found"));

            } else if (query.contains(Constants.CASH_LIST)) {
                //cash list
                ViewCLI.printCashiers(cashierController.listCashiers());
                ViewCLI.print(Constants.okStatus(Constants.CASH, Constants.CASH_LIST));

            } else if (query.contains(Constants.CASH_TICKETS)) {
                //cash tickets <id>
                //TODO hacer que printeé los tickets asociados a un cajero, hay que modificar el método listTickets
                String cashId = querySplit[1];
                //ViewCLI.printTickets(cashierController.listTickets(cashId));
                ViewCLI.print(Constants.okStatus(Constants.CASH, Constants.CASH_TICKETS));
            }
        } catch (Exception e) {
            ViewCLI.print(Constants.errorStatus(Constants.CASH, "Error", e.getMessage()));
        }
    }

}
