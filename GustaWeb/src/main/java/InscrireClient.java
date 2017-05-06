import metier.modele.Client;
import metier.service.ServiceMetier;

import javax.servlet.http.HttpServletRequest;

class InscrireClient {

    boolean execute(HttpServletRequest request, ServiceMetier metier){
        String[] clientString = {request.getParameter("nom"),request.getParameter("prenom"),
                request.getParameter("email"),request.getParameter("adresse")};
        Client newClient = new Client(clientString[0],clientString[1],clientString[2],clientString[3]);
        if(metier.inscrireClient(newClient)){
            System.out.println("[InscrireClient] inscription successful.");
            return true;
        }else{
            System.out.println("[InscrireClient] inscription failed.");
            request.setAttribute("errorMessage","Echec de l'inscription, veuillez recommencer plus tard !");
            return false;
        }

    }
}
