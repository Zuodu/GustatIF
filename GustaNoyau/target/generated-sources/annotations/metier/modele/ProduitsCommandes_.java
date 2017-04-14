package metier.modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Produit;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-14T17:29:10")
@StaticMetamodel(ProduitsCommandes.class)
public class ProduitsCommandes_ { 

    public static volatile SingularAttribute<ProduitsCommandes, Long> id;
    public static volatile SingularAttribute<ProduitsCommandes, Produit> produit;
    public static volatile SingularAttribute<ProduitsCommandes, Double> prix;
    public static volatile SingularAttribute<ProduitsCommandes, Integer> quantite;

}