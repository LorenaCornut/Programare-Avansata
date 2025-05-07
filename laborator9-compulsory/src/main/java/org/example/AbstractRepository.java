package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public abstract class AbstractRepository<T> {
    protected EntityManager em;
    private Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        em = PersistenceManager.getEntityManagerFactory().createEntityManager();
    }

    public void create(T entity) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(entity);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        }
    }

    public T findById(Object id) {
        return em.find(entityClass, id);
    }

    public void close() {
        em.close();
    }
}