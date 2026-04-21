/**
 * helper klase, skirta darbui su duomenu baze
 */
package com.example.courseprifs.HibernateControl;

import com.example.courseprifs.fxControllers.FxUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {
    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void create(T entity) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            FxUtils.generateErrorDialog(e, "error", "create", "create");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> void update(T entity) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            FxUtils.generateErrorDialog(e, "error", "update", "update");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> void delete(Class<T> entityClass, int id) {
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            T entityFromDb = entityManager.find(entityClass, id);
            entityManager.remove(entityFromDb);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            FxUtils.generateErrorDialog(e, "error", "delete", "delete");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> T getEntityById(Class<T> entity, int id) {
        T entityFromDb = null;
        try{
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityFromDb = entityManager.find(entity, id);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            FxUtils.generateErrorDialog(e, "error", "getEntityByID", "getEntityByID");
        }finally {
            if (entityManager != null) entityManager.close();
        }
        return entityFromDb;
    }

    public <T> List<T> getAllRecords(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try{
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaQuery query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = entityManager.createQuery(query);
            list = q.getResultList();
        } catch (Exception e) {
            FxUtils.generateErrorDialog(e, "error", "getAllRecords", "getAllRecords");
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return list;
    }

    public boolean userExists(String login) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.login = :login");
            query.setParameter("login", login);
            Long count = (Long) query.getSingleResult();
            return count > 0;
        } finally {
            entityManager.close();
        }
    }
}
