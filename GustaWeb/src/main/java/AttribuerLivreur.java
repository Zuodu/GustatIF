import metier.modele.*;
import metier.service.ServiceMetier;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zifanYao on 06/05/2017.
 */
public class AttribuerLivreur {

    int execute(HttpServletRequest request, ServiceMetier metier){
        Map<String, String[]> paramIds = request.getParameterMap();
        ArrayList<ProduitsCommandes> produitscommandes = new ArrayList<>();
        Produit currentProduit = null;
        Restaurant currentResto = null;
        Client currentClient = null;
        for (String paramName : paramIds.keySet()) {
            if (paramName.equals("action")) {
            } else if (paramName.equals("restoID")) {
                long restoID = Long.parseLong(paramIds.get(paramName)[0]);
                try {
                    currentResto = metier.recupererRestaurant(restoID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("[AttribuerLivreur] current resto " + currentResto.getDenomination());
            } else if (paramName.equals("clientID")) {
                long clientID = Long.parseLong(paramIds.get(paramName)[0]);
                try {
                    currentClient = metier.recupererClient(clientID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("[AttribuerLivreur] current client " + currentClient.getNom());
            } else {
                int paramValueInt = Integer.parseInt(paramIds.get(paramName)[0]);
                System.out.println("value parser " + paramValueInt + " of " + paramName);
                if (paramValueInt > 0) {
                    try {
                        int paramInt = Integer.parseInt(paramName);
                        currentProduit = metier.recupererProduit(paramInt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ProduitsCommandes pc = new ProduitsCommandes(currentProduit, Integer.parseInt(paramIds.get(paramName)[0]));
                    produitscommandes.add(pc);
                }
            }
        }
        if(produitscommandes.isEmpty()){
            System.out.println("[AttribuerLivreur] attribution failed : no product were chosen.");
            request.setAttribute("errorMessage","Echec de la commande : vous n'avez rien commandé ! GustaPanda est triste...");
            request.setAttribute("redirectTarget","dashboard?action=authentifierClient");
            return 1;
        }
        //fin for
        Livreur livreur = null;
        try {
            livreur = metier.attribuerLivreur(currentResto,produitscommandes,currentClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(livreur != null){
            System.out.println("[AttribuerLivreur] attribution to : "+livreur.getId());
            return 0;
        }else{
            System.out.println("[AttribuerLivreur] attribution failed.");
            request.setAttribute("errorMessage","Echec de la commande : veuillez réessayer plus tard !");
            request.setAttribute("redirectTarget","dashboard?action=authentifierClient");
            return 2;
        }
    }
}
