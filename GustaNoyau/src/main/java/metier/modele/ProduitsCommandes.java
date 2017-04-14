
package metier.modele;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author gelghissas
 */

@Entity
public class ProduitsCommandes implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private long id;
    private int quantite;
    private double prix;
    
    @ManyToOne
    private Produit produit;
    
    
    public ProduitsCommandes() {
        this.produit = null;
    }

    public ProduitsCommandes(Produit p, int quantite) {
        if(quantite<0)
            this.quantite = 0;
        else
            this.quantite = quantite;
        this.produit = p;
        this.prix = quantite * p.getPrix();
    }

    public long getId() {
        return id;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrix() {
        return prix;
    }

    public Produit getProduit() {
        return produit;
    }

   

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrix(long prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return quantite + " " + produit.getDenomination() + " : "
                + quantite + " x " + produit.getPrix() + '€';
    }
    

}
