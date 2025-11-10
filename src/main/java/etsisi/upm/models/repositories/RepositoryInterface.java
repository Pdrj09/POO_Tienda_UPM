package etsisi.upm.models.repositories;

import java.util.Collection;

public interface RepositoryInterface<T> {
    void add(T object);
    T findById(String id);
    T removeById(String id);
    Collection<T> findAll();
}