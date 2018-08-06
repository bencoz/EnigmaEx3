package server.servlets;

import Alies.Alies;
import GameManager.*;
import Users.UserManager;
import com.google.gson.Gson;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AliesListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
            Game game = gameManager.getGame(SessionUtils.getGameName(request));
            if (game == null){
                response.sendError(403,"game has not been created yet...");
                return;
            }
            List<aliesInfo> aliesInfoList = generateAliesInfo(game.getAlies());
            String json = gson.toJson(aliesInfoList);
            out.println(json);
            out.flush();
        }
    }
    public List<aliesInfo> generateAliesInfo(List<Alies> aliesList){
        List<aliesInfo> result = new LinkedList<>();
        for (Alies alies : aliesList){
            result.add(new aliesInfo(alies));
        }
        return result;
    }

    public class aliesInfo implements Serializable {
        private String name;
        private Integer numOfAgents;

        public aliesInfo(Alies alies){
            name = alies.getAliesName();
            numOfAgents = alies.getNumOfAgents();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    }// </editor-fold>
}
