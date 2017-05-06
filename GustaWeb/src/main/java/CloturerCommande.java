import metier.modele.Commande;
import metier.service.ServiceMetier;

import javax.servlet.http.HttpServletRequest;

class CloturerCommande {

    void execute(HttpServletRequest request, ServiceMetier metier){
        Commande commandeACloturer;
        try {
            commandeACloturer = metier.recupererCommande(Long.parseLong(request.getParameter("livreurID")));
            metier.cloturerLivraison(commandeACloturer.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
