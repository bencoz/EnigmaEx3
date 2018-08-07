package server.servlets;

import GameManager.GameManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LeaveGameServlet extends HttpServlet {

    private final String GAMES_LIST_URL = "../gamelist.html";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("pages/games/leavegame.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameRequested = SessionUtils.getGameName(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        gameManager.removeAliesFromGame(usernameFromSession, battleNameRequested);
        response.sendRedirect(GAMES_LIST_URL);
    }
}
