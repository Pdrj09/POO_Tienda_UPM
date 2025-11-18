package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.CLI;
import etsisi.upm.util.Categories;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import java.util.HashMap;

public class ProductController {

    private static HashMap<Integer, Product> products;
    private static Ticket ticket;
    private int totalProducts;


    private static final String ERROR_CREATE_PRODUCT = "Error al crear el producto";


    private static final String CATALOG = "Catalog:\n";
    private static final String NEXT_LINE = "\n";
    private static final String TAB_SPACE = "\t";
    private static final int MAX_SIZE = 200;

    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String PRICE = "PRICE";


    private static String deleteSubstring(String query, String regex) {
        return query.replaceFirst(regex, Constants.STR_EMPTY);
    }


    private static String okStatus(String type, String comand) {
        StringBuilder builder;
        builder = new StringBuilder();

        builder.append(type)
                .append(Constants.STR_BLANK_SPACE)
                .append(comand)
                .append(Constants.STR_DOUBLE_DOT)
                .append(Constants.STR_BLANK_SPACE)
                .append(Constants.OK_STATUS);

        return builder.toString();
    }


    public static String productAdder(String[] querySplit, ProductController productController) {
        if ((querySplit[Constants.ONE].isEmpty())||(querySplit[Constants.ONE ].equals(Constants.STR_BLANK_SPACE )) ){
            throw new IllegalArgumentException("there is no id for product ");
        }
        int id = Integer.parseInt(querySplit[Constants.ONE]);
        String name = querySplit[Constants.TWO].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

        if ((querySplit[Constants.THREE].isEmpty())||(querySplit[Constants.THREE].equals(Constants.STR_BLANK_SPACE )) ){
            throw new IllegalArgumentException("The product has to have a price");
        }
        float price = Float.parseFloat(querySplit[Constants.FOUR].replace(Constants.STR_COMMA, Constants.STR_DOT));

        String response;

        if (querySplit.length > Constants.FIVE) {
            int maxPers = Integer.parseInt(querySplit[Constants.FIVE]);
            response = productController.addProduct(name, querySplit[Constants.THREE], price, id, maxPers);
        } else {

            response = productController.addProduct(name, querySplit[Constants.THREE], price, id);
            System.out.println(response);
        }
        if (!response.startsWith(Constants.STR_ERROR)) {
            response = okStatus(Constants.PROD, Constants.PRODUCT_ADD);
        }
        return response;
    }

    public static String prodDelete (ProductController productController, String query) {
        int id = Integer.parseInt(deleteSubstring(query, Constants.createGeneralRegex(Constants.PRODUCT_REMOVE)));
        String deletedProd = productController.deleteProduct(id);
        String response = "";
        if (deletedProd != null) {
            System.out.println(deletedProd);
            response = okStatus(Constants.PROD, Constants.PRODUCT_REMOVE);
        } else {
           // CLI.errorStatus(PROD, PRODUCT_REMOVE);
        }
        return response;
    }

    public ProductController() {
        this.products = new HashMap<>();
      //  this.ticket = new Ticket();
        this.totalProducts=0;
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    private String addProduct(String name, String category, double price, int id) {
        Product product;
        if (Categories.existCategory(category) && this.totalProducts<MAX_SIZE) {
            product = new Product(id, name, price, Categories.valueOf(category));
            products.put(product.getId(), product);
            this.totalProducts++;
            return product.toString();
        } else return ERROR_CREATE_PRODUCT;
    }
    private String addProduct(String name, String category, double price, int id , int maxPers) {
        Product product;
        if (Categories.existCategory(category)) {
            product = new Product(id, name, price, Categories.valueOf(category),maxPers);
            products.put(product.getId(), product);
            return product.toString();
        } else return ERROR_CREATE_PRODUCT;
    }


    public String updateProduct(int id, String field, String newContent) {
        if (this.products.get(id) == null) return null;
        switch (field) {
            case NAME:
                this.products.get(id).setName(newContent);
                break;
            case CATEGORY:
                if (Categories.existCategory(newContent)) {
                    Categories cat = Categories.valueOf(newContent);
                    this.products.get(id).setCategory(cat);
                } else return null;
                break;
            case PRICE:
                this.products.get(id).setPrice(Double.parseDouble(newContent));
                break;
            default:
                return null;
        }
        return this.products.get(id).toString();
    }

    //here we delete a product from the hashmap of products
    //return true if delete succeed, else false
    public String deleteProduct(int prodId) {
        if (products.containsKey(prodId)) {
            ticket.remove(products.get(prodId));
            this.totalProducts--;
            return products.remove(prodId).toString();
        } else return null;
    }

    public static String addProductToTicket(String ticketId,String cashId, int prodId, int amount) {
        if (products.containsKey(prodId)) {
            ticket.add(products.get(prodId), amount);
        }
        return ticket.toString();
    }

    public String prodList() {
        StringBuilder builder = new StringBuilder();

        builder.append(CATALOG);

        for (Product p : products.values()) {
            builder.append(TAB_SPACE)
                    .append(p.toString())
                    .append(NEXT_LINE);
        }

        return builder.toString();
    }
}
