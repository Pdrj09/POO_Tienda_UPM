package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.util.Categories;
import etsisi.upm.models.Product;

import etsisi.upm.models.Food;
import etsisi.upm.models.Meeting;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class ProductController {
    private final Repository<Integer, Product> productRepository;
    private final Repository<String, Ticket> ticketRepository;

    private static final String ERROR_CREATE_PRODUCT = "Error al crear el producto, categoría no existente";
    private static final String ERROR_DELETE_PRODUCT = "Error al borrar el producto";
    private static final String DUPLICATED_ID_ERROR  = "El id pasado como pararametro ya existe, añada otro";
    private static final String ERROR_ID_NONEXISTENT = "El id pasado como pararametro no existe";

    private static final String CATALOG = "Catalog:\n";
    private static final String TAB_SPACE = "\t";

    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String PRICE = "PRICE";

    private static final String DATETIME_FORMAT = "yyyy-MM-dd";

    public ProductController(Repository<Integer, Product> productRepository, Repository<String, Ticket> ticketRepository) {
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
    }

    private int generateAutomaticId() {
        return productRepository.findAll().stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;
    }

    public String decodeQuery(String[] querySplit){
        String name, category, field, newContent;
        double price;
        Integer prodId, maxPers, maxPeople;
        LocalDateTime expirationDate;
        String command = Constants.PROD + " " + querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION];
        switch (querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION]){
            case Constants.PRODUCT_ADD:
                int id;
                try {
                    id = Integer.parseInt(querySplit[Constants.ONE]);  // si es número, perfecto
                    name = querySplit[Constants.TWO].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

                    if ((querySplit[Constants.FOUR].isEmpty()) || (querySplit[Constants.FOUR].equals(Constants.STR_BLANK_SPACE))) {
                        throw new IllegalArgumentException(Constants.ERROR_PRICE);
                    }
                    price = Float.parseFloat(querySplit[Constants.FOUR].replace(Constants.STR_COMMA, Constants.STR_DOT));

                    if (querySplit.length > Constants.FIVE) {
                        maxPers = Integer.parseInt(querySplit[Constants.FIVE]);
                        StringBuilder response = new StringBuilder();
                        response.append(View.getString(this.addProduct(name, querySplit[Constants.THREE], price, id, maxPers), command));
                        response.append(Constants.ENTER_KEY);
                        response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_ADD ));
                        return response.toString();
                    } else {
                        StringBuilder response = new StringBuilder();
                        response.append(View.getString(this.addProduct(name, querySplit[Constants.THREE], price, id), command));
                        response.append(Constants.ENTER_KEY);
                        response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_ADD ));
                        return response.toString();
                    }
                } catch (NumberFormatException e) {
                    id = generateAutomaticId();
                    name = querySplit[Constants.ONE].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

                    if ((querySplit[Constants.THREE].isEmpty()) || (querySplit[Constants.THREE].equals(Constants.STR_BLANK_SPACE))) {
                        throw new IllegalArgumentException(Constants.ERROR_PRICE);
                    }
                    price = Float.parseFloat(querySplit[Constants.THREE].replace(Constants.STR_COMMA, Constants.STR_DOT));

                    if (querySplit.length > Constants.FOUR) {
                        maxPers = Integer.parseInt(querySplit[Constants.FOUR]);
                        StringBuilder response = new StringBuilder();
                            response.append(View.getString(this.addProduct(name, querySplit[Constants.TWO], price, id, maxPers), command));
                            response.append(Constants.ENTER_KEY);
                         response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_ADD ));
                        return response.toString();
                    } else {
                        StringBuilder response = new StringBuilder();
                            response.append(View.getString(this.addProduct(name, querySplit[Constants.TWO], price, id), command));
                            response.append(Constants.ENTER_KEY);
                            response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_ADD ));
                        return response.toString();
                    }
                    // si NO es número, generas el ID
                }



            case Constants.PRODUCT_UPDATE:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                field = querySplit[Constants.QUERY_PRODUCT_POS_FIELD];
                newContent = querySplit[Constants.QUERY_PRODUCT_POS_NEWCONTENT];
                StringBuilder response = new StringBuilder();
                    response.append(View.getString(this.updateProduct(prodId,field,newContent), command));
                    response.append(Constants.ENTER_KEY);
                    response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_UPDATE ));
                return response.toString();

            case Constants.PRODUCT_ADD_FOOD:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                name = querySplit[Constants.QUERY_PRODUCT_POS_NAME];
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDateTime.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]);

                return View.getString(this.addFood(prodId, name, price, maxPeople, expirationDate), command);
            case Constants.PRODUCT_ADD_MEETING:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                name = querySplit[Constants.QUERY_PRODUCT_POS_NAME];
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDateTime.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]);

                return View.getString(this.addMeeting(prodId, name, price, maxPeople, expirationDate), command);
            case Constants.PRODUCT_LIST:

                return View.getString(this.prodList(), command);
            case Constants.PRODUCT_REMOVE:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);

                return View.getString(this.deleteProduct(prodId), command);
            default:
                throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    private Product addProduct(String name, String category, double price, int id) {
        Product product;
        if (Categories.existCategory(category)) {
            product = new Product(id, name, price, Categories.valueOf(category));
            productRepository.add(product.getId(), product);
            return product;
        } else throw new IllegalArgumentException(ERROR_CREATE_PRODUCT);
    }
    private Product addProduct(String name, String category, double price, int id, Integer maxPers) {
        Product product;
        if (Categories.existCategory(category)) {
             product = new Product(id, name, price, Categories.valueOf(category),maxPers);
            productRepository.add(product.getId(), product);
            return product;
        } else throw new IllegalArgumentException(ERROR_CREATE_PRODUCT);
    }

    private Product updateProduct(int id, String field, String newContent) {
        Product productToUpdate = this.productRepository.findByIdOrThrow(id);
        if (productToUpdate == null) throw new IllegalArgumentException(ERROR_ID_NONEXISTENT);
        switch (field) {
            case NAME:
                productToUpdate.setName(newContent);
                break;
            case CATEGORY:
                if (Categories.existCategory(newContent)) {
                    Categories cat = Categories.valueOf(newContent);
                    productToUpdate.setCategory(cat);
                } else return null;
                break;
            case PRICE:
                productToUpdate.setPrice(Double.parseDouble(newContent));
                break;
            default:
                return null;
        }
        return productToUpdate;
    }

    private Product deleteProduct(int prodId) {
        Product productToDelete = this.productRepository.findByIdOrThrow(prodId);
        if (productToDelete != null){
            Collection<Ticket> tickets = this.ticketRepository.findAll();
            for (Ticket ticket : tickets){
                if(!ticket.isClosed() && ticket.containsProduct(productToDelete)){
                    ticket.remove(productToDelete);
                }
            }
            return productToDelete;
        }else throw new IllegalArgumentException(ERROR_ID_NONEXISTENT);
    }


    private String prodList() {
        StringBuilder builder = new StringBuilder();

        builder.append(CATALOG);

        for (Product p : this.productRepository.findAll()) {
            builder.append(TAB_SPACE)
                    .append(p.toString())
                    .append(Constants.ENTER_KEY);
        }

        return builder.toString();
    }

    private Product addFood(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        Food food = new Food(id, name, pricePerPerson, maxPeople, expirationDate);
        this.productRepository.add(food.getId(), food);
        return food;
    }

    private Product addMeeting(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
            Meeting meeting = new Meeting(id, name, pricePerPerson, maxPeople, expirationDate);
            this.productRepository.add(meeting.getId(), meeting);
            return meeting;
    }


}
