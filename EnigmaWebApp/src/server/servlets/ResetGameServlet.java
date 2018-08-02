package server.servlets;

import GameManager.GameManager;
import server.constants.Constants;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResetGameServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("pages/games/resetgame.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameRequested = request.getParameter("battleName");
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        gameManager.resetGame(usernameFromSession, battleNameRequested);
    }
}
