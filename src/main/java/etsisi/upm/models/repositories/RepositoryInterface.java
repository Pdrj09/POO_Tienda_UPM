package etsisi.upm.models.repositories;

import java.util.Collection;

public interface RepositoryInterface<K, T> {
    void add(K key, T object);
    T findById(K id);
    T removeById(K id);
    Collection<T> findAll();
}