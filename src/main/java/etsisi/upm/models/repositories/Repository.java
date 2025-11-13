package etsisi.upm.models.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Repository <K, T> {
    private final Map<K, T> repoMap;

    public Repository() {
        repoMap = new HashMap<>();
    }

    public void add(K key, T object) {

        this.repoMap.put(key, object);
    }

    public T findById(K id) {
        return repoMap.get(id);
    }


    public T removeById(K id) {
        return repoMap.remove(id);
    }


    public Collection<T> findAll(){
        return this.repoMap.values();
    }
}
