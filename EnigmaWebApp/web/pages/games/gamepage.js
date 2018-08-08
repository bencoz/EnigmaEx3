var uboatMsgVersion = 0;
var aliesMsgVersion = 0;
var refreshRate = 2000; //mili seconds
var UBOAT_CANDIDATES_LIST_URL = buildUrlWithContextPath("uboatcandidateslist");
var ALIES_CANDIDATES_LIST_URL = buildUrlWithContextPath("aliescandidateslist");
var ALIES_LIST_URL = buildUrlWithContextPath("alieslist");
var AGENT_LIST_URL = buildUrlWithContextPath("agentlist");
var aliesIntervalId;
var g_readyUsers = [];
var g_started = false;
var readyUserIntervalId;
var gameStatusIntervaId;
var candidatesTimeoutId;

function appendToMsgList(entries) {
    $.each(entries || [], appendMsgEntry);

    // handle the scroller to auto scroll to the end of the chat area
    var scroller = $(".msg-list");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

function appendMsgEntry(index, entry){
    var entryElement = createMsgEntry(entry);
    $(".msg-list").append(entryElement).append("<br>");
}

function createMsgEntry (entry){
    return $("<span class=\"success\">").append(entry.name + "> " + entry.decoding);
}

function ajaxMsgContent() {
    var uboatDisplay = $('.uboat-game').css('display');
    if (uboatDisplay == 'none'){
        ajaxAliesCandidates();
    } else {
        ajaxUboatList()
    }
}

function ajaxAliesCandidates() {
    $.ajax({
        url: './aliescandidateslist', //ALIES_CANDIDATES_LIST_URL,
        data: "aliesMsgVersion=" + aliesMsgVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data is of the next form:
             {
                "entries": [
                    {
                        "decoding":"Hi",
                        "name":"bbb",
                    }, ...
                ],
                "version":1
             }
             */
            console.log("Server msg version: " + data.version + ", Current msg version: " + aliesMsgVersion);
            if (data.version !== aliesMsgVersion) {
                aliesMsgVersion = data.version;
                appendToMsgList(data.entries);
            }
            triggerAjaxMsgContent();
        },
        error: function(error) {
            triggerAjaxMsgContent();
        }
    });
}

function ajaxUboatList() {
    $.ajax({
        url: './uboatcandidateslist',//UBOAT_CANDIDATES_LIST_URL,
        data: "uboatMsgVersion=" + uboatMsgVersion,
        dataType: 'json',
        success: function(data) {
            console.log("Server msg version: " + data.version + ", Current msg version: " + uboatMsgVersion);
            if (data.version !== uboatMsgVersion) {
                uboatMsgVersion = data.version;
                appendToMsgList(data.entries);
            }
            triggerAjaxMsgContent();
        },
        error: function(error) {
            triggerAjaxMsgContent();
        }
    });
}

function triggerAjaxMsgContent() {
    candidatesTimeoutId = setTimeout(ajaxMsgContent, refreshRate);
}
function enableMachineConfig() {
    $('#uboatfieldset').attr('disabled', '');
    $('.uboat-ready-btn').attr("disabled", false);
}
function disableMachineConfig() {
    $('#uboatfieldset').attr('disabled', 'disabled');
    $('.uboat-ready-btn').attr("disabled", true);
}
function disableAliesConfig() {
    $('#aliesfieldset').attr('disabled', 'disabled');
    $('.uboat-ready-btn').attr("disabled", true);
}
function addOutputText(text) {
    $('.target-msg-value').text(text);
    $('.target-msg-value').css("color", "rgb(75, 213, 238)");
}
function addAliesTarget(text) {
    $('#target').text("Target: "+text);
}
function OutputRed(cond) {
    if (cond) {
        $('.target-msg-value').css('color', 'red');
    } else {
        $('.target-msg-value').css('color', 'rgb(238, 75, 75)');
    }
}

function postMachineConfig() {
        console.log("posting uboat machine config");
        $.ajax({
            method:'POST',
            url: './load',
            data : $('#config').serialize(),
            error: function(xhr) {
                console.log("error");
                var html = $.parseHTML(xhr.responseText)
                console.error("Failed to submit");
                if (html){
                    addOutputText(html[5].innerText.replace("Message", ''));
                    OutputRed(true);
                }
            },
            success: function(data) {
                console.log("success")
                disableMachineConfig();
                addOutputText(data);
                OutputRed(false);
                $("#target").text = "Out: "+ data;
                //$("#target").css("color", "rgb(238, 75, 75)");
            }
        });
        return false;
}

function postAliesSettings() {
    console.log("posting alies settings");
    $.ajax({
        method:'POST',
        url: './aliesready',
        data : $('#aliesConfig').serialize(),
        error: function(xhr) {
            console.error("Failed to submit");
        },
        success: function(data) {
            console.log("success");
            disableAliesConfig();
        }
    });
    return false;
}


