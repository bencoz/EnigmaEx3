<%--
    Document   : index
    Created on : Jan 24, 2012, 6:01:31 AM
    Author     : blecherl
    This is the login JSP for the online chat application
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@page import="server.utils.*" %>
    <%@ page import="server.constants.Constants" %>
    <%@ page import="server.utils.SessionUtils" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Chat</title>
        <link rel="stylesheet" href="../../common/bootstrap.min.css"/>
        <link rel="stylesheet" href="../../common/enigmaWars.css">
        <script type="text/javascript" src="../../common/jquery-2.0.3.min.js"></script>
        <script type="text/javascript" src="../../common/enigmawars.js"></script>
    </head>
    <body>
    <div class="sign-up-container">
        <article class="starwars">
            <section class="intro">
            A long time ago, in a galaxy far,<br> far away....
            </section>
            <img class='logo' src="../../common/images/enigmawars.png">
            <% String usernameFromSession = SessionUtils.getUsername(request);%>
            <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>
            <% if (usernameFromSession == null) {%>
                <form id='loginform'method="GET" action="pages/signup/login">
                    User Name: <input type="text" name="username" class=""/>
                    <input type="radio" name="usertype" value="Uboat" checked/> Uboat
                    <input type="radio" name="usertype" value="Alies"/> Alies
                </form>
            <button class="btn btn-info btn-lg active" type="submit" form="loginform" value="submit">Login</button><br/>
                <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>
                <% if (errorMessage != null) {%>
                <span class="bg-danger" style="color:red;"><%=errorMessage%></span>
            <% } %>
            <% } else {%>
            <h1>Welcome back, <%=usernameFromSession%></h1>
            <a href="../chatroom/chatroom.html">Click here to enter the chat room</a>
            <br/>
            <a href="login?logout=true" id="logout">logout</a>
            <% }%>
        </article>
    </div>
    </body>
</html>