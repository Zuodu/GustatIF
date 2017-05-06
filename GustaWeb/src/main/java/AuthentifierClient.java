
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.*;
import metier.service.*;

class AuthentifierClient {

    boolean execute(HttpServletRequest request, ServiceMetier metier) {
        String email = request.getParameter("email");
        long id = Long.parseLong(request.getParameter("pwd"));
        Client currentUser = null;
        try {
            currentUser = metier.authentifierClient(email, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentUser != null) { // SI YA BIEN CE COMPTE
            HttpSession session = request.getSession(true);
            session.setAttribute("user", email);
            session.setAttribute("client", currentUser);
            List<Restaurant> restaurants = null;
            try {
                restaurants = metier.recupererListeRestaurants();
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.setAttribute("listeResto", restaurants);
            System.out.println("[AuthentifierClient] authentication success.");
            return true;
        } else {
            System.out.println("[AuthentifierClient] authentication fail.");
            request.setAttribute("errorMessage","Ce compte n'existe pas ! Veuillez réessayer.");
            return false;
        }
    }

    void bypass(HttpServletRequest request, ServiceMetier metier){
        List<Restaurant> restaurants = null;
        try {
            restaurants = metier.recupererListeRestaurants();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("listeResto",restaurants);
    }
}
