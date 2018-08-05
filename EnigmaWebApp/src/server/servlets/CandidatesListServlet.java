package server.servlets;

import Alies.AliesResponse;
import Commons.CandidateForDecoding;
import GameManager.*;
import Users.UserManager;
import com.google.gson.Gson;
import server.constants.Constants;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CandidatesListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect("index.html");
        }
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
            int msgVersion = ServletUtils.getIntParameter(request, Constants.UBOAT_MSG_VERSION_PARAMETER);
            if (msgVersion > Constants.INT_PARAMETER_ERROR) {
                Game game = gameManager.getGame(SessionUtils.getGameName(request));
                List<AliesResponse> allCandidates = game.getAllCandidates();
                MsgAndVersion mav = new MsgAndVersion(allCandidates, game.getVersion());
                String json = gson.toJson(mav);
                out.println(json);
                out.flush();
            }
        }
    }

    class MsgAndVersion implements Serializable {
        final private List<AliesResponse> entries;
        final private int version;

        public MsgAndVersion(List<AliesResponse> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "retrun all candidates";
    }// </editor-fold>

}
