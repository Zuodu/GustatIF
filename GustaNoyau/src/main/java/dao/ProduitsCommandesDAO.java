/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.ProduitsCommandes;

/**
 *
 * @author gelghissas
 */
public class ProduitsCommandesDAO{
    
    public ProduitsCommandes findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        ProduitsCommandes produitsCommandes = null;
        try {
            produitsCommandes = em.find(ProduitsCommandes.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return produitsCommandes;
    }
    
    public List<ProduitsCommandes> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<ProduitsCommandes> liste = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProduitsCommandes p");
            liste = (List<ProduitsCommandes>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return liste;
    }
    
    public void creerProduitsCommandes(ProduitsCommandes prodCom) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(prodCom);
    }
    
}
