import com.google.gson.*;
import metier.modele.Commande;
import metier.modele.ProduitsCommandes;
import metier.modele.Restaurant;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class jsonPrinter {

    void printCommande(PrintWriter pw, Commande commande, Restaurant restaurant){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonCommande = new JsonObject();
        JsonArray jsonListeProduits = new JsonArray();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy à HH:mm");
        jsonCommande.addProperty("date",df.format(commande.getDateCommmande()));
        jsonCommande.addProperty("livreur",commande.getLivreur().getMail()+" (no "+commande.getLivreur().getId()+")");
        jsonCommande.addProperty("livreurLat",commande.getLivreur().getLatitude());
        jsonCommande.addProperty("livreurLng",commande.getLivreur().getLongitude());
        jsonCommande.addProperty("restaurant",restaurant.getDenomination());
        jsonCommande.addProperty("restaurantLat",restaurant.getLatitude());
        jsonCommande.addProperty("restaurantLng",restaurant.getLongitude());
        jsonCommande.addProperty("client",commande.getClient().getNom()+" "+commande.getClient().getPrenom());
        jsonCommande.addProperty("clientAdr",commande.getClient().getAdresse());
        jsonCommande.addProperty("clientLat",commande.getClient().getLatitude());
        jsonCommande.addProperty("clientLng",commande.getClient().getLongitude());
        jsonCommande.addProperty("total",commande.getPrixTotal());
        for(ProduitsCommandes i : commande.getListeProduits()){
            JsonPrimitive string = new JsonPrimitive(i.toString());
            jsonListeProduits.add(string);
        }
        jsonCommande.add("produitsCommandes",jsonListeProduits);
        pw.println(gson.toJson(jsonCommande));
    }

}
