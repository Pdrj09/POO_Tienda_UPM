package etsisi.upm.models.repositories;

import etsisi.upm.models.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository implements RepositoryInterface<Product> {
    private final Map<String, Product> productMap;

    public ProductRepository() {
        productMap = new HashMap<>();
    }

    @Override
    public void add(Product prod) {
        this.productMap.put(Integer.toString(prod.getId()), prod);
    }

    @Override
    public Product findById(String id) {
        return productMap.get(id);
    }


    @Override
    public Product removeById(String id) {
        return productMap.remove(id);
    }


    @Override
    public Collection<Product> findAll(){
        return this.productMap.values();
    }
}
