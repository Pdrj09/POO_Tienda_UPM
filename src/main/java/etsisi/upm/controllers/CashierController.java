package etsisi.upm.controllers;

import etsisi.upm.util.Constants;
import etsisi.upm.io.View;
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
                int index;
                String cashierId;
                if (querySplit.length == Constants.QUERY_CASH_LENGTH_WITHID) {
                    index = Constants.CASH_WITH_ID_INDEX;
                    cashierId = querySplit[Constants.QUERY_CASH_POS_ID];
                } else if (querySplit.length == Constants.QUERY_CASH_LENGTH_WITHOUTID) {
                    index = Constants.CASH_WITHOUT_ID_INDEX;
                    cashierId = generateCashierId();
                } else {
                    throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
                }
                String name = Utilities.cleanName(querySplit[Constants.QUERY_CASH_POS_NAME - index]);
                String mail = Utilities.cleanName(querySplit[Constants.QUERY_CASH_POS_EMAIL - index]);

                Cashier newCash = addCashier(cashierId,mail, name);

                return View.getString(newCash, command);

            case Constants.CASH_REMOVE:

                Cashier cashier = removeCashier(querySplit[Constants.QUERY_CASH_POS_ID]);

                return View.getString(cashier, command);
            case Constants.CASH_LIST:

                if (querySplit.length == Constants.QUERY_CASH_LIST_LENGTH) {
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

    private Collection<String> listTickets(String cashierId) {
        return repository.findByIdOrThrow(cashierId).getTicketsSummaryList();
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