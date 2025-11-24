package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.util.Categories;
import etsisi.upm.models.Product;

import etsisi.upm.models.Meal;
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
    public static final int MAX_SIZE = 200;

    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String PRICE = "PRICE";

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    public ProductController(Repository<Integer, Product> productRepository, Repository<String, Ticket> ticketRepository) {
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
    }

    public static Product productAdder(String[] querySplit, ProductController productController) {
        if ((querySplit[Constants.ONE].isEmpty()) || (querySplit[Constants.ONE].equals(Constants.STR_BLANK_SPACE))) {
            throw new IllegalArgumentException("there is no id for product ");
        }
        int id = Integer.parseInt(querySplit[Constants.ONE]);
        String name = querySplit[Constants.TWO].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

        if ((querySplit[Constants.THREE].isEmpty()) || (querySplit[Constants.THREE].equals(Constants.STR_BLANK_SPACE))) {
            throw new IllegalArgumentException("The product has to have a price");
        }
        float price = Float.parseFloat(querySplit[Constants.FOUR].replace(Constants.STR_COMMA, Constants.STR_DOT));

        Product response;

        if (querySplit.length > Constants.FIVE) {
            int maxPers = Integer.parseInt(querySplit[Constants.FIVE]);
            response = productController.addProduct(name, querySplit[Constants.THREE], price, id, maxPers);
        } else {

            response = productController.addProduct(name, querySplit[Constants.THREE], price, id, null);
            System.out.println(response);
        }
        return response;
    }

    public Product editProduct(String[] querySplit) {

        Product productEdited = updateProduct(Integer.parseInt(querySplit[Constants.ONE]), querySplit[Constants.TWO], querySplit[Constants.THREE]);
        if (productEdited != null) {
            return productEdited;
        } else {
            throw new IllegalStateException(Constants.errorStatus(Constants.TICKET, Constants.TICKET_NEW));
        }
    }


    public Product prodDelete(ProductController productController, String query) {
        int id = Integer.parseInt(Constants.deleteSubstring(query, Constants.createGeneralRegex(Constants.PRODUCT_REMOVE)));
        Product deletedProd = deleteProduct(id);
        String response = "";
        if (deletedProd != null) {
            return deletedProd;
        } else {
            throw new RuntimeException();
        }
    }



    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    private Product addProduct(String name, String category, double price, int id, Integer maxPers) {
        Product product;
        if (Categories.existCategory(category)) {
            if(maxPers==null) product = new Product(id, name, price, Categories.valueOf(category));
            else product = new Product(id, name, price, Categories.valueOf(category),maxPers);
            productRepository.add(product.getId(), product);
            return product;
        } else throw new IllegalArgumentException(ERROR_CREATE_PRODUCT);
    }

    public Product updateProduct(int id, String field, String newContent) {
        Product productToUpdate = this.productRepository.findById(id);
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

    public Product deleteProduct(int prodId) {
        Product productToDelete = this.productRepository.findById(prodId);
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


    public String prodList() {
        StringBuilder builder = new StringBuilder();

        builder.append(CATALOG);

        for (Product p : this.productRepository.findAll()) {
            builder.append(TAB_SPACE)
                    .append(p.toString())
                    .append(Constants.ENTER_KEY);
        }

        return builder.toString();
    }

    public Product prodAddMeal(String[] querySplit) {
        try {
            if (querySplit.length < Constants.FIVE + Constants.ONE) {
                throw new IllegalArgumentException("Some parameters are missing to create Meal.");
            }

            int id = Integer.parseInt(querySplit[Constants.ONE]);
            String name = querySplit[Constants.TWO].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);
            double pricePerPerson = Double.parseDouble(querySplit[Constants.THREE].replace(Constants.STR_COMMA, Constants.STR_DOT));

            String dateString = querySplit[Constants.FOUR];
            LocalDateTime expirationDate = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATETIME_FORMAT));

            int maxPeople = Integer.parseInt(querySplit[Constants.FIVE]);

            Product response = addMeal(id, name, pricePerPerson, maxPeople, expirationDate);

            return response;

        } catch (java.time.format.DateTimeParseException e) {
            throw new DateTimeException(Constants.STR_ERROR + ": Date format incorrect. Use " + DATETIME_FORMAT);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(Constants.STR_ERROR + ": ID, Price or Number of persons not valid.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Constants.STR_ERROR + ": " + e.getMessage());
        }
    }

    public Product prodAddMeeting(String[] querySplit) {
        try {
            // 6 elements expected: [0]prod, [1]addMeeting, [2]id, [3]name, [4]price, [5]date, [6]maxPeople
            if (querySplit.length < Constants.FIVE + 1) {
                throw new IllegalArgumentException("some parameters are missing to create the Meeting");
            }

            int id = Integer.parseInt(querySplit[Constants.ONE]);
            String name = querySplit[Constants.TWO].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);
            double pricePerPerson = Double.parseDouble(querySplit[Constants.THREE].replace(Constants.STR_COMMA, Constants.STR_DOT));

            String dateString = querySplit[Constants.FOUR];
            LocalDateTime expirationDate = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATETIME_FORMAT));

            int maxPeople = Integer.parseInt(querySplit[Constants.FIVE]);

            Product response = addMeeting(id, name, pricePerPerson, maxPeople, expirationDate);

            return response;

        } catch (java.time.format.DateTimeParseException e) {
            throw new DateTimeException(Constants.STR_ERROR + ": Date format incorrect. Use " + DATETIME_FORMAT);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(Constants.STR_ERROR + ": ID, Price or Number of persons not valid.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Constants.STR_ERROR + ": " + e.getMessage());
        }
    }

    private Product addMeal(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        Meal meal = new Meal(id, name, pricePerPerson, maxPeople, expirationDate);
        this.productRepository.add(meal.getId(), meal);
        return meal;
    }

    private Product addMeeting(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
            Meeting meeting = new Meeting(id, name, pricePerPerson, maxPeople, expirationDate);
            this.productRepository.add(meeting.getId(), meeting);
            return meeting;
    }


}
