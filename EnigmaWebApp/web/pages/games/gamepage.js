var uboatMsgVersion = 0;
var aliesMsgVersion = 0;
var refreshRate = 2000; //mili seconds
var UBOAT_CANDIDATES_LIST_URL = buildUrlWithContextPath("uboatcandidateslist");
var ALIES_CANDIDATES_LIST_URL = buildUrlWithContextPath("aliescandidateslist");
var ALIES_LIST_URL = buildUrlWithContextPath("alieslist");
var AGENT_LIST_URL = buildUrlWithContextPath("agentlist");
var aliesIntervalId;

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
    var uboatDisplay = $('#uboat-game').css('display');
    if (uboatDisplay == 'none'){
        ajaxAliesCandidates();
    } else {
        ajaxUboatList()
    }
}

function ajaxAliesCandidates() {
    $.ajax({
        url: ALIES_CANDIDATES_LIST_URL, //TODO:: change to agentcandidatesservlet
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
        url: UBOAT_CANDIDATES_LIST_URL,
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
    setTimeout(ajaxMsgContent, refreshRate);
}

function disableMachineConfig() {
    $('#uboatfieldset').attr('disabled', 'disabled');
}
function disableAliesConfig() {
    $('#aliesfieldset').attr('disabled', 'disabled');
}
function addOutputText(text) {
    $('.target-msg-value').text(text);
}
function OutputRed(cond) {
    if (cond) {
        $('.target-msg-value').css('color', 'red');
    } else {
        $('.target-msg-value').css('color', 'black');
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
                $("#target").innerText += data; //TODO:: Send data to alies in game
                //TODO:: Notify All game users that user is ready.
            }
        });
        return false;
}

function postAliesSettings() {
    console.log("posting alies settings");
    $.ajax({
        method:'POST',
        url: './ready',
        data : $('#aliesConfig').serialize(),
        error: function(xhr) {
            console.error("Failed to submit");
        },
        success: function(data) {
            console.log("success");
            disableAliesConfig();
            //TODO:: Notify All game users that user is ready.
        }
    });
    return false;
}


//TODO:: connect to X button
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

//TODO:: connect to logout button
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

//TODO:: connect to Reset button
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
        var nameDiv = $('<div>').text("Alies Name: "+ alies.aliesName);
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
        var codesChecked = $('<div>').text("Codes Checked: "+ agent.codeschecked);
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
        url: ALIES_LIST_URL,
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
        url: AGENT_LIST_URL,
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
//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    aliesIntervalId = setInterval(ajaxAliesList, refreshRate);

    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    //triggerAjaxMsgContent();
});