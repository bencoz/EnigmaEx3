<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 20/07/2018
  Time: 19:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Set Machine</title>
    <style>
        input {margin:2px;  width: 50px;}
    </style>
</head></head>
<body>
<form id="enigma" name="enigma" method="GET" action="enigma.html">
    <table id="config">
        <tbody>
        <tr>
            <th>
                <label title="Rotor Setting">Rotors:</label>
            </th>
            <td class="settings" id="rotors-container">
                ${rotors}
            </td>
        </tr>
        <tr>
            <th>
                <label title="Rotor Location Setting">Location:</label>
            </th>
            <td class="settings" id="rotors-location-container">
                ${rotorsLocation}
            </td>
        </tr>
        <tr>
            <th>
                <label for="reflector-container" title="Chosen Reflector">Reflector:</label>
            </th>
            <td class="settings">
                <select id="reflector-container">
                    ${reflectors}
                </select>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>
