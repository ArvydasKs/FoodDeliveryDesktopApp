package com.example.courseprifs.HibernateControl;

import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String login, String password) {
        User user = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // turesime metodus and, or, like
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.equal(root.get("login"), login));
            Query q = entityManager.createQuery(query);

            user = (User) q.getSingleResult();

            if (!BCrypt.checkpw(password, user.getPassword())) {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return user;
    }

    public List<FoodOrder> getRestaurantOrders(Restaurant restaurant) {
        List<FoodOrder> orders = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> query = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = query.from(FoodOrder.class);

            query.select(root).where(cb.equal(root.get("restaurant"), restaurant));
            Query q = entityManager.createQuery(query);
            orders = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return orders;
    }

    public List<FoodOrder> getFilteredRestaurantOrders(Restaurant restaurant, BasicUser client, OrderStatus orderStatus) {
        List<FoodOrder> orders = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> query = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = query.from(FoodOrder.class);

            List<Predicate> predicates = new ArrayList<>();

            if (restaurant != null) {
                predicates.add(cb.equal(root.get("restaurant"), restaurant));
            }

            if (client != null) {
                predicates.add(cb.equal(root.get("buyer"), client));
            }

            if (orderStatus != null) {
                predicates.add(cb.equal(root.get("orderStatus"), orderStatus));
            }

            if (!predicates.isEmpty()) {
                query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
            } else {
                query.select(root);
            }

            Query q = entityManager.createQuery(query);
            orders = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return orders;
    }

    public List<Cuisine> getRestaurantCuisine(Restaurant restaurant) {
        List<Cuisine> menu = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cuisine> query = cb.createQuery(Cuisine.class);
            Root<Cuisine> root = query.from(Cuisine.class);

            query.select(root).where(cb.equal(root.get("restaurant"), restaurant));
            Query q = entityManager.createQuery(query);
            menu = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return menu;
    }

    public List<Cuisine> getFilteredCuisines(Restaurant restaurant, Boolean spicy, Boolean vegan) {
        List<Cuisine> cuisines = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cuisine> query = cb.createQuery(Cuisine.class);
            Root<Cuisine> root = query.from(Cuisine.class);

            List<Predicate> predicates = new ArrayList<>();

            if (restaurant != null) {
                predicates.add(cb.equal(root.get("restaurant"), restaurant));
            }

            if (spicy != null) {
                predicates.add(cb.equal(root.get("isSpicy"), spicy));
            }

            if (vegan != null) {
                predicates.add(cb.equal(root.get("isVegan"), vegan));
            }

            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
            Query q = entityManager.createQuery(query);
            cuisines = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return cuisines;
    }

    public List<Review> getChatMessages(Chat chat) {
        List<Review> messages = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Review> query = cb.createQuery(Review.class);
            Root<Review> root = query.from(Review.class);

            query.select(root).where(cb.equal(root.get("chat"), chat));
            Query q = entityManager.createQuery(query);
            messages = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return messages;
    }

    public List<User> getUsersByType(Class<? extends User> clazz) {
        List<User> users = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.equal(root.type(), clazz));
            Query q = entityManager.createQuery(query);
            users = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return users;
    }

    public List<Cuisine> getOrderCuisines(FoodOrder foodOrder) {
        List<Cuisine> cuisines = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cuisine> query = cb.createQuery(Cuisine.class);
            Root<FoodOrder> root = query.from(FoodOrder.class);

            Join<FoodOrder, Cuisine> join = root.join("cuisines");

            query.select(join).where(cb.equal(root.get("id"), foodOrder.getId()));

            Query q = entityManager.createQuery(query);
            cuisines = q.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return cuisines;
    }

    public List<Review> getUserReviews(BasicUser basicUser) {
        List<Review> reviews = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Review> query = cb.createQuery(Review.class);
            Root<Review> root = query.from(Review.class);

            query.select(root).where(cb.equal(root.get("feedBackOwner"), basicUser));
            Query q = entityManager.createQuery(query);
            reviews = q.getResultList();
        }
        catch (Exception e) {
            // alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return reviews;
    }
}
