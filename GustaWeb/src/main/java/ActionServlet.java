/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.*;
import dao.CommandeDAO;
import dao.JpaUtil;
import metier.modele.*;
import metier.service.*;

import static util.Saisie.pause;


/**
 *
 * @author zifanYao
 */

public class ActionServlet extends HttpServlet {

    public static Set<String> currentUserList = new HashSet<>();
    final int CHARGE_MAX_LIMIT = 8000;

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
        System.out.println("[Servlet] called with : "+action);
        HttpSession session = request.getSession(false);
//-----------------------------------------------------------------------------------
        //SI YA PAS DE SESSION
        if (session == null) {
            //SI LE CLIENT VEUT S'AUTENTIFIER
            if (action.equals("authentifierClient")) {
                // SI LE CLIENT A DEJA UNE SESSION AUTRE PART EN COURS
                System.out.println("appel du service authentifierClient");
                if (currentUserList.contains(request.getParameter("email"))) {
                    System.out.println("utilisateur deja auth sur un autre servlet");
                    request.setAttribute("errorMessage","Vous êtes déjà connecté sur ce compte ailleurs ! Veuillez vous déconnecter.");
                    request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                    return;
                }
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
                    session.setAttribute("client",currentUser);
                    List<Restaurant> restaurants = null;
                    try {
                        restaurants = metier.recupererListeRestaurants();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    request.setAttribute("listeResto",restaurants);
                    request.getRequestDispatcher("/app/restaurantDirectory.jsp").forward(request,response);
                    return;
                } else { //SI YA PAS CE COMPTE
                    System.out.println("This account does not exist, redirecting...");
                    request.setAttribute("errorMessage","Ce compte n'existe pas ! Veuillez réessayer.");
                    request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                    return;
                }
            }
            else if(action.equals("authentifierLivreur")) {
                System.out.println("appel du service authentifierLivreur");
                if(currentUserList.contains(request.getParameter("email"))){
                    System.out.println("utilisateur deja auth sur un autre servlet");
                    request.setAttribute("errorMessage","Vous êtes déjà connecté sur ce compte ailleurs ! Veuillez vous déconnecter.");
                    request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                    return;
                }
                String email = request.getParameter("email");
                long id = Long.parseLong(request.getParameter("pwd"));
                System.out.println("de " + email + id);
                List<Livreur> livreurs = null;
                try {
                    livreurs = metier.authentifierLivreur(email,id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //ADMIN
                if(livreurs == null){
                    System.out.println("c'est l'admin");
                    session = request.getSession(true);
                    session.setAttribute("user",email);
                    session.setAttribute("livreurs",livreurs);
                    response.sendRedirect("/");//todo : page d'admin ??
                    return;
                }
                //ERREUR D'AUTH
                if(livreurs.size() == 0){
                    System.out.println("erreur d'auth Livreur");
                    request.setAttribute("errorMessage","Ce compte Livreur n'existe pas ! Veuillez réessayer.");
                    request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                    return;
                } else {
                    System.out.println("livreurs trouves");
                    session = request.getSession(true);
                    session.setAttribute("user",email);
                    session.setAttribute("id",id);
                    List<Long> idLivreursEnLivraison = new ArrayList<>();
                    for (Livreur i : livreurs){
                        if(i.getStatut().equals("livraison")) idLivreursEnLivraison.add(i.getId());
                    }
                    //ecriture des donnees
                    System.out.println("nbr livraisons a faire "+ idLivreursEnLivraison.size());
                    request.setAttribute("livreursEnCours",idLivreursEnLivraison);
                    if(livreurs.get(0) instanceof Cycliste){
                        System.out.println("auth Cycliste");
                        request.getRequestDispatcher("/app/livraisonCycliste.jsp").forward(request,response);
                        return;
                    }
                    if(livreurs.get(0) instanceof Drone){
                        System.out.println("auth Gestionnaire");
                        request.getRequestDispatcher("/app/livraisonGestionnaire.jsp").forward(request,response);
                        return;
                    }
                }
            }
            else if(action.equals("inscrireClient")){
                //-----------------------------------------------------------------------------------
                //inscrireClient
                System.out.println("appel du service inscrireClient");
                String[] clientString = {request.getParameter("nom"),request.getParameter("prenom"),
                        request.getParameter("email"),request.getParameter("adresse")};
                Client newClient = new Client(clientString[0],clientString[1],clientString[2],clientString[3]);
                if(metier.inscrireClient(newClient)){
                    System.out.println("inscription successful.");
                    response.sendRedirect("/inscriptionSuccess.html");
                    return;
                }

            }
            else{//SI LE CLIENT VEUT FAIRE UN AUTRE CALL SANS ETRE AUTH OU UN MAUVAIS SERVICE
                System.out.println("ServiceMetier call without auth !");
                request.setAttribute("errorMessage","Vous n'êtes pas authentifié ! Veuillez vous connecter avec votre compte.");
                request.getRequestDispatcher("/errorMessage.jsp").forward(request,response);
                return;
            }
        //SESSION NOT NULL
        } else if (session.getAttribute("user") != null) { // SI LA SESSION EN COURS A DEJA UN NOM D'UTILISATEUR
            System.out.println("session already auth, ok for ServiceMetier calls.");
            //TRAITEMENT EN FONCTION DE L'ACTION DESIREE :
            //authentiferClient
            //-----------------------------------------------------------------------------------
            if (action.equals("authentifierClient")) {
                System.out.println("appel du service authentifierClient avec une session");
                List<Restaurant> restaurants = null;
                try {
                    restaurants = metier.recupererListeRestaurants();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.setAttribute("listeResto",restaurants);
                request.getRequestDispatcher("/app/restaurantDirectory.jsp").forward(request,response);
                return;
            }
            //-----------------------------------------------------------------------------------
            //authentifierLivreur
            if(action.equals("authentifierLivreur")){
                System.out.println("appel du service authentifierLivreur avec une session");
                List<Livreur> livreurs = null;
                try {
                    livreurs = metier.authentifierLivreur((String)session.getAttribute("user"),(long)session.getAttribute("id"));
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
                    request.getRequestDispatcher("/app/livraisonCycliste.jsp").forward(request,response);
                    return;
                }
                if(livreurs.get(0) instanceof Drone){
                    System.out.println("auth Gestionnaire");
                    request.getRequestDispatcher("/app/livraisonGestionnaire.jsp").forward(request,response);
                    return;
                }
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
                response.setCharacterEncoding("ISO-8859-1");
                PrintWriter pw = response.getWriter();
                printListeRestaurants(pw, restaurants);
                pw.close();
                return;
            }
            //-----------------------------------------------------------------------------------
            //recupererRestaurant
            //-----------------------------------------------------------------------------------
            if(action.equals("afficherCarte")){
                System.out.println("appel du service afficherCarte");
                long restoId = Long.parseLong(request.getParameter("restoId"));
                Restaurant resto = null;
                List<Produit> produits = null;
                try {
                    resto = metier.recupererRestaurant(restoId);
                    produits = metier.recupererListeProduits(restoId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.setAttribute("resto",resto);
                request.setAttribute("carte",produits);
                request.setAttribute("chargeMaxLimit",CHARGE_MAX_LIMIT);
                request.getRequestDispatcher("/app/carteDirectory.jsp").forward(request,response);
                return;
            }
            //-----------------------------------------------------------------------------------
            //attribuerLivreur
            //-----------------------------------------------------------------------------------
            if(action.equals("attribuerLivreur")){
                /*TODO: alors ca c compliqué... il faut vérifier que les arguments sont valides :
                que l'id donné est bien dans le resto, et que l'utilisateur ne change pas les params
                */
                System.out.println("appel du service attribuerLivreur");
                Map<String, String[]> paramIds = request.getParameterMap();
                ArrayList<ProduitsCommandes> produitscommandes = new ArrayList<>();
                Produit currentProduit = null;
                Restaurant currentResto = null;
                Client currentClient = null;
                for (String paramName : paramIds.keySet()) {
                    if(paramName.equals("action")){
                    }else if(paramName.equals("restoID")){
                        long restoID = Long.parseLong(paramIds.get(paramName)[0]);
                        try {
                            currentResto = metier.recupererRestaurant(restoID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("current resto "+currentResto.getDenomination());
                    }else if(paramName.equals("clientID")){
                        long clientID = Long.parseLong(paramIds.get(paramName)[0]);
                        try {
                            currentClient = metier.recupererClient(clientID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("current client "+currentClient.getNom());
                    }else{
                        int paramValueInt = Integer.parseInt(paramIds.get(paramName)[0]);
                        System.out.println("value parser "+paramValueInt+" of "+paramName);
                        if(paramValueInt >0) {
                            try {
                                int paramInt = Integer.parseInt(paramName);
                                currentProduit = metier.recupererProduit(paramInt);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ProduitsCommandes pc = new ProduitsCommandes(currentProduit, Integer.parseInt(paramIds.get(paramName)[0]));
                            produitscommandes.add(pc);
                        }
                    }
                }
                //fin for
                Livreur livreur = null;
                try {
                    livreur = metier.attribuerLivreur(currentResto,produitscommandes,currentClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(livreur != null){
                    System.out.println("attribution to : "+livreur.getId());
                    response.sendRedirect("/dashboard?action=authentifierClient");
                }else{
                    System.out.println("echec d'attribution.");
                    response.sendRedirect("/");//todo: envoyer a la page d'erreur
                }
            }
            //-----------------------------------------------------------------------------------
            //recupererCommandeParID
            if(action.equals("recupererCommande")){
                System.out.println("Appel du service recupererCommande");
                long livreurID = Long.parseLong(request.getParameter("livreurID"));
                Commande commande = null;
                Restaurant restaurant = null;
                try {
                    commande = metier.recupererCommande(livreurID);
                    restaurant = metier.recupererRestaurantParIdProduit(
                            ((ProduitsCommandes) commande.getListeProduits().get(0)).getProduit().getId().longValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.setContentType("application/json");
                response.setCharacterEncoding("ISO-8859-15");
                PrintWriter pw = response.getWriter();
                printCommande(pw, commande,restaurant);
                pw.close();
                System.out.println("json print over.");
                return;
            }
            //-----------------------------------------------------------------------------------
            //cloturerLivraison
            if(action.equals("cloturerCommande")){
                System.out.println("appel du service cloturerCommande");
                Commande commandeACloturer = null;
                try {
                    commandeACloturer = metier.recupererCommande(Long.parseLong(request.getParameter("livreurID")));
                    metier.cloturerLivraison(commandeACloturer.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect("/dashboard?action=authentifierLivreur");
                return;
            }
            //-----------------------------------------------------------------------------------
            //deconnexion
            //-----------------------------------------------------------------------------------
            if(action.equals("deconnexion")){
                System.out.println("appel du service deconnexion");
                session.invalidate();
                response.sendRedirect("/");
                return;
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
        System.out.println("request method : GET");
        processRequest(request,response);
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
            jsonRestaurant.addProperty("latitude",r.getLatitude());
            jsonRestaurant.addProperty("longitude",r.getLongitude());
            jsonliste.add(jsonRestaurant);
        }
        JsonObject container = new JsonObject();
        container.add("restaurants",jsonliste);
        pw.println(gson.toJson(container));
    }

    private void printCommande(PrintWriter pw,Commande commande,Restaurant restaurant)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonCommande = new JsonObject();
        JsonArray jsonListeProduits = new JsonArray();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy à HH:mm");
        jsonCommande.addProperty("date",df.format(commande.getDateCommmande()));
        jsonCommande.addProperty("livreur",commande.getLivreur().getMail()+" (no "+commande.getLivreur().getId()+")");
        jsonCommande.addProperty("livreurLat",commande.getLivreur().getLatitude());
        jsonCommande.addProperty("livreurLng",commande.getLivreur().getLongitude());
        jsonCommande.addProperty("restaurant",restaurant.getDenomination());
        jsonCommande.addProperty("restaurantLat",restaurant.getLatitude());
        jsonCommande.addProperty("restaurantLng",restaurant.getLongitude());
        jsonCommande.addProperty("client",commande.getClient().getNom()+" "+commande.getClient().getPrenom());
        jsonCommande.addProperty("clientAdr",commande.getClient().getAdresse());
        jsonCommande.addProperty("clientLat",commande.getClient().getLatitude());
        jsonCommande.addProperty("clientLng",commande.getClient().getLongitude());
        jsonCommande.addProperty("total",commande.getPrixTotal());
        for(ProduitsCommandes i : commande.getListeProduits()){
            JsonPrimitive string = new JsonPrimitive(i.toString());
            jsonListeProduits.add(string);
        }
        jsonCommande.add("produitsCommandes",jsonListeProduits);
        pw.println(gson.toJson(jsonCommande));
    }
}