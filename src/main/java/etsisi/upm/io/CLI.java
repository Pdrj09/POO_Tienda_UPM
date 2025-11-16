package etsisi.upm.io;

import etsisi.upm.controllers.ProductController;
import etsisi.upm.controllers.TicketController;

public class CLI {

    /// Global variables
    private final ProductController productController;  //gobal variable called controller

    // status code
    private static final int QUERY_SUCCESS = 1;
    private static final int QUERY_EXIT = 0;

    // numbers
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;

    private static final String OK_STATUS = "ok";  //Ok
    private static final String ERROR_STATUS = "Error"; //error

    // menu const
    private static final String EXIT = "exit";
    private static final String PROD = "prod";
    private static final String TICKET = "ticket";
    private static final String ECHO = "echo";
    private static final String HELP = "help";
    private static final String CLIENT = "client";
    private static final String CASH = "cash";

    // str const
    private static final String STR_EMPTY = "";
    private static final String STR_DOT = ".";
    private static final String STR_COMMA = ",";
    private static final String STR_ERROR = "Error";
    private static final String STR_BLANK_SPACE = " ";
    private static final String STR_DOUBLE_DOT = ":";

    // regex const
    private static final String REGEX_INIT = "^";
    private static final String REGEX_BLANK_SPACE = "\\s*";
    private static final String REGEX_DOUBLE_QUOTE = "\"";
    private static final String REGEX_TO_SPLIT = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final String REGEX_PERSONALIZED = "(?<=--p)";


    // products const
    private static final String PRODUCT_ADD = "add";
    private static final String PRODUCT_LIST = "list";
    private static final String PRODUCT_UPDATE = "update";
    private static final String PRODUCT_REMOVE = "remove";

    // ticket const
    private static final String TICKET_ADD = "add";
    private static final String TICKET_PRINT = "print";
    private static final String TICKET_NEW = "new";
    private static final String TICKET_REMOVE = "remove";

    // client const
    private static final String CLIENT_ADD = "add";
    private static final String CLIENT_REMOVE = "remove";
    private static final String CLIENT_LIST = "list";

    public CLI() {
        ViewCLI.printWellcomeMessage();
        this.productController = new ProductController();
    }

    public int newQuery(String query) {
        //Check if the query starts with PROD command keyword
        if (query.startsWith(PROD)) {
            this.prodQuery(deleteSubstring(query, createGeneralRegex(PROD)));
            //if the query starts with TICKET, we handle using ticketQuery().
        } else if (query.startsWith(TICKET)) {
            this.ticketQuery(deleteSubstring(query, createGeneralRegex(TICKET)));
            //if query starts with ECHO,it echoes back the input
        } else if (query.startsWith(ECHO)) {
            ViewCLI.echoCommand(query);
            //if query starts with HELP, displays help information available
        } else if (query.startsWith(HELP)) {
            ViewCLI.printHelp();
            //if query starts with EXIT prints goodbye message
        } else if (query.startsWith(EXIT)) {
            ViewCLI.printExit();
            return QUERY_EXIT;
            //returns 0
        }else if (query.startsWith(CLIENT)){
            this.clientQuery(query);
        }else if (query.startsWith(CASH)){
            //this.cashQuery(query);
        }

        return QUERY_SUCCESS;
        //returns 1
    }
    //client add "<nombre>" <DNI> <email> <cashId>
    //client remove <DNI>
    //client list

    private void clientQuery(String query){
        String[] querySplit = query.split(REGEX_TO_SPLIT);
        if(query.contains(CLIENT_ADD)){
            
        }else if (query.contains(CLIENT_REMOVE)){

        }else if (query.contains(CLIENT_LIST)){


        }
    }

