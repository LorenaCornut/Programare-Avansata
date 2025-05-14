package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.PersistenceManager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractRepository<T, ID extends Serializable> {
    protected EntityManager em;
    private final Class<T> entityClass;
    protected final Logger logger;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.logger = Logger.getLogger(getClass().getName());
        this.em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        logger.log(Level.CONFIG, "Initializat repository pentru entitatea {0}", entityClass.getSimpleName());
    }

    public void create(T entity) {
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            logger.log(Level.FINE, "Început creare entitate {0}", entity);

            et.begin();
            em.persist(entity);
            et.commit();

            logger.log(Level.INFO, "Entitate creată cu succes: {0}", entity);
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
                logger.log(Level.WARNING, "Rollback la crearea entității", e);
            }
            logger.log(Level.SEVERE, "Eroare la crearea entității", e);
            throw e;
        }
    }

    public T findById(ID id) {
        logger.log(Level.FINE, "Căutare entitate {0} cu ID {1}",
                new Object[]{entityClass.getSimpleName(), id});

        T entity = em.find(entityClass, id);

        if (entity != null) {
            logger.log(Level.INFO, "Entitate găsită: {0}", entity);
        } else {
            logger.log(Level.WARNING, "Entitatea {0} cu ID {1} nu a fost găsită",
                    new Object[]{entityClass.getSimpleName(), id});
        }

        return entity;
    }

    public List<T> findAll() {
        logger.log(Level.FINE, "Căutare toate entitățile de tip {0}", entityClass.getSimpleName());

        try {
            TypedQuery<T> query = em.createQuery(
                    "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            List<T> result = query.getResultList();

            logger.log(Level.INFO, "Găsite {0} entități de tip {1}",
                    new Object[]{result.size(), entityClass.getSimpleName()});

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Eroare la obținerea tuturor entităților", e);
            throw e;
        }
    }

    public void update(T entity) {
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            logger.log(Level.FINE, "Început actualizare entitate {0}", entity);

            et.begin();
            em.merge(entity);
            et.commit();

            logger.log(Level.INFO, "Entitate actualizată cu succes: {0}", entity);
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
                logger.log(Level.WARNING, "Rollback la actualizarea entității", e);
            }
            logger.log(Level.SEVERE, "Eroare la actualizarea entității", e);
            throw e;
        }
    }

    public void delete(ID id) {
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            logger.log(Level.FINE, "Început ștergere entitate {0} cu ID {1}",
                    new Object[]{entityClass.getSimpleName(), id});

            T entity = em.find(entityClass, id);
            if (entity != null) {
                et.begin();
                em.remove(entity);
                et.commit();
                logger.log(Level.INFO, "Entitate ștearsă cu succes: ID {0}", id);
            } else {
                logger.log(Level.WARNING, "Entitatea cu ID {0} nu există - ștergere anulată", id);
            }
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
                logger.log(Level.WARNING, "Rollback la ștergerea entității", e);
            }
            logger.log(Level.SEVERE, "Eroare la ștergerea entității", e);
            throw e;
        }
    }

    public void close() {
        try {
            if (em.isOpen()) {
                em.close();
                logger.log(Level.INFO, "EntityManager închis cu succes");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Eroare la închiderea EntityManager", e);
        }
    }

    protected List<T> executeNamedQuery(String queryName, Object... params) {
        logger.log(Level.FINE, "Executare named query: {0}", queryName);

        try {
            TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);

            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
                logger.log(Level.FINER, "Setat parametru {0} = {1}",
                        new Object[]{i + 1, params[i]});
            }

            List<T> result = query.getResultList();
            logger.log(Level.INFO, "Query {0} returnat {1} rezultate",
                    new Object[]{queryName, result.size()});

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Eroare la executarea query-ului " + queryName, e);
            throw e;
        }
    }
}