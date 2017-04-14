package metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String denomination;
    private String description;
    private String adresse;
    private Double Longitude;
    private Double latitude;

    @OneToMany
    private List<Produit> produits;
    
    protected Restaurant() {
        this.produits = new ArrayList<>();
    }

    public Restaurant(String denomination, String description, String adresse) {
        this.denomination = denomination;
        this.description = description;
        this.adresse = adresse;
        this.Longitude = null;
        this.latitude = null;
        this.produits = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getDenomination() {
        return denomination;
    }

    public String getDescription() {
        return description;
    }

    public String getAdresse() {
        return adresse;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setLatitudeLongitude(Double latitude, Double Longitude) {
        this.latitude = latitude;
        this.Longitude = Longitude;
    }

    public void addProduit(Produit produit) {
        this.produits.add(produit);
    }

    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id + ", denomination=" + denomination + ", description=" + description + ", adresse=" + adresse + ", Longitude=" + Longitude + ", latitude=" + latitude + ", produits=" + produits + '}';
    }

}
