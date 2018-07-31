package server.servlets;

import Enigma.EnigmaManager;
import GameManager.GameManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

public class JoinGameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("games/joingame.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameFromSession = SessionUtils.getGameName(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        gameManager.addAliesToGame(usernameFromSession, battleNameFromSession);
    }
}
