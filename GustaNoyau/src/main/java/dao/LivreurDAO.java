/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Drone;
import metier.modele.Cycliste;
import metier.modele.Livreur;

/**
 *
 * @author gelghissas
 */
public class LivreurDAO {
    
    public Livreur findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Livreur livreur = null;
        try {
            livreur = em.find(Livreur.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        
        return livreur;
    }
    
    public List<Livreur> findByMail(String mail) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Livreur> liste = new ArrayList<>();
        try {
            String sQuery = "SELECT c FROM Cycliste c WHERE c.mail = :m";
            Query q = em.createQuery(sQuery);
            q.setParameter("m", mail);
            
            liste.addAll((List<Cycliste>)q.getResultList());
            
            if(liste.isEmpty()){
                sQuery = "SELECT d FROM Drone d WHERE d.mail = :m";
                q = em.createQuery(sQuery);
                q.setParameter("m", mail);
                liste.addAll((List<Drone>)q.getResultList());
            }
        }
        catch(Exception e) {
            throw e;
        }
        
        return liste;
    }
    
    public List<Cycliste> findAllCycliste() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Cycliste> cyclistes = null;
        try {
            Query q = em.createQuery("SELECT c FROM Cycliste c");
            cyclistes = (List<Cycliste>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return cyclistes;
    }
    
    public List<Drone> findAllDrone() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Drone> drones = null;
        try {
            Query q = em.createQuery("SELECT c FROM Drone c");
            drones = (List<Drone>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return drones;
    }
    
    
    public void creerLivreur(Livreur l) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(l);
    }
    
    public void majLivreur(Livreur l){
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.merge(l);
    }
    
     
     
     
    
}
