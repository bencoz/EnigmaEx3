package server.servlets;

import Enigma.EnigmaManager;
import GameManager.*;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String battleNameFromSession = SessionUtils.getGameName(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        EnigmaManager enigmaManager = gameManager.getBattlefieldEnigmaManager(battleNameFromSession);
        List<Integer> chosenRotorsID = new LinkedList<>();
        List<Character> chosenRotorsLoc = new LinkedList<>();
        Integer chosenReflectorID = Integer.parseInt(request.getParameter("reflector"));
        String message = request.getParameter("message");
        message = message.toUpperCase();
        boolean buildIsOk = true;
        String error = "";
        for (int i =0; i < enigmaManager.getMachine().getRotorsCount(); i++){
            Integer id = Integer.parseInt(request.getParameter("rotor"+(i+1)));
            if (chosenRotorsID.contains(id)){
                buildIsOk = false;
                error = "Use a rotor only one time";
                break;
            } else {
                chosenRotorsID.add(id);
            }
        }
        if (buildIsOk) {
            String ABC = enigmaManager.getMachine().getABC();
            for (int i = 0; i < enigmaManager.getMachine().getRotorsCount(); i++) {
                Character loc = request.getParameter("rotor" + (i + 1) + "_loc").toUpperCase().charAt(0);
                if (ABC.indexOf(loc) != -1)
                    chosenRotorsLoc.add(loc);
                else {
                    buildIsOk = false;
                    error = "Character \'" + loc + "\' is not in machine's abc";
                    break;
                }
            }
        }
        if (buildIsOk){
            if (!enigmaManager.isInDictionary(message)){
                buildIsOk = false;
                error = "messgae contains words not in machine's dictionary";
            }
        }
        if (!buildIsOk){
            response.sendError(403, error);
            return;
        }
        gameManager.loadGameSettings(battleNameFromSession, chosenRotorsID, chosenRotorsLoc, chosenReflectorID);
        String encryptedCode = gameManager.setGameCode(usernameFromSession, message);
        gameManager.setUboatReady(battleNameFromSession, usernameFromSession);
        Game game = gameManager.getGame(battleNameFromSession);
        if (game.isRunnable()) {
            game.giveAllAliesSecret();
            Thread gameThread = new Thread(game);
            gameThread.setName(battleNameFromSession);
            gameThread.start();
        }
        response.getWriter().write(encryptedCode);
        response.setStatus(200);
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

}
