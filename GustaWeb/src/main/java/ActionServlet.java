import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.JpaUtil;
import metier.service.*;


public class ActionServlet extends HttpServlet {

    ServiceMetier metier;
    public static Set<String> currentUserList = new HashSet<>();
    final static int CHARGE_MAX_LIMIT = 8000;

    @Override
    public void init() throws ServletException {
        super.init();
        JpaUtil.init();
        metier = new ServiceMetier();
        if(currentUserList.isEmpty()){
            System.out.println("[Servlet] init : CurrentUserList empty.");
        }
    }

    @Override
    public void destroy() {
        JpaUtil.destroy();
        super.destroy();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//-----------------------------------------------------------------------------------
        String action = request.getParameter("action");
        System.out.println("[Servlet] AS called with : "+action);
        HttpSession session = request.getSession(false);
//-----------------------------------------------------------------------------------
        if (session == null) {
            //----------------------------------------------------------------------------
            switch (action) {
                case "authentifierClient":
                    {
                        if (currentUserList.contains(request.getParameter("email"))) {
                            request.setAttribute("errorMessage","Vous êtes déjà connecté sur ce compte ailleurs ! Veuillez vous déconnecter.");
                            request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                            return;
                        }       AuthentifierClient act = new AuthentifierClient();
                        if(act.execute(request,metier)){
                            request.getRequestDispatcher("/app/restaurantDirectory.jsp").forward(request, response);
                        } else {
                            request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                            return;
                        }       break;
                    }
            //-----------------------------------------------------------------------------------
                case "authentifierLivreur":
                {
                    if(currentUserList.contains(request.getParameter("email"))){
                        request.setAttribute("errorMessage","Vous êtes déjà connecté sur ce compte ailleurs ! Veuillez vous déconnecter.");
                        request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                        return;
                    }
                    AuthentifierLivreur act = new AuthentifierLivreur();
                    int code = act.execute(request,metier);
                    switch (code){
                        case 0:
                            response.sendRedirect("/");//todo : page d'admin ??
                            break;
                        case 1:
                            request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                            break;
                        case 2:
                            request.getRequestDispatcher("/app/livraisonCycliste.jsp").forward(request,response);
                            break;
                        case 3:
                            request.getRequestDispatcher("/app/livraisonGestionnaire.jsp").forward(request,response);
                            break;
                        default:
                            request.setAttribute("errorMessage","Une erreur inattendue s'est produite. Veuillez essayer plus tard !");
                            request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                    }
                    return;
                }
                case "inscrireClient":
                {
                    InscrireClient act = new InscrireClient();
                    if(act.execute(request,metier)){
                        response.sendRedirect("/inscriptionSuccess.html");
                        return;
                    }else{
                        request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                    }
                    return;
                }
                default:
                    System.out.println("[Servlet] Bad Servlet call : no authentification");
                    request.setAttribute("errorMessage","Vous n'êtes pas authentifié ! Veuillez vous connecter avec votre compte.");
                    request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
            }
        } else if (session.getAttribute("user") != null) {
            System.out.println("[Servlet] session is alive, proceed to service execution.");
            //authentiferClient
            //-----------------------------------------------------------------------------------
            if (action.equals("authentifierClient")) {
                AuthentifierClient act = new AuthentifierClient();
                act.bypass(request,metier);
                request.getRequestDispatcher("/app/restaurantDirectory.jsp").forward(request,response);
                return;
            }
            //-----------------------------------------------------------------------------------
            //authentifierLivreur
            if(action.equals("authentifierLivreur")){
                AuthentifierLivreur act = new AuthentifierLivreur();
                int code = act.bypass(request,metier,(String)session.getAttribute("user"),(long)session.getAttribute("id"));
                switch (code){
                    case 2:
                        request.getRequestDispatcher("/app/livraisonCycliste.jsp").forward(request,response);
                        break;
                    case 3:
                        request.getRequestDispatcher("/app/livraisonGestionnaire.jsp").forward(request,response);
                        break;
                    default:
                        request.setAttribute("errorMessage","Une erreur inattendue s'est produite. Veuillez essayer plus tard !");
                        request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                }
                return;
            }
            //-----------------------------------------------------------------------------------
            //recupererRestaurant
            //-----------------------------------------------------------------------------------
            if(action.equals("afficherCarte")){
                AfficherCarte act = new AfficherCarte();
                act.execute(request,metier);
                request.getRequestDispatcher("/app/carteDirectory.jsp").forward(request,response);
                return;
            }
            //-----------------------------------------------------------------------------------
            //attribuerLivreur
            //-----------------------------------------------------------------------------------
            if(action.equals("attribuerLivreur")){
                AttribuerLivreur act = new AttribuerLivreur();
                int code = act.execute(request, metier);
                switch (code){
                    case 0:
                        response.sendRedirect("/dashboard?action=authentifierClient");
                        break;
                    case 1:
                        request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                        break;
                    case 2:
                        request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                }
            }
            //-----------------------------------------------------------------------------------
            //recupererCommandeParID
            if(action.equals("recupererCommande")){
                RecupererCommande act = new RecupererCommande();
                act.execute(request,response,metier);
                return;
            }
            //-----------------------------------------------------------------------------------
            //cloturerLivraison
            if(action.equals("cloturerCommande")){
                CloturerCommande act = new CloturerCommande();
                act.execute(request, metier);
                response.sendRedirect("/dashboard?action=authentifierLivreur");
                return;
            }
            //-----------------------------------------------------------------------------------
            //deconnexion
            //-----------------------------------------------------------------------------------
            if(action.equals("deconnexion")){
                session.invalidate();
                response.sendRedirect("/");
            }
        }//todo : bonus si on veut faire la page d'admin
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[Servlet] request method : GET");
        processRequest(request,response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[Servlet] request method : POST");
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "ActionServlet is the main Controller of our application.";
    }
}