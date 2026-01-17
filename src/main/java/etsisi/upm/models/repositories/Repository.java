package etsisi.upm.models.repositories;

import etsisi.upm.util.Constants;

import java.util.Collection;
import etsisi.upm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository <K, T> implements RepositoryInterface<K, T>{
    private final Class<T> entityCLass;
    private final boolean hasMaxSize;
    private final int maxSize;


    public Repository(Class<T> entityCLass) {
        this.entityCLass = entityCLass;
        this.hasMaxSize = false;
        this.maxSize = Constants.NON_SIZE;
    }

    public Repository(Class<T> entityCLass, int maxSize){
        this.entityCLass = entityCLass;
        this.hasMaxSize = true;
        this.maxSize = maxSize;
    }

    @Override
    public void add(K key, T object) {
        if (this.hasMaxSize && findAll().size() >= this.maxSize) {
            throw new IllegalStateException(Constants.ERROR_MAXSIZE + this.maxSize);
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(object);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
            throw new IllegalArgumentException(Constants.DUPLICATED_ID_ERROR);
        } finally {
            session.close();
        }


    }

    @Override
    public T findByIdOrThrow(K id) {
        T founded = findById(id);
        if(founded == null) throw new IllegalArgumentException(Constants.ERROR_NONEXISTEN_ID);
        return founded;
    }

    @Override
    public boolean hasKey(K id) {
        return repoMap.containsKey(id);
    }

    @Override
    public T findById(K id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = session.get(entityCLass, id);
        session.close();
        return entity;
    }

    @Override
    public T removeById(K id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        T entity = session.get(entityCLass, id);
        if (entity != null) session.remove(entity);

        tx.commit();
        session.close();
        return entity;
    }

    @Override
    public Collection<T> findAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<T> result = session
                .createQuery("FROM "+ entityCLass.getSimpleName(), entityCLass)
                .getResultList();

        session.close();

        return result;
    }

    @Override
    public Map<K, T> getMap() {
        List<T> entities = (List<T>) this.findAll();

        Map<K, T> map = new HashMap<>();
        for (T entity : entities) {
            K id;
            try {
                id = (K) entityCLass.getMethod("getId").invoke(entity);
            }catch (Exception e){
                throw new RuntimeException(Constants.ERROR_GET_ID, e);
            }
        }
        return map;
    }
}
