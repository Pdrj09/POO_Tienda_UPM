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
        if (this.hasMaxSize && findAll().size() >= this.maxSize) {
            throw new IllegalStateException(Constants.ERROR_MAXSIZE + this.maxSize);
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        //ACID ATOMIC OPERATION Atomic,Consistent, Isolated, Durable
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
    public T findById(K id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = session.createQuery("from " + entityCLass.getName() + " where id = :id", entityCLass).setParameter("id", id).uniqueResult();
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
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            //we invoke the setter. MAGIC
            java.lang.reflect.Method setter = entityCLass.getMethod(setterName, newValue.getClass());
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        T entity = findById(id);
        if (entity != null) session.remove(entity);

        tx.commit();
        session.close();
        return entity;
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
}
