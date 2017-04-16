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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.JpaUtil;
import metier.modele.Restaurant;
import metier.service.*;


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

        String methode = request.getMethod();
        String action = request.getParameter("action");
        if(action.equals("recupererListeRestaurants")){
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
