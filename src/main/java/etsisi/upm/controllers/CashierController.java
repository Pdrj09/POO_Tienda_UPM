package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.Ticket;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.util.Utilities;

import java.security.InvalidParameterException;
import java.util.*;

public class CashierController {
    private final Repository<String, Cashier> repository;

    public CashierController(Repository<String, Cashier> repository) {
        this.repository = repository;
    }

    public String cashierQuery(String[] querySplit) {
        StringBuilder cashierRegex = new StringBuilder();
        cashierRegex.append(Constants.REGEX_INIT);
        String instruction = querySplit[Constants.QUERY_CASH_POS_INSTRUCTION];
        String command = querySplit[Constants.QUERY_CASH_POS_CLASS] + Constants.STR_BLANK_SPACE + querySplit[Constants.QUERY_CASH_POS_INSTRUCTION];
        switch (instruction) {
            case Constants.CASH_ADD:
                if (querySplit.length == Constants.QUERY_CASH_LENGTH_WITHID) {
                    String id = querySplit[Constants.QUERY_CASH_POS_ID];
                    String name = Utilities.cleanName(querySplit[Constants.QUERY_CASH_POS_NAME]);
                    String mail = Utilities.cleanName(querySplit[Constants.QUERY_CASH_POS_EMAIL]);

                    Cashier newCash = addCashier(id, mail, name);

                    return View.getString(newCash, command);
                } else if (querySplit.length == Constants.QUERY_CASH_LENGTH_WITHOUTID) {
                    int index = Constants.ONE;
                    String name = Utilities.cleanName(querySplit[Constants.QUERY_CASH_POS_NAME - index]);
                    String mail = Utilities.cleanName(querySplit[Constants.QUERY_CASH_POS_EMAIL - index]);

                    Cashier newCash = addCashier(mail, name);

                    return View.getString(newCash, command);
                } else {
                    throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
                }

            case Constants.CASH_REMOVE:

                Cashier cashier = removeCashier(querySplit[Constants.QUERY_CASH_POS_ID]);

                return View.getString(cashier, command);
            case Constants.CASH_LIST:

                if (querySplit.length == Constants.QUERY_CASH_POS_INSTRUCTION + Constants.ONE) {
                    return View.getString(listCashiers(), command);
                } else {
                    throw new IllegalArgumentException(Constants.ERROR_TOOMANY_ARGUMENTS);
                }
            case Constants.CASH_TICKETS:

                return View.getString(listTickets(querySplit[Constants.QUERY_CASH_POS_ID]), command);
            default:
                throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    private Cashier addCashier(String emailCompany, String name) {
        String cashierId = generateCashierId();
        return addCashier(cashierId,emailCompany,name);
    }

    private Cashier addCashier(String cashierId, String emailCompany, String name) {
        if(!cashierId.matches(Constants.REGEX_CASH_ID)) throw new InvalidParameterException(Constants.ERROR_INVALID_ID);

        Cashier cashier =  Cashier.create(cashierId, Utilities.cleanName(emailCompany), Utilities.cleanName(name));

        repository.add(cashierId, cashier);

        return cashier;
    }

    private Cashier removeCashier(String id) {
        return repository.removeById(id);
    }

    private Collection<Cashier> listCashiers() {
        return repository.findAll();
    }

    private Collection<Ticket> listTickets(String cashierId) {
        return repository.findByIdOrThrow(cashierId).getTickets();
    }

    private String generateCashierId(){
        String id;
        do{
            int num = Constants.RANDOM.nextInt(Constants.MAX_RANDOM_CASH_ID); //range between 0 and 9.999.999
            id = String.format(Constants.CASH_REGEX, Constants.CASHIER_PREFIX, num);
        }while(repository.findById(id) != null);
        return id;
    }
}