function AliesClickXbutton() { //(alies)
    console.log("alies leave game");
    $.ajax({
        method:'POST',
        url: './leavegame',
        error: function(xhr) {
            console.error("Failed to submit");
        },
        success: function(data) {
            console.log("success");
        }
    });
    return false;
}

function UboatClickLogout() { //(uboat)
    console.log("uboat logout");
    $.ajax({
        method:'POST',
        url: './logout',
        error: function(xhr) {
            console.error("Failed to submit");
        },
        success: function(data) {
            console.log("success");
        }
    });
    return false;
}

function UboatClickResetGame() { //(uboat)
    console.log("uboat reset game");
    $.ajax({
        method:'POST',
        url: './resetgame',
        error: function(xhr) {
            console.error("Failed to submit");
        },
        success: function(data) {
            console.log("success");
            appendToMsgList();
            $('#myModal').css('display','none');
            enableMachineConfig();
        }
    });
    return false;
}

function refreshParticipatingAlies(alies_list) {
    //clear all current users
    $(".alies-list").empty();

    // rebuild the list of alies: scan all alies and add them to the list
    $.each(alies_list || [], function(index, alies) {

        var id = alies.aliesName;
        var nameDiv = $('<div>').text("Alies Name: "+ alies.name);
        var numOfAgentDiv = $('<div>').text("Number Of Agent: "+ alies.numOfAgents);

        var aliesDiv = $('<div>', {
            class: "AliesObj",
            id: id
        });
        aliesDiv.append(nameDiv);
        aliesDiv.append(numOfAgentDiv);

        aliesDiv.appendTo($(".alies-list"));
    });
}

function refreshAgentList(agentList) {
    $(".agent-list").empty();

    // rebuild the list of alies: scan all alies and add them to the list
    $.each(agentList || [], function(index, agent) {

        var nameDiv = $('<div>').text("Agent Name: "+ agent.name);
        var codesChecked = $('<div>').text("Candidates: "+ agent.candidates);
        var codesLeft = $('<div>').text("Codes Left: "+ agent.codesleft);
        var aliesDiv = $('<div>', {class: "AgentObj"});
        aliesDiv.append(nameDiv);
        aliesDiv.append(codesChecked);
        aliesDiv.append(codesLeft);

        aliesDiv.appendTo($(".agent-list"));
    });

    setTimeout(ajaxAgentList, 2000);
}

function ajaxAliesList() {
    //TODO :: STOP when alies number.
    $.ajax({
        url: './alieslist',
        success: function (alies_list) {
            refreshParticipatingAlies(alies_list);
        },
        error: function (xhr) {
            var html = $.parseHTML(xhr.responseText)
            console.error(html[5].innerText.replace("Message",""));
        }
    });
}

function ajaxAgentList() {
    $.ajax({
        url: './agentDetails',
        success: function (agent_list) {
            refreshAgentList(agent_list);
        },
        error: function (xhr) {
            var html = $.parseHTML(xhr.responseText)
            console.error(html[5].innerText.replace("Message",""));
            setTimeout(ajaxAgentList, 2000)
        }
    });
}
function arrayInclude(arr, name) {
    var result = false;
    for (var i = 0; i< arr.length; i++){
        if (arr[i] == name)
            result = true;
    }
    return result;
}
function setReadyUsers(readyUsers) {
    var namesArray = readyUsers.names;
    var needNum = readyUsers.numOfUsers;
    for (var i = 0; i < namesArray.length; i++) {
        if (!arrayInclude(g_readyUsers,namesArray[i])){
            g_readyUsers.push(namesArray[i]);
            alert(namesArray[i] + " is ready !");
        }
    }
    if (g_readyUsers.length == needNum){
        clearInterval(readyUserIntervalId);
        alert("All users are ready - Starting Game !");
    }
}
function ajaxGameStatus() {
    $.ajax({
        url: './status',
        success: function (res) {
            console.log("game status is: "+res);
            if (res == 'DONE'){
                $('#myModal').css('display','block')
                clearInterval(gameStatusIntervaId);
                clearInterval(aliesIntervalId);
                clearInterval(readyUserIntervalId);
                clearTimeout(candidatesTimeoutId);
                //TODO :: Clear Msg-conent timeout and agent info timeout !
            } else if (res == 'RUNNING' && !g_started){
                g_started = true;
                triggerAjaxMsgContent();
            }
        }
    });
}
function ajaxReady() {
    $.ajax({
        url: './ready',
        success: function (readyUsers) {
            setReadyUsers(readyUsers);
            if (readyUsers.target != null){
                addAliesTarget(readyUsers.target);
            }
        },
    });
}

function initEndingPopup() {
    var uboatDisplay = $('.uboat-game').css('display');
    if (uboatDisplay == 'none'){
        $('.modal-footer').css("display", "none");
    } else {
        $('.close').css("display", "none");
    }
}
//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    aliesIntervalId = setInterval(ajaxAliesList, refreshRate);
    gameStatusIntervaId = setInterval(ajaxGameStatus, refreshRate);
    readyUserIntervalId = setInterval(ajaxReady, refreshRate);
    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)

    initEndingPopup()
});