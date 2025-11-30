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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class ProductController {
    private final Repository<Integer, Product> productRepository;
    private final Repository<String, Ticket> ticketRepository;

    public ProductController(Repository<Integer, Product> productRepository, Repository<String, Ticket> ticketRepository) {
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
    }

    private int generateAutomaticId() {
        return productRepository.findAll().stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(Constants.ZERO) + Constants.ONE;
    }

    public String decodeQuery(String[] querySplit){
        String name, category, field, newContent;
        double price;
        Integer prodId, maxPers, maxPeople;
        LocalDateTime expirationDate;
        String command = Constants.PROD + Constants.STR_BLANK_SPACE + querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION];
        switch (querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION]){
            case Constants.PRODUCT_ADD:
                int id;
                try {
                    id = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);  // if number, perfect
                    name = querySplit[Constants.QUERY_PRODUCT_POS_NAME].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

                    if ((querySplit[Constants.QUERY_PRODUCT_POS_PRICE].isEmpty()) || (querySplit[Constants.QUERY_PRODUCT_POS_PRICE].equals(Constants.STR_BLANK_SPACE))) {
                        throw new IllegalArgumentException(Constants.ERROR_PRICE);
                    }
                    price = Float.parseFloat(querySplit[Constants.QUERY_PRODUCT_POS_PRICE].replace(Constants.STR_COMMA, Constants.STR_DOT));

                    if (querySplit.length > Constants.QUERY_PRODUCT_POS_MAXPERS) {
                        maxPers = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPERS]);
                        StringBuilder response = new StringBuilder();
                        response.append(View.getString(this.addProduct(name, querySplit[Constants.QUERY_PRODUCT_POS_CATEGORY], price, id, maxPers), command));
                        response.append(Constants.ENTER_KEY);
                        response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_ADD ));
                        return response.toString();
                    } else {
                        StringBuilder response = new StringBuilder();
                        response.append(View.getString(this.addProduct(name, querySplit[Constants.QUERY_PRODUCT_POS_CATEGORY], price, id), command));
                        response.append(Constants.ENTER_KEY);
                        response.append(Constants.okStatus(Constants.PRODUCT, Constants.PRODUCT_ADD ));
                        return response.toString();
                    }
                } catch (NumberFormatException e) {
                    id = generateAutomaticId();
                    name = querySplit[Constants.QUERY_PRODUCT_POS_NAME].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

                    if ((querySplit[Constants.THREE].isEmpty()) || (querySplit[Constants.THREE].equals(Constants.STR_BLANK_SPACE))) {/// WE HAVE TO REVISE THIS POSITIONS!!!!!!!!!!!!!!!!!!!
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
                    // if it's NOT a number, generate the ID
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
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE_FOODMEETING]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDate.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]).atStartOfDay();

                return View.getString(this.addFood(prodId, name, price, maxPeople, expirationDate), command);
            case Constants.PRODUCT_ADD_MEETING:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                name = querySplit[Constants.QUERY_PRODUCT_POS_NAME];
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE_FOODMEETING]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDate.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]).atStartOfDay();

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
        } else throw new IllegalArgumentException(Constants.ERROR_CREATE_PRODUCT);
    }
    private Product addProduct(String name, String category, double price, int id, Integer maxPers) {
        Product product;
        if (Categories.existCategory(category)) {
             product = new Product(id, name, price, Categories.valueOf(category),maxPers);
            productRepository.add(product.getId(), product);
            return product;
        } else throw new IllegalArgumentException(Constants.ERROR_CREATE_PRODUCT);
    }

    private Product updateProduct(int id, String field, String newContent) {
        Product productToUpdate = this.productRepository.findByIdOrThrow(id);
        if (productToUpdate == null) throw new IllegalArgumentException(Constants.ERROR_ID_NONEXISTENT);
        switch (field) {
            case Constants.NAME:
                productToUpdate.setName(newContent);
                break;
            case Constants.CATEGORY:
                if (Categories.existCategory(newContent)) {
                    Categories cat = Categories.valueOf(newContent);
                    productToUpdate.setCategory(cat);
                } else return null;
                break;
            case Constants.PRICE:
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
        }else throw new IllegalArgumentException(Constants.ERROR_ID_NONEXISTENT);
    }

    private Collection<Product> prodList() {
        return this.productRepository.findAll();
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
