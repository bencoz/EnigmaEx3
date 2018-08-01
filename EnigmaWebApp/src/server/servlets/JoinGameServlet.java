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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("games/joingame.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameRequested = request.getParameter("battleName");
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        gameManager.addAliesToGame(usernameFromSession, battleNameRequested);
        int port = gameManager.getAliesPort(usernameFromSession);
        request.setAttribute("portNum", port);
        request.setAttribute("battlefield", battleNameRequested);
        request.setAttribute("uboatdisplay", "none");
        request.setAttribute("aliesdisplay","inline-flex");
        request.setAttribute("rotors", null);
        request.setAttribute("rotors", null); // This will be available as ${rotors}
        request.setAttribute("rotorsLocation", null); // This will be available as ${rotorsLocation}
        request.setAttribute("reflectors", null); // This will be available as ${reflectors}
        request.getRequestDispatcher("gamepage.jsp").forward(request, response);
    }
}
