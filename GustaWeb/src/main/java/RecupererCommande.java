import com.google.gson.*;
import metier.modele.Commande;
import metier.modele.ProduitsCommandes;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class RecupererCommande {

    void execute(HttpServletRequest request, HttpServletResponse response, ServiceMetier metier) throws IOException {
        long livreurID = Long.parseLong(request.getParameter("livreurID"));
        Commande commande = null;
        Restaurant restaurant = null;
        try {
            commande = metier.recupererCommande(livreurID);
            if(commande.getListeProduits().size() == 0){
                System.out.println("liste produit vide : cloture automatique");
                metier.cloturerLivraison(commande.getId());
                return;
            }
            restaurant = metier.recupererRestaurantParIdProduit(commande.getListeProduits().get(0).getProduit().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("ISO-8859-15");
        PrintWriter pw = response.getWriter();
        printCommande(pw, commande,restaurant);
        pw.close();
        System.out.println("[RecupererCommande] json print over.");
    }

    private void printCommande(PrintWriter pw,Commande commande,Restaurant restaurant)
    {
        jsonPrinter jp = new jsonPrinter();
        jp.printCommande(pw,commande,restaurant);
    }
}
