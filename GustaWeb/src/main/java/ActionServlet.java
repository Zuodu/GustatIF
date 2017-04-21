/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.JpaUtil;
import metier.modele.*;
import metier.service.*;

import static util.Saisie.pause;


/**
 *
 * @author zifanYao
 */
public class ActionServlet extends HttpServlet {

    public static List<String> currentUserList = new ArrayList<String>();

    @Override
    public void init() throws ServletException {
        super.init();
        JpaUtil.init();
        if(currentUserList.isEmpty()){
            System.out.println("currentUserList is empty");
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
        //init des services metiers et autre
        ServiceMetier metier = new ServiceMetier();
//-----------------------------------------------------------------------------------
        //PROCEDURE POUR CHAQUE POST ET GET : SESSION ET RECUP DE CE QUI FAUT FAIRE
        String action = request.getParameter("action");
        System.out.println("[Servlet] called with : "+" for "+action);
        HttpSession session = request.getSession(false);
//-----------------------------------------------------------------------------------
        //SI YA PAS DE SESSION
        if (session == null) {
            //SI LE CLIENT VEUT S'AUTENTIFIER
            if (action.equals("authentifierClient")) {
                // SI LE CLIENT A DEJA UNE SESSION AUTRE PART EN COURS
                if (currentUserList.contains(request.getParameter("email"))) {
                    System.out.println("utilisateur deja auth sur un autre servlet");
                    response.sendRedirect("/");
                    return;
                }
                System.out.println("appel du service authentifierClient");
                String email = request.getParameter("email");
                long id = Long.parseLong(request.getParameter("pwd"));
                System.out.println("de " + email + id);
                Client currentUser = null;
                try {
                    currentUser = metier.authentifierClient(email, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (currentUser != null) { // SI YA BIEN CE COMPTE
                    session = request.getSession(true);
                    session.setAttribute("user", email);
                    response.sendRedirect("/app/authSuccess.html");
                    return;
                } else { //SI YA PAS CE COMPTE
                    System.out.println("This account does not exist, redirecting...");
                    response.sendRedirect("/"); //todo; envoyer vers error jsp
                    return;
                }
            }else if(action.equals("inscrireClient")){
                //-----------------------------------------------------------------------------------
                //inscrireClient
                System.out.println("appel du service inscrireClient");
                String[] clientString = {request.getParameter("nom"),request.getParameter("prenom"),
                        request.getParameter("email"),request.getParameter("adresse")};
                Client newClient = new Client(clientString[0],clientString[1],clientString[2],clientString[3]);
                if(metier.inscrireClient(newClient)){
                    System.out.println("inscription successful.");
                    response.sendRedirect("/");//todo: changer en la page jsp
                    return;
                }
            }else{//SI LE CLIENT VEUT FAIRE UN AUTRE CALL SANS ETRE AUTH OU UN MAUVAIS SERVICE
                System.out.println("ServiceMetier call without auth !");
                response.sendRedirect("/");
                return;
            }
        } else if (session.getAttribute("user") != null) { // SI LA SESSION EN COURS A DEJA UN NOM D'UTILISATEUR
            System.out.println("session already auth, ok for ServiceMetier calls.");
            //TRAITEMENT EN FONCTION DE L'ACTION DESIREE :

            //authentiferClient
            //-----------------------------------------------------------------------------------
            if (action.equals("authentifierClient")) {
                System.out.println("authentifierClient call with session+user already, redirection...");
                response.sendRedirect("/app/authSuccess.html");
                return;
            }
            //-----------------------------------------------------------------------------------
            //recupererListeRestaurants
            //-----------------------------------------------------------------------------------
            if (action.equals("recupererListeRestaurants")) {
                System.out.println("Appel du service recupererListeRestaurants");
                List<Restaurant> restaurants = null;
                try {
                    restaurants = metier.recupererListeRestaurants();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pw = response.getWriter();
                printListeRestaurants(pw, restaurants);
                pw.close();
                return;
            }
            //-----------------------------------------------------------------------------------
            //recupererRestaurant
            //-----------------------------------------------------------------------------------
            if(action.equals("recupererRestaurant")){
/*                    long restaurantID = Long.parseLong(request.getParameter("restaurantID"));
                    Restaurant target = null;
                    try{
                        target = metier.recupererRestaurant(restaurantID);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter pw = response.getWriter();
                    printRestaurant(pw, target);
                    pw.close();
                    return;*/
            }
            //-----------------------------------------------------------------------------------
        }
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
        System.out.println("request method was not POST, redirection to index.");
        response.sendRedirect("/");
        return;
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    //PRIVATE

    private void printListeRestaurants(PrintWriter pw,List<Restaurant> liste)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonliste = new JsonArray();
        for(Restaurant r: liste)
        {
            JsonObject jsonRestaurant = new JsonObject();
            jsonRestaurant.addProperty("id",r.getId());
            jsonRestaurant.addProperty("denomination",r.getDenomination());
            jsonRestaurant.addProperty("description",r.getDescription());
            jsonRestaurant.addProperty("adresse",r.getAdresse());
            jsonliste.add(jsonRestaurant);
        }
        JsonObject container = new JsonObject();
        container.add("restaurants",jsonliste);
        pw.println(gson.toJson(container));
    }

}