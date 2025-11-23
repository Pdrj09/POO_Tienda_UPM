package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.CLI;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.util.Categories;
import etsisi.upm.models.Product;
import etsisi.upm.models.Ticket;

import etsisi.upm.models.Meal;
import etsisi.upm.models.Meeting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ProductController {

    private final Repository<Integer, Product> productRepository;

    private static final String ERROR_CREATE_PRODUCT = "Error al crear el producto";


    private static final String CATALOG = "Catalog:\n";
    private static final String TAB_SPACE = "\t";
    private static final int MAX_SIZE = 200;

    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String PRICE = "PRICE";

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static String productAdder(String[] querySplit, ProductController productController) {
        if ((querySplit[Constants.ONE].isEmpty()) || (querySplit[Constants.ONE].equals(Constants.STR_BLANK_SPACE))) {
            throw new IllegalArgumentException("there is no id for product ");
        }
        int id = Integer.parseInt(querySplit[Constants.ONE]);
        String name = querySplit[Constants.TWO].replace(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);

        if ((querySplit[Constants.THREE].isEmpty()) || (querySplit[Constants.THREE].equals(Constants.STR_BLANK_SPACE))) {
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
            response = Constants.okStatus(Constants.PROD, Constants.PRODUCT_ADD);
        }
        return response;
    }

    public String editProcuct(String[] querySplit) {

        StringBuilder builder = new StringBuilder();
        String productEdited = updateProduct(Integer.parseInt(querySplit[Constants.ONE]), querySplit[Constants.TWO], querySplit[Constants.THREE]);
        if (productEdited != null) {
            builder.append(productEdited);
            builder.append(Constants.ENTER_KEY );
            builder.append( Constants.okStatus(Constants.PROD, Constants.PRODUCT_UPDATE));
            return builder.toString();
        } else {
            return Constants.errorStatus(Constants.PROD, Constants.PRODUCT_UPDATE);
        }
    }


    public static String prodDelete(ProductController productController, String query) {
        int id = Integer.parseInt(Constants.deleteSubstring(query, Constants.createGeneralRegex(Constants.PRODUCT_REMOVE)));
        String deletedProd = productController.deleteProduct(id);
        String response = "";
        if (deletedProd != null) {
            System.out.println(deletedProd);
            response = Constants.okStatus(Constants.PROD, Constants.PRODUCT_REMOVE);
        } else {
            // CLI.errorStatus(PROD, PRODUCT_REMOVE);
        }
        return response;
    }

    public ProductController(Repository<Integer, Product> products) {
        this.productRepository = products;
    }

    //here we add a new product to the hashmap of products
    //return true if it didn't exist, else false
    private String addProduct(String name, String category, double price, int id) {
        Product product;
        if (Categories.existCategory(category) && this.productRepository.size() < MAX_SIZE) {
            product = new Product(id, name, price, Categories.valueOf(category));
            productRepository.add(product.getId(), product);
            return product.toString();
        } else return ERROR_CREATE_PRODUCT;
    }

    private String addProduct(String name, String category, double price, int id, int maxPers) {
        Product product;
        if (Categories.existCategory(category)) {
            product = new Product(id, name, price, Categories.valueOf(category),maxPers);
            productRepository.put(product.getId(), product);
            return product.toString();
        } else return ERROR_CREATE_PRODUCT;
    }


    public String updateProduct(int id, String field, String newContent) {
        if (this.productRepository.get(id) == null) return null;
        switch (field) {
            case NAME:
                this.productRepository.get(id).setName(newContent);
                break;
            case CATEGORY:
                if (Categories.existCategory(newContent)) {
                    Categories cat = Categories.valueOf(newContent);
                    this.productRepository.get(id).setCategory(cat);
                } else return null;
                break;
            case PRICE:
                this.productRepository.get(id).setPrice(Double.parseDouble(newContent));
                break;
            default:
                return null;
        }
        return this.productRepository.get(id).toString();
    }

    //here we delete a product from the hashmap of products
    //return true if delete succeed, else false
    public String deleteProduct(int prodId) {
        if (productRepository.containsKey(prodId)) {
            ticket.remove(productRepository.get(prodId));
            this.totalProducts--;
            return productRepository.remove(prodId).toString();
        } else return null;
    }

    public static String addProductToTicket(String ticketId,String cashId, int prodId, int amount) {
        if (productRepository.containsKey(prodId)) {
            ticket.add(productRepository.get(prodId), amount);
        }
        return ticket.toString();
    }

    public String prodList() {
        StringBuilder builder = new StringBuilder();

        builder.append(CATALOG);

        for (Product p : productRepository.values()) {
            builder.append(TAB_SPACE)
                    .append(p.toString())
                    .append(Constants.ENTER_KEY);
        }

        return builder.toString();
    }

    public static String prodAddMeal(String[] querySplit, ProductController productController) {
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

            // negotiation logic response.
            String response = productController.addMeal(id, name, pricePerPerson, maxPeople, expirationDate);

            // if method returns toString of the object and not an error, return an empty chain or a succesfull message.
            if (!response.startsWith(Constants.STR_ERROR)) {
                return "service product added successfully: " + response;
            }
            return response;

        } catch (java.time.format.DateTimeParseException e) {
            return Constants.STR_ERROR + ": Date format incorrect. Use " + DATETIME_FORMAT;
        } catch (NumberFormatException e) {
            return Constants.STR_ERROR + ": ID, Price or Number of persons not valid.";
        } catch (IllegalArgumentException e) {
            return Constants.STR_ERROR + ": " + e.getMessage();
        } catch (Exception e) {
            return Constants.STR_ERROR + ": Unknown error when adding Meal. " + e.getMessage();
        }
    }

    public static String prodAddMeeting(String[] querySplit, ProductController productController) {
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

            // negotiation logic response
            String response = productController.addMeeting(id, name, pricePerPerson, maxPeople, expirationDate);

            // is the toString is returned, then we return the message
            if (!response.startsWith(Constants.STR_ERROR)) {
                return "Product added successfully: " + response;
            }
            return response;

        } catch (java.time.format.DateTimeParseException e) {
            return Constants.STR_ERROR + ": Date format incorrect. Use " + DATETIME_FORMAT;
        } catch (NumberFormatException e) {
            return Constants.STR_ERROR + ": ID, Price or Number of persons not valid.";
        } catch (IllegalArgumentException e) {
            return Constants.STR_ERROR + ": " + e.getMessage();
        } catch (Exception e) {
            return Constants.STR_ERROR + ": Unknown error when adding Meeting " + e.getMessage();
        }
    }

    private String addMeal(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        // ... (validación de ID y MAX_SIZE)
        if (this.productRepository.containsKey(id)) {
            return ERROR_CREATE_PRODUCT + ": the product with the ID " + id + "already exists.";
        }
        if (this.totalProducts >= MAX_SIZE) {
            return ERROR_CREATE_PRODUCT + ": Catalog full";
        }

        try {
            Meal meal = new Meal(id, name, pricePerPerson, maxPeople, expirationDate);
            productRepository.put(meal.getId(), meal);
            this.totalProducts++;
            // return of the toString of the object captured by prodAddMeal
            return meal.toString();
        } catch (IllegalArgumentException e) {
            return ERROR_CREATE_PRODUCT + ": " + e.getMessage();
        }
    }

    private String addMeeting(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        // 1. ID validation
        if (this.productRepository.containsKey(id)) {
            return ERROR_CREATE_PRODUCT + ": the product with the ID " + id + "already exists.";
        }

        // 2. validate the catalog size
        if (this.totalProducts >= MAX_SIZE) {
            return ERROR_CREATE_PRODUCT + ": Catalog full";
        }

        try {
            // 3. meeting instance and date validation
            Meeting meeting = new Meeting(id, name, pricePerPerson, maxPeople, expirationDate);

            // 4. add to the repository
            productRepository.put(meeting.getId(), meeting);
            this.totalProducts++;

            // 5. return the created object
            return meeting.toString();
        } catch (IllegalArgumentException e) {
            // catch the date error by meeting construction
            return ERROR_CREATE_PRODUCT + Constants.STR_DOUBLE_DOT + e.getMessage();
        }
    }

}
