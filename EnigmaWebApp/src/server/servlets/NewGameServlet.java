package server.servlets;

import Enigma.EnigmaManager;
import GameManager.*;
import Machine.*;
import Users.UserManager;
import server.constants.Constants;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class NewGameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("games/uploadgame.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        EnigmaManager enigmaManager = gameManager.createNewEnigmaManager();
        Part part = request.getPart("xmlFile");
        boolean isEnigmaBuildOK = enigmaManager.createEnigmaMachineFromXMLInputStream(part.getInputStream());
        boolean isGameNameExists = gameManager.isGameExist(enigmaManager.getBattleName());
        if(!isEnigmaBuildOK) {
            String error = enigmaManager.getErrorInMachineBuilding();
            response.sendError(403, error); //TODO :: CHOOSE ERROR CODE PROPERLY
        }
        if(isGameNameExists){
            response.sendError(403, "name already exists"); //TODO :: CHOOSE ERROR CODE PROPERLY
        }
        gameManager.createGame(usernameFromSession, enigmaManager);
        //response.sendRedirect();//TODO: response....(?)
        String rotorsHTML = generateRotorsHTML(enigmaManager.getMachine().getRotors().size());
        String rotorsLocation = generateRotorsLocationHTML(enigmaManager.getMachine().getRotors().size());
        String reflectors = generateReflectorsHTML(enigmaManager.getMachine().getReflectors().size());

        request.setAttribute("rotors", rotorsHTML); // This will be available as ${rotors}
        request.setAttribute("rotorsLocation", rotorsLocation); // This will be available as ${rotorsLocation}
        request.setAttribute("reflectors", reflectors); // This will be available as ${reflectors}
        request.getRequestDispatcher("setmachine.jsp").forward(request, response);
    }

    private String generateRotorsLocationHTML(Integer size) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append("<input type=\"text\" name=\"rotor");
            res.append((i+1));
            res.append("_loc\" size=\"3\" maxlength=\"1\" value=\"A\" form=\"config\"/>");
        }
        return res.toString();
    }



    private String generateReflectorsHTML(int size) {
        StringBuilder res = new StringBuilder();
        res.append("<option value=\"1\" selected=\"selected\">I</option>");
        for (int i = 2; i < size+1; i++) {
            res.append("<option value=\"");
            res.append(i);
            res.append("\">");
            res.append(ServletUtils.numToString(i));
            res.append("</option>");
        }
        return res.toString();
    }

    private String generateRotorsHTML(int size) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append("<input type=\"number\" form=\"config\" name=\"rotor");
            res.append((i+1));
            res.append("\" size=\"3\" maxlength=\"1\" value=\"");
            res.append(i+1);
            res.append("\" min=\"1\" max=\"");
            res.append(size+1);
            res.append("\"/>");
        }

        return res.toString();
    }
}