package etsisi.upm.models.repositories;

import java.util.Collection;
import java.util.Map;

public interface RepositoryInterface<K, T> {
    void add(K key, T object);
    T findByIdOrThrow(K id);
    T findById(K id);
    T removeById(K id);
    Collection<T> findAll();
    Map<K, T> getMap();
}