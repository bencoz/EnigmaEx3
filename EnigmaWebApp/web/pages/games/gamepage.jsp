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
    <div class="logout-container">
        <button class="logout-btn">
            Logout
        </button>
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
            <h3>Port Number : ${portNum}</h3>
            <h3 id="target">Target: </h3>
            <form id='aliesConfig' method="post" class="alies-setting" onsubmit="postAliesSettings()" target="dummyframe">
                <fieldset id="aliesfieldset">
                    <label title="text">Task Size:</label>
                    <input name="tasksize" type="text" value="10" />
                    <button class="alies-ready-btn" type="submit" form="aliesConfig">
                        Ready
                    </button>
                </fieldset>
            </form>
            <div class="agent-list-container">
                <h2>Alies Agents:</h2>
                <ul class='agent-list'>
                </ul>
            </div>
            <div class="target-msg-container">

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
                <span class="close">&times;</span>
                <h3>Wining Alies: </h3>
            </div>
            <div class="modal-body">
                <p>Agent Name: </p>
            </div>
            <div class="modal-footer">
                <button id="btncontinue">Continue</button>
                <button id="btnlogout">Logout</button>
            </div>

    </div>
</div>
</body>
</html>
