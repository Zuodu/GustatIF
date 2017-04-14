/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author gelghissas
 */

@Entity
public class Drone extends Livreur{
    
    @Column(nullable=false)
    private Double vitesseMoy;

    public Drone() {
    }

  
    public Drone(String mail, String adresse, Double chargeMax, String statut, Double vitesseMoy) {
        super(mail, adresse, chargeMax, statut);
        if(vitesseMoy<=0){
            this.vitesseMoy = 0.1;
        }else{
            this.vitesseMoy = vitesseMoy;
        }
    }

    public void setVitesseMoy(Double vitesseMoy) {
        this.vitesseMoy = vitesseMoy;
    }

    public Double getVitesseMoy() {
        return vitesseMoy;
    }

    @Override
    public String toString() {
        return super.toString() + " | Drone{" + "vitesseMoy=" + vitesseMoy + '}';
    }

    
}
