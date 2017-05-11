/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zyao
 */

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.*;
import metier.service.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zyao
 */
class AuthentifierLivreur {


    int execute(HttpServletRequest request, ServiceMetier metier) {
        String email = request.getParameter("email");
        long id = Long.parseLong(request.getParameter("pwd"));
        System.out.println("de " + email + id);
        List<Livreur> livreurs = null;
        try {
            livreurs = metier.authentifierLivreur(email, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (livreurs == null) {
            System.out.println("[AuthentifierLivreur] admin auth.");
            HttpSession session = request.getSession(true);
            session.setAttribute("user", email);
            session.setAttribute("id", id);
            return 0;
        }
        if (livreurs.isEmpty()) {
            System.out.println("[AuthentifierLivreur] authtication error");
            request.setAttribute("errorMessage", "Ce compte Livreur n'existe pas ! Veuillez réessayer.");
            return 1;
        } else {
            System.out.println("[AuthentifierLivreur] livreurs found");
            HttpSession session = request.getSession(true);
            session.setAttribute("user", email);
            session.setAttribute("id", id);
            List<Long> idLivreursEnLivraison = new ArrayList<>();
            for (Livreur i : livreurs) {
                if (i.getStatut().equals("livraison")) idLivreursEnLivraison.add(i.getId());
            }
            //ecriture des donnees
            System.out.println("nbr livraisons a faire " + idLivreursEnLivraison.size());
            request.setAttribute("livreursEnCours", idLivreursEnLivraison);
            if (livreurs.get(0) instanceof Cycliste) {
                System.out.println("auth Cycliste");
                return 2;
            }
            if (livreurs.get(0) instanceof Drone) {
                System.out.println("auth Gestionnaire");
                return 3;
            }
        }
        return 4;
    }

    int bypass(HttpServletRequest request, ServiceMetier metier, String user,long id) {
        List<Livreur> livreurs = null;
        try {
            livreurs = metier.authentifierLivreur(user,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Long> idLivreursEnLivraison = new ArrayList<>();
        for (Livreur i : livreurs){
            if(i.getStatut().equals("livraison")) idLivreursEnLivraison.add(i.getId());
        }
        //ecriture des donnees
        System.out.println("nbr livraisons a faire "+ idLivreursEnLivraison.size());
        request.setAttribute("livreursEnCours",idLivreursEnLivraison);
        if(livreurs.get(0) instanceof Cycliste){
            System.out.println("auth Cycliste");
            return 2;
        }        if(livreurs.get(0) instanceof Drone){
            System.out.println("auth Gestionnaire");
            return 3;
        }
        return 4;
    }
}
