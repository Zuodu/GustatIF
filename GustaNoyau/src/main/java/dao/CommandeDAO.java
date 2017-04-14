/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Commande;

/**
 *
 * @author gelghissas
 */
public class CommandeDAO {
    
     public Commande findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Commande commande = null;
        try {
            commande = em.find(Commande.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return commande;
    }
    
    public List<Commande> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Commande> commandes = null;
        try {
            Query q = em.createQuery("SELECT c FROM Commande c");
            commandes = (List<Commande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return commandes;
    }
    
    public void creerCommande(Commande c) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(c);
    }
    
    public void majCommande(Commande c){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.merge(c);
    }
    
}
