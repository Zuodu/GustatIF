/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.persistence.Version;
import metier.service.ServiceMetier;

/**
 *
 * @author gelghissas
 */
@Entity
public class Commande implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    private Long version;
    
    @Temporal(DATE)
    private Date dateCommmande;
    @Temporal(DATE)
    private Date dateFinLivraison;
    private Double prixTotal;
    
    @OneToMany
    private List<ProduitsCommandes> listeProduits;
    
    @ManyToOne
    private Client client;
    
    @ManyToOne
    private Livreur livreur;

    public Commande() {
        this.listeProduits= new ArrayList<>();
    }

    public Commande(Date dateCommmande, List<ProduitsCommandes> listeP, Client c, Livreur l) {
        this.dateCommmande = dateCommmande;
        this.dateFinLivraison = null;
        this.listeProduits = listeP;
        
        this.prixTotal = 0.0;
        for(ProduitsCommandes pC : listeP){
            this.prixTotal += pC.getPrix();
        }
        
        this.client = c;
        this.livreur = l;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Date getDateCommmande() {
        return dateCommmande;
    }

    public Date getDateFinLivraison() {
        return dateFinLivraison;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public List<ProduitsCommandes> getListeProduits() {
        return listeProduits;
    }

    public Client getClient() {
        return client;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void setDateCommmande(Date dateCommmande) {
        this.dateCommmande = dateCommmande;
    }

    public void setDateFinLivraison(Date dateFinLivraison) {
        this.dateFinLivraison = dateFinLivraison;
    }

    public void addProduitsCommandes(ProduitsCommandes pC) {
        this.listeProduits.add(pC);
        this.prixTotal += pC.getPrix()*pC.getQuantite();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH'h'mm");
        ServiceMetier sm = new ServiceMetier();
        String s = "Détail de la Livraison\n"
                    + "\t Date/heure : " + dateFormat.format(dateCommmande) + '\n'
                    + "\t Livreur : ";
        if(livreur instanceof Cycliste){
            s += ((Cycliste)livreur).getPrenom();
            s += ( " " + ((Cycliste)livreur).getNom() );
            s += (" (n°" + livreur.getId() + ")\n" ); 
        }else{
            s += ( "Drône n°" + livreur.getId() + '\n');
        }
        s += "\t Restaurant : ";
        try{
            s = s + ((sm.recupererRestaurantParIdProduit(listeProduits.get(0).getProduit().getId())).getDenomination());
        }catch(Exception e){
            s += '?';
        }finally{
            s+='\n';
        }
        s = s + "\t Client :\n"
                + "\t\t " + client.getPrenom() + " " +client.getNom() + '\n'
                + "\t\t " + client.getAdresse() +"\n\n";
        s = s + "Commande :\n";
        for(ProduitsCommandes pC : listeProduits){
            s += '\t' + pC.toString() + '\n';
        }
        s += "\nTOTAL : " + prixTotal + "€\n";
        return s;
    }
}
