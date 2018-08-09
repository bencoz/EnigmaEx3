<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Enigma Competition</title>
    <link rel="stylesheet" href="game.css">
    <link rel="stylesheet" href="winningmodal.css">
    <script src="../../common/jquery-2.0.3.min.js"></script>
    <script src="gamepage.js"></script>
    <script src="wining.js"></script>
</head>
<body>
<div class="game-container">
    <div class="header-container">
        <h1>Battle of ${battlefield}</h1>
    </div>
    <div class="uboat-game" style="display: ${uboatdisplay};">
        <div class="uboat-settings-container">
            <h2>Machine Configuration</h2>
            <iframe width="0" height="0" border="0" name="dummyframe" id="dummyframe"></iframe>
            <form class="uboat-settings" id="config" method="post" onsubmit="postMachineConfig()" target="dummyframe">
                <fieldset id="uboatfieldset">
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
            <form id='aliesConfig' method="post" class="alies-setting" onsubmit="postAliesSettings()" target="dummyframe">
                <p>Port Number : ${portNum}</p>
                <p id="target">Target: </p>
                <fieldset id="aliesfieldset">
                    <label title="text">Task Size:</label>
                    <input name="tasksize" type="text" value="10" />
                </fieldset>
            </form>
            <button class="alies-ready-btn" type="submit" form="aliesConfig">
                Ready
            </button>
            <div class="agent-list-container">
                <h2>Alies Agents:</h2>
                <ul class='agent-list'>
                </ul>
            </div>
            <div class="agent-info-container">
            </div>
        </div>
    </div>
    <div class="msg-container">
        <h1>Candidates:</h1>
        <ol class="msg-list">
        </ol>
    </div>
    <div class="alies-info-container">
        <h2>Participating Alies</h2>
        <ul class='alies-list'>
        </ul>
    </div>
</div>
<div class="modal-container">
    <!-- The Modal -->
    <div id="myModal" class="modal">
        <!-- Modal content -->
        <div class="modal-content">
            <div class="modal-header">
                <span class="close" onclick="AliesClickXbutton()">&times;</span>
                <h3>Wining Alies: </h3>
            </div>
            <div class="modal-body"></div>
            <div class="modal-footer">
                <button id="btncontinue" onclick="UboatClickResetGame()">Continue</button>
                <button id="btnlogout" type='button' onclick="UboatClickLogout()">Logout</button>
            </div>

        </div>
    </div>
</div>
</body>
</html>
