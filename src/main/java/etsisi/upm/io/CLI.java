package etsisi.upm.io;

import etsisi.upm.controllers.ProductController;
import etsisi.upm.controllers.TicketController;
import etsisi.upm.Constants;

public class CLI {

    /// Global variables
    private final ProductController productController;  //gobal variable called controller

    // status code

    public CLI() {
        ViewCLI.printWellcomeMessage();
        this.productController = new ProductController();
    }

    public int newQuery(String query) {
        //Check if the query starts with PROD command keyword
        if (query.startsWith(Constants.PROD)) {
            this.prodQuery(deleteSubstring(query,  Constants.createGeneralRegex(Constants.PROD)));
            //if the query starts with TICKET, we handle using ticketQuery().
        } else if (query.startsWith(Constants.TICKET)) {
            this.ticketQuery(deleteSubstring(query,  Constants.createGeneralRegex(Constants.TICKET)));
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
            //this.cashQuery(query);
        }

        return Constants.QUERY_SUCCESS;
        //returns 1
    }


    //client add "<nombre>" <DNI> <email> <cashId>
    //client remove <DNI>
    //client list

    private void clientQuery(String query){
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        if(query.contains(Constants.CLIENT_ADD)){
            //client add "<nombre>" <DNI> <email> <cashId>


        }else if (query.contains(Constants.CLIENT_REMOVE)){

        }else if (query.contains(Constants.CLIENT_LIST)){


        }
    }

    private void prodQuery(String query) {
        String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
        try {
            if (query.contains(Constants.PRODUCT_ADD)) {

                 ViewCLI.print(ProductController.productAdder(querySplit , productController));
            } else if (query.contains(Constants.PRODUCT_LIST)) {

                ViewCLI.print( productController.prodList());
                ViewCLI.print(Constants.okStatus(Constants.PROD, Constants.PRODUCT_LIST));

            } else if (query.contains(Constants.PRODUCT_REMOVE)) {

                productController.prodDelete(productController, query);
            } else if (query.contains(Constants.PRODUCT_UPDATE)) {
                ViewCLI.print(productController.editProcuct(querySplit));

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

            //productController.ticketNew();
            ViewCLI.print(Constants.okStatus(Constants.TICKET,Constants.TICKET_NEW));


        } else if (query.contains(Constants.TICKET_PRINT)) {

            //System.out.println(productController.ticketPrint());
            ViewCLI.print(Constants.okStatus(Constants.TICKET, Constants.TICKET_PRINT));

        } else if (query.contains(Constants.TICKET_REMOVE)) {
            int id = Integer.parseInt(deleteSubstring(query,  Constants.createGeneralRegex(Constants.PRODUCT_REMOVE)));

           // if (controller.removeProductFromTicket(ticketId,id)) {
           //     System.out.println(okStatus(TICKET, TICKET_REMOVE));
           // } else {
            //    errorStatus(TICKET, TICKET_REMOVE);
            //}
        }
    }


    private static String deleteSubstring(String query, String regex) {
        return query.replaceFirst(regex, Constants.STR_EMPTY);
    }


}
