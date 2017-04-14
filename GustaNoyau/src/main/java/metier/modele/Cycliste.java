/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import javax.persistence.Entity;

/**
 *
 * @author gelghissas
 */
@Entity
public class Cycliste extends Livreur {
    
    private String nom;
    private String prenom;

    public Cycliste() {
    }

    public Cycliste(String mail, String adresse, Double chargeMax, String statut, String nom, String prenom) {
        super( mail, adresse, chargeMax, statut);
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return super.toString() + " | Cycliste{" + "nom=" + nom + ", prenom=" + prenom + '}';
    }
    
    

    
}
