/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
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

    @Override
    public void init() throws ServletException {
        super.init();
        JpaUtil.init();
    }

    @Override
    public void destroy() {
        JpaUtil.destroy();
        pause();
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

        //PROCEDURE POUR CHAQUE POST ET GET : SESSION ET RECUP DE CE QUI FAUT FAIRE
        HttpSession session = request.getSession(true);
        String methode = request.getMethod();
        String action = request.getParameter("action");

        //TRAITEMENT EN FONCTION DE L'ACTION DESIREE

        if(action.equals("authentifierClient")){
            if(session.getAttribute("user")!=null){
                System.out.println("User already auth, redirect...");
                response.sendRedirect("/index.html");
            }
            System.out.println("appel2 du service authentifierClient");
            String email = request.getParameter("email");
            long id = Long.parseLong(request.getParameter("pwd"));
            System.out.println("de "+email+id);
            Client currentUser = null;
            try{
                currentUser = metier.authentifierClient(email, id);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(currentUser != null){
                //TRES IMPORTANT PERMET DE VERIFIER SI LA SESSION A ETE AUTHENTIFIEE AVANT
                session.setAttribute("user",email);
                response.sendRedirect("/authSuccess.html");
            }
        }

        if(action.equals("recupererListeRestaurants")){
            System.out.println("Appel du service recupererListeRestaurants");
            List<Restaurant> restaurants = null;
            try{
                restaurants = metier.recupererListeRestaurants();
            }catch(Exception e){
                e.printStackTrace();
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter pw = response.getWriter();
            printListeRestaurants(pw,restaurants);
            pw.close();
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
        processRequest(request, response);
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
