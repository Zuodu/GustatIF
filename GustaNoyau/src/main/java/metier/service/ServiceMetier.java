/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.ClientDAO;
import dao.CommandeDAO;
import dao.JpaUtil;
import dao.LivreurDAO;
import dao.ProduitDAO;
import dao.ProduitsCommandesDAO;
import dao.RestaurantDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.RollbackException;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Cycliste;
import metier.modele.Drone;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.modele.ProduitsCommandes;
import metier.modele.Restaurant;
import util.Saisie;

/**
 *
 * @author Jindun
 */
public class ServiceMetier {
    
    
    //// Client
    
    public boolean inscrireClient(Client client){
        JpaUtil.creerEntityManager();
        ClientDAO cDAO = new ClientDAO();
        JpaUtil.ouvrirTransaction();
        cDAO.creerClient(client);
        
        ServiceTechnique serviceTechnique = new ServiceTechnique();
        try{
            JpaUtil.validerTransaction();
            JpaUtil.fermerEntityManager();
            serviceTechnique.envoyerMailClient(client, true);
            return true;
        }catch(RollbackException e){
            JpaUtil.fermerEntityManager();
            serviceTechnique.envoyerMailClient(client, false);
            return false;
        }
    }
    
    public Client authentifierClient(String mail, long idClient) throws Exception {
        Client c = recupererClient(idClient);
        if(c!=null){
            if (mail.equals(c.getMail())){
                return c;
            }
        }
        return null;
    }
    
    public Client recupererClient(long id) throws Exception{
        JpaUtil.creerEntityManager();
        ClientDAO cDAO = new ClientDAO();
        Client c = cDAO.findById(id);
        JpaUtil.fermerEntityManager();
        return c;
    }
    
    public List<Client> recupererListeClients() throws Exception{
        JpaUtil.creerEntityManager();
        
        ClientDAO cDAO = new ClientDAO();
        List<Client> liste = cDAO.findAll();
        
        JpaUtil.fermerEntityManager();
        
        return liste;
    }
    
    
    //// Restaurant
    
    public Restaurant recupererRestaurant(long id) throws Exception{
        JpaUtil.creerEntityManager();
        RestaurantDAO rDAO = new RestaurantDAO();
        Restaurant r = rDAO.findById(id);
        JpaUtil.fermerEntityManager();
        return r;
    }
    
    public Restaurant recupererRestaurantParIdProduit(long idProduit) throws Exception{
        JpaUtil.creerEntityManager();
        RestaurantDAO rDAO = new RestaurantDAO();
        Restaurant r = rDAO.findByProductId(idProduit);
        JpaUtil.fermerEntityManager();
        return r;
    }
    
    public List<Restaurant> recupererListeRestaurants() throws Exception{
        JpaUtil.creerEntityManager();
        RestaurantDAO rDAO = new RestaurantDAO();
        List<Restaurant> liste = rDAO.findAll();
        JpaUtil.fermerEntityManager();
        return liste;
    }
    
    public Produit recupererProduit(long id) throws Exception{
        JpaUtil.creerEntityManager();
        ProduitDAO pDAO = new ProduitDAO();
        Produit p = pDAO.findById(id);
        JpaUtil.fermerEntityManager();
        return p;
    }
    
    public List<Produit> recupererListeProduits(long idRestaurant) throws Exception{
        return (recupererRestaurant(idRestaurant)).getProduits();
    }
    
    
    //// Livreur
    
    public void initialiserLivreurs(){
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        Cycliste c1 = new Cycliste("mathilde.bronnert@insa-lyon.fr", "20 Avenue Albert Einstein, Villeurbanne", 8000.0, "disponible", "BRONNERT", "Mathilde");
        Cycliste c2 = new Cycliste("ghita.el_ghissassi@insa-lyon.fr", "65 Avenue Roger Salengro, Villeurbanne", 8000.0, "disponible", "EL GHISSASSI", "Ghita");
        Cycliste c3 = new Cycliste("jindun.shao@insa-lyon.fr", "107 Cours Emile Zola, Villeurbanne", 8000.0, "disponible", "SHAO", "Jindun");
        Cycliste c4 = new Cycliste("victor.borg@insa-lyon.fr", "1 Avenue d'Orcha, Vaulx-en-Velin", 8000.0, "disponible", "BORG", "Victor");
        Drone d1 = new Drone("gestionnaire1@gmail.com", "183 Rue Anatole France, Villeurbanne", 5000.0, "disponible", 10.0);
        Drone d2 = new Drone("gestionnaire1@gmail.com", "290 Route de Vienne, Lyon", 5000.0, "disponible", 10.0);
        Drone d3 = new Drone("gestionnaire2@gmail.com", "99 Route de Lyon, Saint-Priest", 5000.0, "disponible", 10.0);
        Drone d4 = new Drone("gestionnaire3@gmail.com", "19 Rue Marcel Dutartre, Villeurbanne", 5000.0, "disponible", 10.0);
        
        LivreurDAO lDAO = new LivreurDAO();
        lDAO.creerLivreur(c1);
        lDAO.creerLivreur(c2);
        lDAO.creerLivreur(c3);
        lDAO.creerLivreur(c4);
        lDAO.creerLivreur(d1);
        lDAO.creerLivreur(d2);
        lDAO.creerLivreur(d3);
        lDAO.creerLivreur(d4);
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
        
    }
    
