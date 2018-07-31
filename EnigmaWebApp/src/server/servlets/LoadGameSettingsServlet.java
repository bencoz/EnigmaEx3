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
import java.util.LinkedList;
import java.util.List;

public class LoadGameSettingsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameFromSession = SessionUtils.getBattleName(request);
        EnigmaManager enigmaManager = ServletUtils.getEnigmaManager(getServletContext());
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        List<Integer> chosenRotorsID = new LinkedList<>();
        List<Character> chosenRotorsLoc = new LinkedList<>();
        for (int i =0; i < enigmaManager.getMachine().getNumOfRotors(); i++){
            chosenRotorsID.add(Integer.parseInt(request.getParameter("rotor"+(i+1))));
        }
        for (int i =0; i < enigmaManager.getMachine().getNumOfRotors(); i++){
            chosenRotorsLoc.add(request.getParameter("rotor"+(i+1)+"_loc").charAt(0));
        }
        Integer chosenReflectorID = Integer.parseInt(request.getParameter("reflector"));
        String message = request.getParameter("message");

        gameManager.loadGameSettings(battleNameFromSession, enigmaManager, chosenRotorsID, chosenRotorsLoc, chosenReflectorID);
    }
}
