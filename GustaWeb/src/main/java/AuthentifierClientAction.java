
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
public class AuthentifierClientAction {
    
    public boolean sessionExists(HttpServletRequest request){
        System.out.println("appel du service authentifierClient");
                if (ServletBis.currentUserList.contains(request.getParameter("email"))) {
                    System.out.println("utilisateur deja auth sur un autre servlet");
                    request.setAttribute("errorMessage","Vous êtes déjà connecté sur ce compte ailleurs ! Veuillez vous déconnecter."); 
                    return true;
                    }else{
                          
                       return false;
                          }
                
    }
    public Client authentification(HttpServletRequest request, ServiceMetier metier){
                String email = request.getParameter("email");
                long id = Long.parseLong(request.getParameter("pwd"));
                System.out.println("de " + email + id);
                Client currentUser = null; 
                try {
                    currentUser = metier.authentifierClient(email, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return currentUser;
    }
    
    public void noAccount(HttpServletRequest request){
                     System.out.println("This account does not exist, redirecting...");
                    request.setAttribute("errorMessage","Ce compte n'existe pas ! Veuillez réessayer.");                   
    }
     public void alreadyAccount(HttpServletRequest request,HttpSession session,ServiceMetier metier, Client currentUser){
                String email = request.getParameter("email");
                long id = Long.parseLong(request.getParameter("pwd"));
                    session = request.getSession(true);
                    session.setAttribute("user", email);
                    session.setAttribute("client",currentUser);
                    List<Restaurant> restaurants = null;
                    try {
                        restaurants = metier.recupererListeRestaurants();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    request.setAttribute("listeResto",restaurants);                   
    }
    
    
}
