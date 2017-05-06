import metier.modele.Produit;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zifanYao on 05/05/2017.
 */
class AfficherCarte {

    void execute(HttpServletRequest request, ServiceMetier metier){
        long restoId = Long.parseLong(request.getParameter("restoId"));
        Restaurant resto = null;
        List<Produit> produits = null;
        try {
            resto = metier.recupererRestaurant(restoId);
            produits = metier.recupererListeProduits(restoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("resto",resto);
        request.setAttribute("carte",produits);
        request.setAttribute("chargeMaxLimit",ActionServlet.CHARGE_MAX_LIMIT);
    }
}
