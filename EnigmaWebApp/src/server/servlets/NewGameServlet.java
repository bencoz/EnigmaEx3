package server.servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import Enigma.EnigmaManager;
import GameManager.GameManager;
import Machine.*;
import Users.UserManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class NewGameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("games/newgame.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        EnigmaManager enigmaManager = ServletUtils.getEnigmaManager(getServletContext());
        Part part = request.getPart("xmlFile");
        boolean isEnigmaBuildOK = enigmaManager.createEnigmaMachineFromXMLInputStream(part.getInputStream());
        boolean isGameNameExists = gameManager.isGameExist(enigmaManager.getBattleName());
        if(!isEnigmaBuildOK) {
            String error = enigmaManager.getErrorInMachineBuilding();
            response.sendError(403, error); //TODO :: CHOOSE ERROR CODE PROPERLY
        } else if(isGameNameExists){
            response.sendError(403, "name already exists"); //TODO :: CHOOSE ERROR CODE PROPERLY
        } else {
            gameManager.createGame(usernameFromSession, enigmaManager);
            //TODO: response....(?)
        }
    }

}