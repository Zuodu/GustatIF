package metier.modele;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Client;
import metier.modele.Livreur;
import metier.modele.ProduitsCommandes;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-15T18:27:34")
@StaticMetamodel(Commande.class)
public class Commande_ { 

    public static volatile SingularAttribute<Commande, Date> dateCommmande;
    public static volatile SingularAttribute<Commande, Client> client;
    public static volatile SingularAttribute<Commande, Long> id;
    public static volatile SingularAttribute<Commande, Livreur> livreur;
    public static volatile ListAttribute<Commande, ProduitsCommandes> listeProduits;
    public static volatile SingularAttribute<Commande, Long> version;
    public static volatile SingularAttribute<Commande, Double> prixTotal;
    public static volatile SingularAttribute<Commande, Date> dateFinLivraison;

}