package server.servlets;

import GameManager.*;
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
import java.util.List;

public class ReadyServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String battleNameFromSession = SessionUtils.getGameName(request);
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
            Game game = gameManager.getGame(battleNameFromSession);
            List<String> users = game.getAllReadyNames();
            Integer numberOfWantedUsers = game.getWantedNumOfAlies() + 1;
            String target = game.getTarget();
            Gson gson = new Gson();
            readyUsers readyUsers = new readyUsers(users, numberOfWantedUsers);
            if (target != null){
                readyUsers.setTarget(target);
            }
            String json = gson.toJson(readyUsers);
            out.println(json);
            out.flush();
        }
    }

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

    public class readyUsers implements Serializable{
        List<String> names;
        Integer numOfUsers;
        String target = null;
        readyUsers(List<String> i_names, Integer num){
            names = i_names;
            numOfUsers = num;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }
}
