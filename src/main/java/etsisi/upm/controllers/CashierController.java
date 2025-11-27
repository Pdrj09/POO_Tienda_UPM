package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class CashierController {
    private static final Random randon = new Random();
    private final Repository<String, Cashier> repository;

    private static final String CASHIER_PREFIX = "UW";
    private static final String CASH_REGEX = "%s%07d";

    public CashierController(Repository<String, Cashier> repository) {
        this.repository = repository;
    }

    public String cashierQuery(String query) {
        StringBuilder cashierRegex = new StringBuilder();
        cashierRegex.append(Constants.REGEX_INIT);

        if (query.startsWith(Constants.CASH_ADD)) {
            cashierRegex.append(Constants.CASH_ADD)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);
            try {
                cashierRegex.append(Constants.CASH_ADD)
                        .append(Constants.REGEX_BLANK_SPACE);

                query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);

                String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);

                if (querySplit.length == Constants.FOUR) {
                    String id = querySplit[Constants.ONE];
                    String name = querySplit[Constants.TWO].replaceAll(Constants.REGEX_DOUBLE_QUOTE, Constants.STR_EMPTY);
                    String mail = querySplit[Constants.THREE];

                    Cashier newCash = addCashier(id, mail, name);

                    return View.getString(newCash);
                } else if (querySplit.length == Constants.THREE) {
                    String name = querySplit[Constants.ONE];
                    String mail = querySplit[Constants.TWO];

                    Cashier newCash = addCash(mail, name);

                    return View.getString(newCash);
                } else {
                    throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);

                }


            } catch (Exception e) {
                return e.getMessage();
            }
            
        } else if (query.startsWith(Constants.CASH_REMOVE)) {
            cashierRegex.append(Constants.CASH_REMOVE)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);

            String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);

            if (querySplit.length != Constants.TWO) {
                throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
            }

            Cashier cashier = removeCashier(querySplit[Constants.ONE]);

            return View.getString(cashier);

        } else if (query.startsWith(Constants.CASH_LIST)) {
            cashierRegex.append(Constants.CASH_LIST)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);

            if(query.equals(Constants.STR_EMPTY)) {
                Collection<Cashier> cashiers = listCashiers();
            } else {
                // TODO dar un codigo de error personalizado
                throw  new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
            }
            

            StringBuilder builder = new StringBuilder();

            Collection<Cashier> cashiers = listCashiers();

            for (Cashier cashier : cashiers) {
                cashierRegex.append(View.getString(cashier));
            }

            return builder.toString();


        } else if (query.startsWith(Constants.CASH_TICKETS)) {
            cashierRegex.append(Constants.CASH_TICKETS)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);

        } else {
            throw  new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }

        return "";
            String[] querySplit = query.split(Constants.REGEX_TO_SPLIT);
            if (querySplit.length != Constants.TWO) {
                throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
            }

            Collection<String> tickets = listTickets(querySplit[Constants.ONE]);

            StringBuilder builder = new StringBuilder();

            for(String ticket : tickets) {
                builder.append(View.getString(ticket));
            }

            return builder.toString();

        } else {
            throw  new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    private Cashier addCash(String emailCompany, String name) {
        String cashierId = generateCashierId();
        Cashier cashier =  Cashier.create(cashierId, emailCompany, name);

        repository.add(cashierId, cashier);

        return cashier;
    }

    private Cashier addCashier(String cahierId, String emailCompany, String name) {
        Cashier cashier =  Cashier.create(cahierId, emailCompany, name);

        repository.add(cahierId, cashier);

        return cashier;
    }

    private Cashier removeCashier(String id) {
        return repository.removeById(id);
    }

    private Collection<Cashier> listCashiers() {
        return repository.findAll();
    }

    private Set<String> listTickets(String cashierId) {
        return repository.findById(cashierId).getTickets();
    }

    private Boolean existCashier(String cashierId) {
        return repository.findById(cashierId) != null;
    }

    private String generateCashierId(){
        String id;
        do{
            int num = randon.nextInt(10_000_000); //range between 0 and 9.999.999
            id = String.format(CASH_REGEX, CASHIER_PREFIX, num);
        }while(repository.findById(id) != null);
        return id;
    }
}
