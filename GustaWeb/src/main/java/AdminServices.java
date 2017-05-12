import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Livreur;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class AdminServices {
    void execute(HttpServletResponse response, int code, ServiceMetier metier) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("ISO-8859-15");
        switch (code){
            case 0:
                int connectedUsers = ActionServlet.currentUserList.size();
                List<Client> clients = null;
                List<Livreur> livreurs = null;
                List<Restaurant> restos = null;
                List<Commande> commandes = null;
                try {
                    clients = metier.recupererListeClients();
                    livreurs = metier.recupererListeLivreurs();
                    restos = metier.recupererListeRestaurants();
                    commandes = metier.recupererLivraisonEnCours();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int sizeClients = clients != null ? clients.size() : 0;
                int sizeLiveurs = livreurs != null ? livreurs.size() : 0;
                int sizeRestos = restos != null ? restos.size() : 0;
                int sizeCommandes = commandes != null ? commandes.size() : 0;
                printInfos(response.getWriter(),connectedUsers,sizeClients,sizeLiveurs,sizeRestos,sizeCommandes);
                break;
            case 1:
                List<Restaurant> listrestos = null;
                try {
                    listrestos = metier.recupererListeRestaurants();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                printListeRestaurants(response.getWriter(),listrestos);
                break;
            case 2:
                List<Livreur> listlivreurs = null;
                try {
                    listlivreurs = metier.recupererListeLivreurs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                printListeLivreurs(response.getWriter(),listlivreurs);
                break;
            case 3:
                List<Commande> listcommandes = null;
                try {
                    listcommandes = metier.recupererLivraisonEnCours();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                printListeLivraisons(response.getWriter(),listcommandes);
                break;
        }
    }

    private void printListeRestaurants(PrintWriter pw, List<Restaurant> liste)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonliste = new JsonArray();
        for(Restaurant r: liste)
        {
            JsonObject jsonRestaurant = new JsonObject();
            jsonRestaurant.addProperty("id",r.getId());
            jsonRestaurant.addProperty("denomination",r.getDenomination());
            jsonRestaurant.addProperty("description",r.getDescription());
            jsonRestaurant.addProperty("adresse",r.getAdresse());
            jsonliste.add(jsonRestaurant);
        }
        JsonObject container = new JsonObject();
        container.add("restaurants",jsonliste);
        pw.println(gson.toJson(container));
    }

    private void printListeLivreurs(PrintWriter pw,List<Livreur> liste)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonliste = new JsonArray();
        for(Livreur r: liste)
        {
            JsonObject jsonLivreur = new JsonObject();
            jsonLivreur.addProperty("id",r.getId());
            jsonLivreur.addProperty("mail",r.getMail());
            jsonLivreur.addProperty("adresse",r.getAdresse());
            jsonLivreur.addProperty("statut",r.getStatut());
            jsonliste.add(jsonLivreur);
        }
        JsonObject container = new JsonObject();
        container.add("livreurs",jsonliste);
        pw.println(gson.toJson(container));
    }

    private void printListeLivraisons(PrintWriter pw,List<Commande> liste)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonliste = new JsonArray();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy à HH:mm");
        for(Commande r: liste)
        {
            JsonObject jsonCommande = new JsonObject();
            jsonCommande.addProperty("id",r.getId());
            jsonCommande.addProperty("mail",r.getClient().getMail());
            jsonCommande.addProperty("livreur",r.getLivreur().getMail());
            jsonCommande.addProperty("prix",r.getPrixTotal());
            jsonCommande.addProperty("date",df.format(r.getDateCommmande()));
            jsonliste.add(jsonCommande);
        }
        JsonObject container = new JsonObject();
        container.add("commandes",jsonliste);
        pw.println(gson.toJson(container));
    }

    private void printInfos(PrintWriter pw,int users,int clients, int livreurs, int restos, int commandes)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject data = new JsonObject();
        data.addProperty("currentUsers",users);
        data.addProperty("nbrClients",clients);
        data.addProperty("nbrLivreurs",livreurs);
        data.addProperty("nbrRestos",restos);
        data.addProperty("nbrCommandes",commandes);
        pw.println(gson.toJson(data));
    }
}
