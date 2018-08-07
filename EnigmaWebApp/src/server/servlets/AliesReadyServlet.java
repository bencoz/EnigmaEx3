package server.servlets;

import GameManager.*;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AliesReadyServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameFromSession = SessionUtils.getGameName(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        Integer taskSize = Integer.parseInt(request.getParameter("tasksize"));
        gameManager.setAliesTaskSize(usernameFromSession, taskSize);
        gameManager.setAliesReady(battleNameFromSession,usernameFromSession);
        Game game = gameManager.getGame(battleNameFromSession);
        game.checkRunnableAndRun();
        response.setStatus(200);
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

}