/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import com.google.maps.model.LatLng;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;
import metier.service.ServiceTechnique;

/**
 *
 * @author gelghissas
 */
@Entity
@Inheritance (strategy = InheritanceType.JOINED)
public abstract class Livreur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @Version
    private Long version;
    
    @Column(nullable=false)
    protected String mail;
    
    @Column(nullable=false)
    protected String adresse;
    
    @Column(nullable=false)
    protected Double chargeMax;
    
    @Column(nullable=false)
    protected String statut;
    
    protected Double Longitude;
    protected Double latitude;

    public Livreur() {
    }
    
    public Livreur(String mail, String adresse, Double chargeMax, String statut) {
        this.mail = mail;
        this.adresse = adresse;
        this.chargeMax = chargeMax;
        this.statut = statut;
        ServiceTechnique sT = new ServiceTechnique();
        LatLng latLng = sT.calculerLatLng(adresse);
        this.Longitude = latLng.lng;
        this.latitude = latLng.lat;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }
    
    public String getMail() {
        return mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public Double getChargeMax() {
        return chargeMax;
    }

    public String getStatut() {
        return statut;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
   
    public void setChargeMax(Double chargeMax) {
        this.chargeMax = chargeMax;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Livreur{" + "id=" + id + ", mail=" + mail + ", chargeMax=" + chargeMax + ", statut=" + statut + ", Longitude=" + Longitude + ", latitude=" + latitude + '}';
    }

   
    
    
}
