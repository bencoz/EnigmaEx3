package server.servlets;

import Enigma.EnigmaManager;
import GameManager.GameManager;
import com.sun.net.httpserver.HttpServer;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoadGameSettingsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("games/loadgamesetting.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameFromSession = SessionUtils.getBattleName(request);
        EnigmaManager enigmaManager = ServletUtils.getEnigmaManager(getServletContext());
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        List<Integer> chosenRotorsID = null;
        List<Character> chosenRotorsLoc = null;
        Integer chosenReflectorID = null;

        gameManager.loadGameSettings(battleNameFromSession, chosenRotorsID, chosenRotorsLoc, chosenReflectorID);
    }
}
