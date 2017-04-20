import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zuoduzuodu on 20/04/2017.
 */
//TODO: regarder pour le probleme de qqn qui viens de s'authentifier et qui tape index.html, ca marche quand meme
public class AuthFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        // permettre l'acces aux ressources tout le temps et empecher une redirection en boucle
        String reqResource = request.getRequestURI().substring(request.getContextPath().length());
        String action = request.getParameter("action");
        if (reqResource.startsWith("/resource") || (reqResource.startsWith("/ActionServlet") && (action.equals("authentifierClient")
                || action.equals("inscrireClient"))) || reqResource.endsWith("/")) {
            chain.doFilter(request, response);
        } else {
            //SI REQUETE UN AUTRE SITE QUE RESOURCE OU INDEX OU LES DEUX SERVICES DU HAUT
            // si la visite n'a pas de session = jamais été connectée
            if (session == null) {
                System.out.println("session == null et forward");
                response.sendRedirect("/");
            } else {
                // si l'utilisateur est déjà connecté
                if (session.getAttribute("user") != null) {
                    System.out.println("user : " + session.getAttribute("user") + " is auth-ed and is going to his website...");
                    chain.doFilter(request, response);
                } else {
                    System.out.println("session != null & user == null and getting him to index ");
                    response.sendRedirect("/");// si session mais pas de user -> retour accueil
                }
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
