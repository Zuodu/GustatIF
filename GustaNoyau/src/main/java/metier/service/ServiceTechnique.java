/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import com.google.maps.model.LatLng;
import metier.modele.*;
import util.GeoTest;


/**
 *
 * @author gelghissas
 */
public class ServiceTechnique {
    
    public void envoyerMailClient(Client client, boolean succes){
        System.out.println("Expéditeur : gustatif@gustatif.com");
        System.out.println("Pour : " + client.getPrenom() + " " + client.getNom() + '<' +client.getMail() + '>');
        System.out.println("Sujet : Bienvenue chez Gustat'IF");
        System.out.println("Corps :\nBonjour " + client.getPrenom() + ',');
        if(succes){
            System.out.println("Nous vous confirmons votre inscription au service GUSTAT'IF. Votre numéro de client est : " + client.getId() + ".");
        }else{
            System.out.println("Votre inscription au service GUSTAT'IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
        }
    }
    
    public void envoyerMailLivreur(Commande c){
        Livreur l = c.getLivreur();
        boolean estCycliste = (c.getLivreur() instanceof Cycliste);
        System.out.println("Expéditeur : gustatif@gustatif.com");
        System.out.println("Pour : "  + l.getMail() );
        System.out.println("Sujet : Livraison n°" + c.getId() + " à effectuer");
        System.out.println("Corps :\nBonjour " + (estCycliste? ((Cycliste)l).getPrenom(): "gestionnaire du drône n°"+l.getId()) ) ;
        System.out.print("\tMerci d'effectuer cette livraison dès maintenant, tout en respectant le code ");
        System.out.println((estCycliste)?"de la route ;-)":"du ciel ;-)");
        System.out.println("Le Chef");
        System.out.println();
        System.out.println(c);
    }
    
    public LatLng calculerLatLng(String adresse){
        return GeoTest.getLatLng(adresse);     
    }
    
    public double calculerDureeVolDOiseau(Drone d, Restaurant r, Client c){
        LatLng latLngDrone = new LatLng(d.getLatitude(), d.getLongitude());
        LatLng latLngResto = new LatLng(r.getLatitude(), r.getLongitude());
        LatLng latLngClient = new LatLng(c.getLatitude(), c.getLongitude());
        double distance = GeoTest.getFlightDistanceInKm(latLngDrone, latLngResto)
                                + GeoTest.getFlightDistanceInKm(latLngResto, latLngClient);
        return 60.0 * distance / d.getVitesseMoy();
    }
    
    public double calculerDureeVelo(Cycliste cycl, Restaurant r, Client client){
        LatLng latLngCycl = new LatLng(cycl.getLatitude(), cycl.getLongitude());
        LatLng latLngResto = new LatLng(r.getLatitude(), r.getLongitude());
        LatLng latLngClient = new LatLng(client.getLatitude(), client.getLongitude());
        return GeoTest.getTripDurationByBicycleInMinute(latLngCycl, latLngClient, latLngResto);
    }
    
    
}
