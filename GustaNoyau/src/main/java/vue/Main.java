/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import dao.*;
import java.util.ArrayList;
import metier.modele.*;
import metier.service.*;
import util.Saisie;

import java.util.List;

/**
 *
 * @author gelghissas
 */
public class Main {

    public static void main(String[] args) throws Exception {
        JpaUtil.init();
        ServiceMetier sm = new ServiceMetier();
        boolean stay = true, stayClient = false, stayLivreur=false;
        Client client = null; boolean clientConnecte = false;
        
        int codeOp;
        while(stay){
            System.out.println("####################");
            System.out.println("MENU PRINCIPAL");
            System.out.println("1: Vous êtes client ?");
            System.out.println("2: Vous êtes livreur ?");
            System.out.println("3: Initialiser les livreurs");
            System.out.println("0: Quitter");
            System.out.println("####################");
            switch(Saisie.lireInteger("Veuillez saisir un numéro : ")){
               
                case 1:
                    stayClient = true;
                    while(stayClient){
                        if(!clientConnecte){
                            System.out.println("####################");
                            System.out.println("PAGE CLIENT");
                            System.out.println("1: Se connecter");
                            System.out.println("2: S'inscrire");
                            System.out.println("0: Menu principal");
                            System.out.println("####################");
                            switch(Saisie.lireInteger("Veuillez saisir un numéro : ")){
                                case 1:
                                    String mail; long id;
                                    mail = Saisie.lireChaine("Adresse mail : ");
                                    id = Saisie.lireInteger("Mot de passe (Votre ID) : ");
                                    client = sm.authentifierClient(mail, id);
                                    clientConnecte = (client!=null);
                                    break;
                                case 2:
                                    String nomC, prenomC, mailC, adresseC;
                                    nomC = Saisie.lireChaine("Nom : ");
                                    prenomC = Saisie.lireChaine("Prenom : ");
                                    mailC = Saisie.lireChaine("Mail : ");
                                    adresseC = Saisie.lireChaine("Adresse postale : ");
                                    client = new Client(nomC, prenomC, mailC, adresseC);
                                    if(! sm.inscrireClient(client)){
                                        client = null;
                                    }
                                    break;
                                default:
                                    stayClient=false;
                                    break;
                            }
                        }else{
                            System.out.println("####################");
                            System.out.println("PAGE CLIENT CONNECTE - Choix du restaurant");
                            List<Restaurant> listeR = sm.recupererListeRestaurants();
                            for(Restaurant r : listeR){
                                System.out.println(r);
                            }
                            System.out.println("0: Se déconnecter");
                            System.out.println("####################");
                            switch(codeOp = Saisie.lireInteger("id restaurant (0 pour se déconnecter) : ")){
                                case 0:
                                    clientConnecte = false;
                                    break;
                                default:
                                    Restaurant restaurant;
                                    if((restaurant = sm.recupererRestaurant(codeOp))==null){
                                        System.out.println("~ id Restaurant ("+codeOp+") incorrect !");
                                        break;
                                    }else{
                                        boolean stayProduit = true;
                                        System.out.println("####################");
                                        System.out.println("PAGE CLIENT CONNECTE - Choix des produits");
                                        List<Produit> listeP = sm.recupererListeProduits(restaurant.getId());
                                        if(listeP!=null){
                                            for(Produit p : listeP){
                                                System.out.println(p);
                                            }
                                        }
                                        System.out.println("0: Quitter");
                                        System.out.println("####################");
                                        List<ProduitsCommandes> listePC = new ArrayList<>();
                                        while(stayProduit){
                                            
                                            switch(codeOp = Saisie.lireInteger("id Produit (0 pour revenir sur la page des restaurants) : ")){
                                                case 0:
                                                    stayProduit = false;
                                                    break;
                                                    
                                                default:
                                                    Produit p = sm.recupererProduit(codeOp);
                                                    if(p==null){
                                                        System.out.println("~ id Produit ("+codeOp+") incorrect !");
                                                    }else{
                                                        int quantite = Saisie.lireInteger("quantite : ");
                                                        ProduitsCommandes pC = new ProduitsCommandes(p, quantite);
                                                        listePC.add(pC);
                                                    }
                                                    
                                                    switch(Saisie.lireInteger("Continuer (1) / Terminer la commande (0) : ")){
                                                        case 1:
                                                            break;
                                                        default:
                                                            stayProduit = false;
                                                            break;
                                                    }
                                                    break;
                                            }
                                        }
                                        if(!listePC.isEmpty()){
                                            System.out.println("####################");
                                            if(sm.attribuerLivreur(restaurant, listePC, client) != null){
                                                System.out.println("COMMANDE CONFIRMEE");
                                            }else{
                                                System.out.println("ECHEC COMMANDE, REESAYEZ");
                                            }
                                            System.out.println("####################");
                                            Saisie.lireChaine("...");
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    break;
                
                case 2:
                    stayLivreur = true;
                    String mailL; long idL;
                    mailL = Saisie.lireChaine("Mail : ");
                    idL = Saisie.lireInteger("id Livreur : ");
                    
                    List<Livreur> listeL = sm.authentifierLivreur(mailL, idL);
                    
                    if(listeL==null){   // Admin
                        boolean stayAdmin = true;
                        while(stayAdmin){
                            System.out.println("####################");
                            System.out.println("PAGE ADMIN");
                            System.out.println("1: Voir la liste des clients");
                            System.out.println("2: Voir la liste des livreurs");
                            System.out.println("3: Voir la liste des restaurants");
                            System.out.println("4: Voir la liste des livraisons en cours");
                            System.out.println("0: Quitter");
                            System.out.println("####################");
                            codeOp = Saisie.lireInteger("Veuillez saisir un numéro : ");
                            switch(codeOp){

                                case 1:
                                    List<Client> tmpC = sm.recupererListeClients();
                                    if(tmpC!=null){
                                        for(Client c : tmpC){
                                            System.out.println(c);
                                        }
                                    }
                                    System.out.println("~ Fin de l'affichage");
                                    break;
                                case 2:
                                    List<Livreur> tmpL = sm.recupererListeLivreurs();
                                    if(tmpL!=null){
                                        for(Livreur l : tmpL){
                                            System.out.println(l);
                                        }
                                    }
                                    System.out.println("~ Fin de l'affichage");
                                    break;
                                case 3:
                                    List<Restaurant> tmpR = sm.recupererListeRestaurants();
                                    if(tmpR!=null){
                                        for(Restaurant r : tmpR){
                                            System.out.println(r);
                                        }
                                    }
                                    System.out.println("~ Fin de l'affichage");
                                    break;
                                case 4:
                                    List<Commande> tmpCo = sm.recupererLivraisonEnCours();
                                    if(tmpCo!=null){
                                        for(Commande co : tmpCo){
                                            System.out.println(co);
                                        }
                                    }
                                    System.out.println("~ Fin de l'affichage");
                                    break;
                                default:
                                    
                                    stayAdmin = false;
                                    break;

                            }
                        }
                    }else{
                        if(listeL.isEmpty()){   // Erreur Auth
                            System.out.println("~ Erreur lors de l'authentification");
                        }else{ // Auth réussie
                            stayLivreur = true;
                            while(stayLivreur){
                                System.out.println("####################");
                                long idLivreur;
                                if(listeL.size()==1){ //Cycliste
                                    System.out.println("PAGE CYCLISTE");
                                    idLivreur = listeL.get(0).getId();
                                }else{  //Drone
                                    System.out.println("PAGE GESTIONNAIRE DE DRONES");
                                    System.out.println("Vous gerez "+ listeL.size()+" drônes :");
                                    for(Livreur l : listeL){
                                        System.out.println(l);
                                    }
                                    idLivreur = Saisie.lireInteger("Veuillez saisir l'id d'un drône"); // erreur possible non gérée
                                }

                                Commande comm = sm.recupererCommande(idLivreur);
                                if(comm==null){
                                    System.out.println("Vous n'avez pas de livraison en cours.");
                                    System.out.println("####################\n");
                                    stayLivreur = false;
                                    Saisie.lireChaine("...");
                                }else{
                                    System.out.println("Vous avez une livraison en cours :");
                                    System.out.println(comm);
                                    System.out.println("\n1 : Clôturer la commande");
                                    System.out.println("\n0 : Quitter");
                                    System.out.println("####################\n");
                                    switch(Saisie.lireInteger("Veuillez saisir un numéro : ")){
                                        case 1:
                                            sm.cloturerLivraison(comm.getId());
                                            comm = null;
                                            Saisie.lireChaine("Livraison clôturée.\n...");
                                            break;
                                        default:
                                            stayLivreur = false;
                                            break;
                                    }

                                    
                                    
                                    
                                }
                            
                            }
                            
                        }
                    }
                    
                    
                    
                    
                    
                    break;
                
                case 3:
                    if(sm.recupererListeLivreurs().isEmpty()){
                        sm.initialiserLivreurs();
                        System.out.println("~ Livreurs initialisés. \n");
                    }else{
                        System.out.println("~ Livreurs déjà initialisés ! \n");
                    }
                    break;
                
                default:
                    stay = false;
                    break;
                
                
            }
        }
       
        JpaUtil.destroy();
    }
}
