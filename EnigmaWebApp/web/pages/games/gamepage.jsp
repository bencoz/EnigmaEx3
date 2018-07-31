<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Enigma Competition</title>
    <link rel="stylesheet" href="game.css">
    <script src="../../common/jquery-2.0.3.min.js"></script>
    <script src="gamepage.js"></script>
</head>
<body>
<div class="game-container">
    <div class="header-container">
        <h1>${battlefield}</h1>
    </div>
    <div class="logout-container">
        <button class="logout-btn">
            Logout
        </button>
    </div>
    <div class="uboat-game" style="display: ${uboatdisplay};">
        <div class="uboat-settings-container">
            <h2>Machine Configuration</h2>
            <form class="uboat-settings" id="config" method="GET" action="./load">
                <fieldset id="fieldset">
                    <table>
                        <tbody>
                        <tr>
                            <th>
                                <label title="Rotor Setting">Rotors:</label>
                            </th>
                            <td class="settings" id="rotors-container" form="config">
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
                                <select id="reflector-container" form="config" name="reflector">
                                    ${reflectors}
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <label title="text">Message:</label>
                            </th>
                            <td >
                                <input type="text" id="source-msg-container" form="config" placeholder="Enter Message Here" name="message"></input>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </fieldset>
            </form>
            <button class="uboat-ready-btn" type="submit" form="config">
                Ready
            </button>
            <div class="target-msg-container">
                Out:
                <output class="target-msg-value">

                </output>
            </div>
            <div class="uboat-error-container">
            </div>
        </div>
    </div>
    <div class="alies-game" style="display: ${aliesdisplay};">
        <div class="alis-setting-container">
            <h2>Alies Configuration</h2>
            <form class="alies-setting">
                Task Size:
                <input name="tasksize" type="text" value="10" />
                <button class="alies-ready-btn">
                    Ready
                </button>
            </form>
            <div class="target-msg-container">

            </div>
            <div class="agent-info-container">
            </div>
        </div>
    </div>
    <div class="msg-container">
        <h1>Candidates:</h1>
        <ol class="msg-list">
        <li>"MessageMessageMessageMessage" By:"Alies1"</li>
        <li>"MessageMessageMessageMessage" By:"Alies1"</li>
        <li>"MessageMessageMessageMessage" By:"Alies2"</li>
        <li>"MessageMessageMessageMessage" By:"Alies3"</li>
        <li>"MessageMessageMessageMessage" By:"Alies3"</li>
        </ol>
    </div>
    <div class="alies-info-container">
        <h2>Participating Alies</h2>
        <ul class='alies-list'>
        </ul>
    </div>
</div>
</body>
</html>
