package etsisi.upm.models.repositories;

import etsisi.upm.Constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Repository <K, T> implements RepositoryInterface<K, T>{
    private final Map<K, T> repoMap;
    private final boolean hasMaxSize;
    private final int maxSize;


    public Repository() {
        this.hasMaxSize = false;
        this.maxSize = Constants.NON_SIZE;
        this.repoMap = new HashMap<>();
    }

    public Repository(int maxSize){
        this.hasMaxSize = true;
        this.maxSize = maxSize;
        this.repoMap = new HashMap<>();
    }

    @Override
    public void add(K key, T object) {
        if (!this.hasMaxSize || this.repoMap.size() < this.maxSize) {

            if (!repoMap.containsKey(key)) {
                repoMap.put(key, object);
            } else {
                throw new IllegalArgumentException(Constants.DUPLICATED_ID_ERROR);
            }
        } else {
            throw new IllegalStateException(Constants.ERROR_MAXSIZE + this.maxSize);
        }
    }

    @Override
    public T findByIdOrThrow(K id) {
        T founded = repoMap.get(id);
        if(founded == null) throw new IllegalArgumentException(Constants.ERROR_NONEXISTEN_ID);
        return founded;
    }

    @Override
    public T findById(K id) {
        return repoMap.get(id);
    }

    @Override
    public T removeById(K id) {
        return repoMap.remove(id);
    }

    @Override
    public Collection<T> findAll(){
        return this.repoMap.values();
    }

    @Override
    public Map<K, T> getMap() {
        return this.repoMap;
    }
}
