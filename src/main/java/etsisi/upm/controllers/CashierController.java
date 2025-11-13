package etsisi.upm.controllers;

import etsisi.upm.models.repositories.CashRepository;
import etsisi.upm.models.repositories.RepositoryInterface;
import etsisi.upm.models.users.Cashier;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CashierController {
    private static final Random ramdon = new Random();
    private final RepositoryInterface<Cashier> repository;

    private static final String CASHIER_PREFIX = "UW";
    private static final String CASH_REGEX = "%s%07d";

    public CashierController() {
        repository = new CashRepository();
    }

    public void addCash(String emailCompany, String name) {

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