    public List<Livreur> authentifierLivreur(String mail, long id) throws Exception{
        if(id==-1 && mail.equals("gustatif@gustatif.com"))
            return null;
        JpaUtil.creerEntityManager();
        LivreurDAO lDAO = new LivreurDAO();
        List<Livreur> liste = lDAO.findByMail(mail);
        for(Livreur l : liste){
            if(l.getId() == id){
                return liste;
            }
        }
        return new ArrayList<>();
    }
    
    public Livreur recupererLivreur(long id) throws Exception{
        JpaUtil.creerEntityManager();
        LivreurDAO lDAO = new LivreurDAO();
        Livreur l = lDAO.findById(id);
        JpaUtil.fermerEntityManager();
        return l;
    }
    
    public List<Cycliste> recupererListeCyclistes() throws Exception{
        JpaUtil.creerEntityManager();
        
        LivreurDAO lDAO = new LivreurDAO();
        List<Cycliste> liste = lDAO.findAllCycliste();
        JpaUtil.fermerEntityManager();
        return liste;
    }
    
    public List<Drone> recupererListeDrones() throws Exception{
        JpaUtil.creerEntityManager();
        
        LivreurDAO lDAO = new LivreurDAO();
        List<Drone> liste = lDAO.findAllDrone();
        JpaUtil.fermerEntityManager();
        return liste;
    }
    
    
    public List<Livreur> recupererListeLivreurs() throws Exception{
        List<Livreur> liste = new ArrayList<>();
        liste.addAll(recupererListeCyclistes());
        liste.addAll(recupererListeDrones());
        return liste;
    }
    
    
    //// Commande
    
    public Livreur attribuerLivreur(Restaurant resto, List<ProduitsCommandes> listeP, Client client) throws Exception{
        double dureeMin = Double.MAX_VALUE;
        Livreur livreur = null;
        ServiceTechnique sT = new ServiceTechnique();
        Double poidsTot = 0.0;
        for(ProduitsCommandes pC : listeP){
            poidsTot += pC.getProduit().getPoids();
        }
        
        
        for(Drone d : recupererListeDrones()){
            if((d.getStatut()).equals("disponible")){
                double duree = sT.calculerDureeVolDOiseau(d, resto, client);
                if(dureeMin > duree && poidsTot <= d.getChargeMax()){
                    dureeMin = duree;
                    livreur = d;
                }
            }
        }
        
        for(Cycliste cycl : recupererListeCyclistes()){
            if((cycl.getStatut()).equals("disponible")){
                double duree = sT.calculerDureeVelo(cycl, resto, client);
                if(dureeMin > duree && poidsTot <= cycl.getChargeMax()){
                    dureeMin = duree;
                    livreur = cycl;
                }
            }
        }
        if(livreur == null){
            System.out.println("/!\\ Erreur lors de l'attribution d'un livreur");
            return null;
        }else{
            JpaUtil.creerEntityManager();
            JpaUtil.ouvrirTransaction();
            CommandeDAO commandeDAO = new CommandeDAO();
            ProduitsCommandesDAO pCDAO = new ProduitsCommandesDAO();
            LivreurDAO lDAO = new LivreurDAO();
            
            try{
                livreur.setStatut("livraison");
                lDAO.majLivreur(livreur);
                
                for(ProduitsCommandes pC : listeP){
                    pCDAO.creerProduitsCommandes(pC);
                }
                
                Commande commande = new Commande(new Date(), listeP, client, livreur);
                commandeDAO.creerCommande(commande);
                Saisie.lireChaine("pause");
                JpaUtil.validerTransaction();
                JpaUtil.fermerEntityManager();
                sT.envoyerMailLivreur(commande);
                return livreur;
            }catch(RollbackException e){
                System.out.println("/!\\ Erreur lors de la création de la commande");
                JpaUtil.fermerEntityManager();
                return attribuerLivreur(resto, listeP, client);
            }
        }
    }
    
    public void cloturerLivraison(long idCommande) throws Exception{
        JpaUtil.creerEntityManager();
        CommandeDAO cDAO = new CommandeDAO();
        Commande commande = cDAO.findById(idCommande);
        JpaUtil.ouvrirTransaction();
        commande.setDateFinLivraison(new Date());
        cDAO.majCommande(commande);
        commande.getLivreur().setStatut("disponible");
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }
        
    public Commande recupererCommande(long idLivreur) throws Exception{
        JpaUtil.creerEntityManager();
        CommandeDAO cDAO = new CommandeDAO();
        List<Commande> listeC = cDAO.findAll();
        if(listeC!=null){
           for(Commande c : listeC){
               if(c.getLivreur().getId()==idLivreur && c.getDateFinLivraison()==null)
                   return c;
           } 
        }
        return null;
    }
    
    public List<Commande> recupererCommande(String mail) throws Exception{
        JpaUtil.creerEntityManager();
        CommandeDAO cDAO = new CommandeDAO();
        List<Commande> listeC = cDAO.findAll();
        List<Commande> listeTmp = new ArrayList();
        
        if(listeC!=null){
           for(Commande c : listeC){
               if(mail.equals(c.getLivreur().getMail()))
                   listeTmp.add(c);
           } 
        }
        return listeTmp;
    }
    
    public List<Commande> recupererLivraisonEnCours() throws Exception{
        JpaUtil.creerEntityManager();
        CommandeDAO cDAO = new CommandeDAO();
        List<Commande> listeC = cDAO.findAll();
        List<Commande> listeTmp = new ArrayList();
        
        if(listeC!=null){
           for(Commande c : listeC){
               if(c.getDateFinLivraison()==null)
                   listeTmp.add(c);
           } 
        }
        return listeTmp;
    }
    
}
