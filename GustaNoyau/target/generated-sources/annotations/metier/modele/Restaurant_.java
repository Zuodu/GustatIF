package metier.modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Produit;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-18T22:15:23")
@StaticMetamodel(Restaurant.class)
public class Restaurant_ { 

    public static volatile ListAttribute<Restaurant, Produit> produits;
    public static volatile SingularAttribute<Restaurant, Double> latitude;
    public static volatile SingularAttribute<Restaurant, String> adresse;
    public static volatile SingularAttribute<Restaurant, String> description;
    public static volatile SingularAttribute<Restaurant, Long> id;
    public static volatile SingularAttribute<Restaurant, Double> Longitude;
    public static volatile SingularAttribute<Restaurant, String> denomination;

}