package metier.modele;

import com.google.maps.model.LatLng;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import metier.service.ServiceTechnique;

@Entity
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable=false)
    private String nom;
    
    @Column(nullable=false)
    private String prenom;
    
    @Column(unique=true, nullable=false)
    private String mail;
    
    @Column(nullable=false)
    private String adresse;
    
    private Double longitude;
    private Double latitude;

    protected Client() {
    }
    
    public Client(String nom, String prenom, String mail, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        if(mail.equals("gustatif@gustatif.com"))
            this.mail = null;
        this.adresse = adresse;
        ServiceTechnique sT = new ServiceTechnique();
        LatLng latLng = sT.calculerLatLng(adresse);
        this.longitude = latLng.lng;
        this.latitude = latLng.lat;
    }
    
    

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setLatitudeLongitude(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", adresse=" + adresse + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }

}
