package etsisi.upm.controllers;

import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class CashierController {
    private static final Random ramdon = new Random();
    private final Repository<String, Cashier> repository;

    private static final String CASHIER_PREFIX = "UW";
    private static final String CASH_REGEX = "%s%07d";

    private static final String DUPLICATED_ID_ERROR  = "El id pasado como pararametro ya existe, añada otro";

    public CashierController() {
        repository = new Repository<>();
    }

    public Cashier addCash(String emailCompany, String name) {
        //TODO change cashier
        //TODO comprobar que el id sea correcto
        String cashierId = generateCashierId();
        Cashier cashier =  new Cashier(emailCompany, name);

        repository.add(cashierId, cashier);

        return cashier;
    }

    public Cashier addCashier(String cahierId, String emailCompany, String name) {
        if(repository.findById(cahierId) == null) {
            Cashier cashier =  new Cashier(cahierId, emailCompany, name);
            repository.add(cahierId, cashier);

            return cashier;
        } else {
            throw new IllegalArgumentException(DUPLICATED_ID_ERROR);
        }
    }

    public Cashier removeCashier(String id) {
        return repository.removeById(id);
    }

    public Collection<Cashier> listCashiers() {
        return repository.findAll();
    }

    public Set<String> listTickets(String cashierId) {
        return repository.findById(cashierId).getTickets();
    }

    public Boolean existCashier(String cashierId) {
        return repository.findById(cashierId) != null;
    }

    private String generateCashierId(){
        String id;
        do{
            int num = ramdon.nextInt(10_000_000); //range between 0 and 9.999.999
            id = String.format(CASH_REGEX, CASHIER_PREFIX, num);
        }while(repository.findById(id) != null);
        return id;
    }
}
