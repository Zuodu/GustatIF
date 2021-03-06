package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Client;

public class ClientDAO {
    
    public Client findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Client client = null;
        try {
            client = em.find(Client.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return client;
    }
    
    public List<Client> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Client> clients = null;
        try {
            Query q = em.createQuery("SELECT c FROM Client c");
            clients = (List<Client>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return clients;
    }
    
    public void creerClient(Client c) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(c);
    }
    
}
