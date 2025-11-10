package etsisi.upm.models.repositories;

import etsisi.upm.models.users.Cashier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CashRepository implements RepositoryInterface<Cashier> {
    private final Map<String, Cashier> cashierMap;

    public CashRepository() {
        this.cashierMap = new HashMap<>();
    }

    @Override
    public void add(Cashier cash) {
        cashierMap.put(cash.getId(), cash);
    }

    @Override
    public Cashier findById(String id) {
        return cashierMap.get(id);
    }

    @Override
    public Cashier removeById(String id) {
        return cashierMap.remove(id);
    }

    public Collection<Cashier> findAll() {
        return cashierMap.values();
    }
}
