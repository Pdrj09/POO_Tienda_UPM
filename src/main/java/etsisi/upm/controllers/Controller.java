package etsisi.upm.controllers;

import etsisi.upm.util.Categories;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import java.util.HashMap;

public class Controller {

    private final HashMap<Integer, Product> products;
    private final Ticket ticket;
    private int totalProducts;

    private static final String ERROR_CREATE_PRODUCT = "Error al crear el producto";
    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String PRICE = "PRICE";


    private static final String CATALOG = "Catalog:\n";
    private static final String NEXT_LINE = "\n";
    private static final String TAB_SPACE = "\t";
    private static final int MAX_SIZE = 200;


    public Controller() {
        this.products = new HashMap<>();
        this.ticket = new Ticket();
        this.totalProducts=0;
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    public String addProduct(String name, String category, double price, int id) {
        Product product;
        if (Categories.existCategory(category) && this.totalProducts<MAX_SIZE) {
            product = new Product(id, name, price, Categories.valueOf(category));
            products.put(product.getId(), product);
            this.totalProducts++;
            return product.toString();
        } else return ERROR_CREATE_PRODUCT;
    }
    public String addProductPersonaliced(String name, String category, double price, int id , int maxPers) {
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

    public String addProductToTicket(int prodId, int amount) {
        if (products.containsKey(prodId)) {
            ticket.add(products.get(prodId), amount);
        }
        return ticket.toString();
    }
    public String addPersonalicedProductToTicket(int prodId , int amount, String[] personalizations){
        if (products.containsKey(prodId)) {
            ticket.addPersonalized(products.get(prodId), amount ,personalizations);
        }
        return ticket.toString();
    }

    public boolean removeProductFromTicket(int prodId) {
        if (products.containsKey(prodId)) {
            ticket.remove(products.get(prodId));
            return true;
        } else return false;
    }

    public boolean ticketNew() {
        return this.ticket.clear();
    }


    public String ticketPrint() {
        return this.ticket.toString();
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
