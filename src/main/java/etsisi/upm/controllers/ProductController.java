package etsisi.upm.controllers;

import etsisi.upm.models.*;
import etsisi.upm.util.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.util.Categories;

import etsisi.upm.util.Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public class ProductController {
    private final Repository<Integer, Sellable> productRepository;
    private final Repository<String, Ticket<?>> ticketRepository;

    public ProductController(Repository<Integer, Sellable> productRepository, Repository<String,
            Ticket<?>> ticketRepository) {
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
    }

    public String decodeQuery(String[] querySplit){
        String name, category, field, newContent;
        double price;
        Integer prodId, maxPers, maxPeople, index;
        LocalDateTime expirationDate;
        String command = Constants.PROD + Constants.STR_BLANK_SPACE + querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION];
        switch (querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION]){
            case Constants.PRODUCT_ADD:
                if(querySplit.length==Constants.QUERY_PRODUCT_LENGTH_WITHCUTOMIZATIONS || querySplit.length==Constants.QUERY_PRODUCT_LENGTH_WITHOUTCUTOMIZATIONS ||
                querySplit.length==Constants.QUERY_PRODUCT_LENGTH_WITHOUTID) {

                    if (Utilities.isPositiveInteger(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID])) {
                        prodId = Integer.valueOf(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                        index = Constants.PROD_WITH_ID_INDEX;
                    } else {
                        prodId = Utilities.generateAutomaticId(productRepository);
                        index = Constants.PROD_WITHOUT_ID_INDEX;
                    }

                    name = Utilities.cleanName(querySplit[Constants.QUERY_PRODUCT_POS_NAME - index]);
                    category = querySplit[Constants.QUERY_PRODUCT_POS_CATEGORY - index];

                    if ((querySplit[Constants.QUERY_PRODUCT_POS_PRICE - index].isEmpty()) ||
                            (querySplit[Constants.QUERY_PRODUCT_POS_PRICE - index].equals(Constants.STR_BLANK_SPACE))) {
                        throw new IllegalArgumentException(Constants.ERROR_PRICE);
                    }
                    price = Float.parseFloat(querySplit[Constants.QUERY_PRODUCT_POS_PRICE - index].
                            replace(Constants.STR_COMMA, Constants.STR_DOT));

                    if (querySplit.length == Constants.QUERY_PRODUCT_LENGTH_WITHCUTOMIZATIONS - index) {
                        maxPers = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPERS - index]);
                    } else maxPers = null;

                    return View.getString(this.addProduct(name, category, price, prodId, maxPers), command);
                    ////////////////////////////////////////
                }else if(querySplit.length == Constants.QUERY_PRODUCT_LENGTH_SERVICE){
                    name = Utilities.cleanName(querySplit[Constants.QUERY_SERVICE_POS_NAME]);
                    category = querySplit[Constants.QUERY_SERVICE_POS_CATEGORY];
                    return View.getString(this.addService(name, category), command);
                }else {
                    throw new IllegalArgumentException(Constants.ERROR_INVALID_NUMBER_ARGUMENTS);
                }
            case Constants.PRODUCT_UPDATE:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                field = querySplit[Constants.QUERY_PRODUCT_POS_FIELD];
                newContent = querySplit[Constants.QUERY_PRODUCT_POS_NEWCONTENT];
                StringBuilder response = new StringBuilder();
                    response.append(View.getString(this.updateProduct(prodId,field,newContent), command));
                    response.append(Constants.ENTER_KEY);
                return response.toString();

            case Constants.PRODUCT_ADD_FOOD:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                name = Utilities.cleanName(querySplit[Constants.QUERY_PRODUCT_POS_NAME]);
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE_FOODMEETING]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDate.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]).atStartOfDay();

                return View.getString(this.addFood(prodId, name, price, maxPeople, expirationDate), command);
            case Constants.PRODUCT_ADD_MEETING:

                prodId = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID]);
                name = Utilities.cleanName(querySplit[Constants.QUERY_PRODUCT_POS_NAME]);
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

    private Sellable addProduct(String name, String category, double price, int id, Integer maxPers) {
        Product product;
        if (Categories.existCategory(category)) {
            if (maxPers != null) product = new Product(id, name, price, Categories.valueOf(category),maxPers);
            else product = new Product(id, name, price, Categories.valueOf(category));
            productRepository.add(product.getId(), product);
            return product;
        } else throw new IllegalArgumentException(Constants.ERROR_CREATE_PRODUCT);
    }

    private Sellable addService(String dateSTR, String categoryName) {
        //parseo yyyy-mm-dd
        LocalDateTime expiration = java.time.LocalDate.parse(dateSTR).atStartOfDay();
        if(expiration.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException(Constants.ERROR_SERVICE_DATE);
        }else{
            //ID starting with 1
            Collection<Sellable> allProducts = productRepository.findAll();
            int nextId = 1;
            for (Sellable product : allProducts) {
                if( product instanceof ServiceProduct){
                    nextId ++;
                }
            }
            //categoria del enum
            Categories cat = Categories.valueOf(categoryName.toUpperCase()); //in upper case
            if(categoryName.equals(Constants.STR_SERVICE_TRANSPORT) ||
                    categoryName.equals(Constants.STR_SERVICE_SHOW) ||
                    categoryName.equals(Constants.STR_SERVICE_INSURANCE))
            {
                ServiceProduct genericService = new ServiceProduct(nextId, expiration, cat);
                return genericService;
            }else throw new IllegalArgumentException(Constants.ERROR_SERVICE_CATEGORY);
        }

    }

    private Sellable updateProduct(int id, String field, String newContent) {
        Sellable productToUpdate = this.productRepository.findByIdOrThrow(id);
        if (productToUpdate == null) throw new IllegalArgumentException(Constants.ERROR_ID_NONEXISTENT);
        switch (field) {
            case Constants.NAME:
                productToUpdate.setName(Utilities.cleanName(newContent));
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

    private Sellable deleteProduct(int prodId) {
        Sellable productToDelete = this.productRepository.findByIdOrThrow(prodId);
        if (productToDelete != null){
            Collection<Ticket<? extends Sellable>> tickets = this.ticketRepository.findAll();
            for (Ticket<? extends Sellable> ticket : tickets){
                if(!ticket.isClosed() && ticket.containsProduct(productToDelete)){
                    ticket.remove(productToDelete);
                }
            }
            return productToDelete;
        }else throw new IllegalArgumentException(Constants.ERROR_ID_NONEXISTENT);
    }

    private Collection<Sellable> prodList() {
        return this.productRepository.findAll();
    }

    private Sellable addFood(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        ServiceProduct food = ServiceProduct.createFood(id, name, pricePerPerson, maxPeople, expirationDate);
        this.productRepository.add(food.getId(), food);
        return food;
    }

    private Sellable addMeeting(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
            ServiceProduct meeting = ServiceProduct.createMeeting(id, name, pricePerPerson, maxPeople, expirationDate);
            this.productRepository.add(meeting.getId(), meeting);
            return meeting;
    }

}
