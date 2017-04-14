package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Restaurant;

public class RestaurantDAO {
    
    public Restaurant findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Restaurant restaurant = null;
        try {
            restaurant = em.find(Restaurant.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return restaurant;
    }
    
    public Restaurant findByProductId(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Restaurant restaurant = null;
        try {
            Query q = em.createNativeQuery("SELECT RESTAURANT_ID FROM RESTAURANT_PRODUIT WHERE PRODUITS_ID = ?1");
            q.setParameter(1, id);
            restaurant = em.find(Restaurant.class, (long)q.getSingleResult());
        }
        catch(Exception e) {
            throw e;
        }
        return restaurant;
    }
    
    
    public List<Restaurant> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Restaurant> restaurants = null;
        try {
            Query q = em.createQuery("SELECT r FROM Restaurant r");
            restaurants = (List<Restaurant>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        return restaurants;
    }
    
  
}
