package etsisi.upm.controllers;

import etsisi.upm.models.*;
import etsisi.upm.util.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.util.Categories;

import etsisi.upm.util.Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductController {
    private final Repository<String, Sellable> productRepository;
    private final Repository<String, Ticket<?>> ticketRepository;

    public ProductController(Repository<String, Sellable> productRepository, Repository<String,
            Ticket<?>> ticketRepository) {
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
    }

    public String decodeQuery(String[] querySplit){
        String name, category, field, newContent, prodId;
        double price;
        Integer maxPers, maxPeople, index;
        LocalDateTime expirationDate;
        String command = Constants.PROD + Constants.STR_BLANK_SPACE + querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION];
        switch (querySplit[Constants.QUERY_PRODUCT_POS_INSTRUCTION]){
            case Constants.PRODUCT_ADD:
                if(querySplit.length==Constants.QUERY_PRODUCT_LENGTH_WITHCUTOMIZATIONS || querySplit.length==Constants.QUERY_PRODUCT_LENGTH_WITHOUTCUTOMIZATIONS ||
                querySplit.length==Constants.QUERY_PRODUCT_LENGTH_WITHOUTID) {

                    if (Utilities.isPositiveInteger(querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID])) {
                        prodId =querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID];
                        index = Constants.PROD_WITH_ID_INDEX;
                    } else {
                        prodId = String.valueOf(Utilities.generateAutomaticId(productRepository));
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
                //}~~~~~~~~~~~~~~~~~0~~~~~~~~~~~~~~~~~~~~~~~{
                }else if(querySplit.length == Constants.QUERY_PRODUCT_LENGTH_SERVICE){
                    name = Utilities.cleanName(querySplit[Constants.QUERY_SERVICE_POS_NAME]);
                    category = querySplit[Constants.QUERY_SERVICE_POS_CATEGORY];
                    return View.getString(this.addService(name, category), command);
                }else {
                    throw new IllegalArgumentException(Constants.ERROR_INVALID_NUMBER_ARGUMENTS);
                }
            case Constants.PRODUCT_UPDATE:

                prodId = querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID];
                field = querySplit[Constants.QUERY_PRODUCT_POS_FIELD];
                newContent = querySplit[Constants.QUERY_PRODUCT_POS_NEWCONTENT];
                StringBuilder response = new StringBuilder();
                    response.append(View.getString(this.updateProduct(prodId,field,newContent), command));
                    response.append(Constants.ENTER_KEY);
                return response.toString();

            case Constants.PRODUCT_ADD_FOOD:

                prodId = querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID];
                name = Utilities.cleanName(querySplit[Constants.QUERY_PRODUCT_POS_NAME]);
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE_FOODMEETING]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDate.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]).atStartOfDay();

                return View.getString(this.addFood(prodId, name, price, maxPeople, expirationDate), command);
            case Constants.PRODUCT_ADD_MEETING:

                prodId = querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID];
                name = Utilities.cleanName(querySplit[Constants.QUERY_PRODUCT_POS_NAME]);
                price = Double.parseDouble(querySplit[Constants.QUERY_PRODUCT_POS_PRICE_FOODMEETING]);
                maxPeople = Integer.parseInt(querySplit[Constants.QUERY_PRODUCT_POS_MAXPEOPLE]);
                expirationDate = LocalDate.parse(querySplit[Constants.QUERY_PRODUCT_POS_EXPIRATION]).atStartOfDay();

                return View.getString(this.addMeeting(prodId, name, price, maxPeople, expirationDate), command);
            case Constants.PRODUCT_LIST:

                return View.getString(this.prodList(), command);
            case Constants.PRODUCT_REMOVE:

                prodId = querySplit[Constants.QUERY_PRODUCT_POS_PRODUCTID];

                return View.getString(this.deleteProduct(prodId), command);
            default:
                throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    private Sellable addProduct(String name, String category, double price, String id, Integer maxPers) {
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
            long nextIdNum = productRepository.findAll().stream()
                    .filter(p -> p instanceof ServiceProduct)
                    .count() + 1;

            String trueId = nextIdNum + "S";
            //categoria del enum
            Categories cat = Categories.valueOf(categoryName.toUpperCase()); //in upper case
            if(categoryName.equals(Constants.STR_SERVICE_TRANSPORT) ||
                    categoryName.equals(Constants.STR_SERVICE_SHOW) ||
                    categoryName.equals(Constants.STR_SERVICE_INSURANCE))
            {
                ServiceProduct genericService = new ServiceProduct(trueId, expiration, cat);
                this.productRepository.add(trueId, genericService);
                genericService.setActive(true);
                genericService.setName(cat.toString());
                return genericService;
            }else throw new IllegalArgumentException(Constants.ERROR_SERVICE_CATEGORY);
        }

    }

    private Sellable updateProduct(String id, String field, String newContent) {
        Sellable productToUpdate = this.productRepository.findByIdOrThrow(id);
        Object newValue;
        if (productToUpdate == null) throw new IllegalArgumentException(Constants.ERROR_ID_NONEXISTENT);
        switch (field) {
            case Constants.NAME:
                newValue = Utilities.cleanName(newContent);
                break;
            case Constants.CATEGORY:
                if (Categories.existCategory(newContent)) {
                    newValue = Categories.valueOf(newContent);
                } else return null;
                break;
            case Constants.PRICE:
                newValue = Double.parseDouble(newContent);
                break;
            default:
                return null;
        }
        return this.productRepository.updateById(id, field, newValue);
    }

    private Sellable deleteProduct(String prodId) {
        Sellable productToDelete = this.productRepository.findByIdOrThrow(prodId);
        if (productToDelete != null){
            Collection<Ticket<? extends Sellable>> tickets = this.ticketRepository.findAll();
            boolean toArchive = false;
            for (Ticket<? extends Sellable> ticket : tickets){
                if(!ticket.isClosed() && ticket.containsProduct(productToDelete)){
                    ticket.remove(productToDelete);
                }
                else if (ticket.isClosed() && ticket.containsProduct(productToDelete)){
                    toArchive = true;
                }
            }
            if (toArchive) {
                this.productRepository.updateById(prodId, "active", false);
                productToDelete.archive();
            }else
                this.productRepository.removeById(prodId);
            return productToDelete;
        }else throw new IllegalArgumentException(Constants.ERROR_ID_NONEXISTENT);
    }

    private Collection<Sellable> prodList() {
        List<Sellable> activeProducts = new ArrayList<>();
        for (Sellable p : this.productRepository.findAll()){
            if (p.isActive())
                activeProducts.add(p);
        }
        return activeProducts;
    }

    private Sellable addFood(String id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        ServiceProduct food = ServiceProduct.createFood(id, name, pricePerPerson, maxPeople, expirationDate);
        this.productRepository.add(food.getId(), food);
        return food;
    }

    private Sellable addMeeting(String id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
            ServiceProduct meeting = ServiceProduct.createMeeting(id, name, pricePerPerson, maxPeople, expirationDate);
            this.productRepository.add(meeting.getId(), meeting);
            return meeting;
    }

}
