package server.servlets;

import GameManager.*;
import Users.UserManager;
import com.google.gson.Gson;
import server.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GameListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
            List<gameInfo> gameInfoList = createGameInfoList(gameManager.getGames());
            String json = gson.toJson(gameInfoList);
            out.println(json);
            out.flush();
        }
    }

    private List<gameInfo> createGameInfoList(Collection<Game> games) {
        List<gameInfo> result = new LinkedList<>();
        for (Game game : games){
            GameStatus status = game.getStatus();
            if (status == GameStatus.UNINITIALIZED || status == GameStatus.WAITING) {
                gameInfo newgame = new gameInfo(game);
                result.add(newgame);
            }
        }
        return result;
    }

    public class gameInfo implements Serializable {
        String name;
        String makerName;
        Factory.DifficultyLevel level;
        Integer numOfAliesSigned;
        Integer neededNumOfAlies;
        GameStatus status;

        gameInfo(Game game){
            name = game.getBattlefieldName();
            makerName = game.getMakerName();
            level = game.getLevel();
            numOfAliesSigned = game.getAlies().size();
            neededNumOfAlies = game.getNeededNumOfAlies();
            status = game.getStatus();
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
