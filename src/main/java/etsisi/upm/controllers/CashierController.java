package etsisi.upm.controllers;

import etsisi.upm.Constants;
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
            
        } else if (query.startsWith(Constants.CASH_REMOVE)) {
            cashierRegex.append(Constants.CASH_REMOVE)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);

        } else if (query.startsWith(Constants.CASH_LIST)) {
            cashierRegex.append(Constants.CASH_LIST)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);
            
        } else if (query.startsWith(Constants.CASH_TICKETS)) {
            cashierRegex.append(Constants.CASH_TICKETS)
                        .append(Constants.REGEX_BLANK_SPACE);

            query = query.replaceFirst(cashierRegex.toString(), Constants.STR_EMPTY);

        } else {
            throw  new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }

        return "";
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