    private void prodQuery(String query) {
        String[] querySplit = query.split(REGEX_TO_SPLIT);
        try {
            if (query.contains(PRODUCT_ADD)) {

                 ViewCLI.print(ProductController.productAdder(querySplit , productController));

            } else if (query.contains(PRODUCT_LIST)) {

                System.out.println(productController.prodList());

                System.out.println(okStatus(PROD, PRODUCT_LIST));

            } else if (query.contains(PRODUCT_REMOVE)) {
                int id = Integer.parseInt(deleteSubstring(query, createGeneralRegex(PRODUCT_REMOVE)));


                String deletedProd = productController.deleteProduct(id);

                if (deletedProd != null) {
                    System.out.println(deletedProd);
                    System.out.println(okStatus(PROD, PRODUCT_REMOVE));
                } else {
                    ViewCLI.errorStatus(PROD, PRODUCT_REMOVE);
                }


            } else if (query.contains(PRODUCT_UPDATE)) {


                int id = Integer.parseInt(querySplit[ONE]);


                String productEdited = productController.updateProduct(id, querySplit[TWO], querySplit[THREE]);

                if (productEdited != null) {
                    System.out.println(productEdited);
                    System.out.println(okStatus(TICKET, TICKET_NEW));
                } else {
                    ViewCLI.errorStatus(TICKET, TICKET_NEW);
                }

            }
        } catch (Exception e) {
            ViewCLI.errorStatus(PROD, PRODUCT_ADD, e.toString());
        }
    }
    /*ticket new [<id>] <cashId> <userId>
    ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]
    ticket remove <ticketId><cashId> <prodId>
    ticket print <ticketId> <cashId>
    ticket list*/

    private void ticketQuery(String query) {
        String[] querySplit = query.split(REGEX_TO_SPLIT);
        String ticketId = querySplit[ONE];

        if (query.contains(TICKET_ADD)) {

            //ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]


            String cashId = querySplit[TWO];
            int id = Integer.parseInt(querySplit[THREE]);

            int quantity = Integer.parseInt(querySplit[FOUR]);

            String newTicket = "";
            //if it is a personalized prod

            if (querySplit [querySplit.length-1].contains("--p") ){
                String[] queryPersonalized = querySplit[FIVE].split(REGEX_TO_SPLIT);
                /// newTicket = controller.addPersonalicedProductToTicket(ticketId, cashId, id, quantity, queryPersonalized);
            }else {
                /// newTicket =  controller.addProductToTicket(ticketId, cashId, id, quantity);
            }
            if (newTicket != null) {
                System.out.println(newTicket);
                System.out.println(okStatus(TICKET, TICKET_ADD));
            } else {
                ViewCLI.errorStatus(TICKET, TICKET_ADD);
            }

        } else if (query.contains(TICKET_NEW)) {

            productController.ticketNew();
            System.out.println(okStatus(TICKET, TICKET_NEW));


        } else if (query.contains(TICKET_PRINT)) {

            System.out.println(productController.ticketPrint());
            System.out.println(okStatus(TICKET, TICKET_PRINT));

        } else if (query.contains(TICKET_REMOVE)) {
            int id = Integer.parseInt(deleteSubstring(query, createGeneralRegex(PRODUCT_REMOVE)));

           // if (controller.removeProductFromTicket(ticketId,id)) {
           //     System.out.println(okStatus(TICKET, TICKET_REMOVE));
           // } else {
            //    errorStatus(TICKET, TICKET_REMOVE);
            //}
        }
    }



    private String deleteSubstring(String query, String regex) {
        return query.replaceFirst(regex, STR_EMPTY);
    }

    private String createGeneralRegex(String query) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();

        stringBuilder.append(REGEX_INIT)
                .append(query)
                .append(REGEX_BLANK_SPACE);

        return stringBuilder.toString();
    }

    private String okStatus(String type, String comand) {
        StringBuilder builder;
        builder = new StringBuilder();

        builder.append(type)
                .append(STR_BLANK_SPACE)
                .append(comand)
                .append(STR_DOUBLE_DOT)
                .append(STR_BLANK_SPACE)
                .append(OK_STATUS);

        return builder.toString();
    }

}
