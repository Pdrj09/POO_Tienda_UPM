package etsisi.upm.models.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Repository <K, T> implements RepositoryInterface<K, T>{
    private final Map<K, T> repoMap;
    private final boolean hasMaxSize;
    private final int maxSize;

    private static final String DUPLICATED_ID_ERROR  = "El id pasado como pararametro ya existe, añada otro";
    private static final String ERROR_MAXSIZE = "Repositorio lleno, tamaño máximo de ";

    public Repository() {
        this.hasMaxSize = false;
        this.maxSize = -1;
        this.repoMap = new HashMap<>();
    }

    public Repository(int maxSize){
        this.hasMaxSize = true;
        this.maxSize = maxSize;
        this.repoMap = new HashMap<>();
    }

    @Override
    public void add(K key, T object) {
        if(!this.hasMaxSize || this.repoMap.size()<this.maxSize){
            if(this.findById(key)==null){
                this.repoMap.put(key, object);
            }else throw new IllegalArgumentException(DUPLICATED_ID_ERROR);
        }else{
            throw new IllegalStateException(ERROR_MAXSIZE+this.maxSize);
        }
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
