package etsisi.upm.models.repositories;

import etsisi.upm.util.Constants;

import java.util.*;

import etsisi.upm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        if (this.hasMaxSize && findAll().size() >= this.maxSize)
            throw new IllegalStateException(Constants.ERROR_MAXSIZE + this.maxSize);
        if (hasKey(key)) {
            throw new IllegalArgumentException(Constants.DUPLICATED_ID_ERROR);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(object);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error de persistencia: " + e.getMessage());
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
        return findById(id)!=null;
    }

    @Override
    public T findById(K id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = session.createQuery("from " + entityCLass.getName() + " where id = :id", entityCLass).setParameter("id", id).uniqueResult();
        if (entity == null && id instanceof String) {
            entity = session.createQuery("from " + entityCLass.getName() + " where id LIKE :partialId", entityCLass).setParameter("partialId", "%-" + id).uniqueResult();
        }
        session.close();
        return entity;
    }

    @Override
    public T updateById(K id, String fieldName, Object newValue){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try{
            T entity = session.createQuery("from " + entityCLass.getName() + " where id = :id", entityCLass)
                    .setParameter("id", id)
                    .uniqueResult();
            if (entity == null)
                throw new IllegalArgumentException(Constants.ERROR_NONEXISTEN_ID);
            String setterName = "set" + fieldName.charAt(0) + fieldName.substring(1).toLowerCase();

            //we invoke the setter. MAGIC
            Class<?> valueType = newValue.getClass();
            if (valueType == Double.class)
                valueType = double.class;
            if (valueType == Integer.class)
                valueType = int.class;
            java.lang.reflect.Method setter = entityCLass.getMethod(setterName, valueType);
            setter.invoke(entity, newValue);

            tx.commit();
            session.close();
            return entity;
        }catch(Exception e){
            if (tx!=null)
                tx.rollback();
            System.out.println(e.getMessage());
            throw new RuntimeException(Constants.ERROR_HIBERNATE_UPDATE + fieldName, e);

        }
    }

    @Override
    public T removeById(K id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T entity = session.createQuery("from " + entityCLass.getName() + " where id = :id", entityCLass)
                        .setParameter("id", id)
                        .uniqueResult();

                if (entity != null) {
                    session.remove(entity);
                    tx.commit();
                }
                return entity;
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public Collection<T> findAll(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session
                    .createQuery("FROM "+ entityCLass.getName(), entityCLass)
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(Constants.ERROR_HIBERNATE_LIST + entityCLass.getSimpleName(), e);
        }
    }

    @Override
    public Map<K, T> getMap() {
        List<T> entities = (List<T>) this.findAll();

        Map<K, T> map = new HashMap<>();
        for (T entity : entities) {
            K id;
            try {
                id = (K) entityCLass.getMethod("getId").invoke(entity);
                map.put(id, entity);
            }catch (Exception e){
                throw new RuntimeException(Constants.ERROR_GET_ID, e);
            }
        }
        return map;
    }

    @Override
    public T update(T object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            T updatedEntity = (T) session.merge(object);
            tx.commit();
            return updatedEntity;
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(Constants.ERROR_HIBERNATE_UPDATE_ENTITY, e);
        } finally {
            session.close();
        }
    }
}
