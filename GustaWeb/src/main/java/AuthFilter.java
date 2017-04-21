import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zuoduzuodu on 20/04/2017.
 * Ce filtre empeche des requetes directement dans "/app".
 */
public class AuthFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        // si la visite n'a pas de session = jamais été connectée
        if (session == null) {
            System.out.println("[AuthFilter] session == null : redirect to index");
            response.sendRedirect("/");
        } else {
            // si l'utilisateur est déjà connecté
            if (session.getAttribute("user") != null) {
                System.out.println("[AuthFilter] user : " + session.getAttribute("user") + " passed.");
                chain.doFilter(request, response);
            } else {
                System.out.println("[AuthFilter] session != null & user == null : redirect to index ");
                response.sendRedirect("/");// si session mais pas de user -> retour accueil
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
