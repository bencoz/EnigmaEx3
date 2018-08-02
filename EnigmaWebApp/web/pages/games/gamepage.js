var msgVersion = 0;
var refreshRate = 2000; //mili seconds
var MSG_LIST_URL = buildUrlWithContextPath("msglist");
var ALIES_LIST_URL = buildUrlWithContextPath("alieslist");
var aliesIntervalId;

function appendToMsgList(entries) {
//    $("#chatarea").children(".success").removeClass("success");

    // add the relevant entries
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
    var date = new Date(entry.time*1000);
    return $("<span class=\"success\">").append(entry.username + "> " + entry.candidate);
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
        url: MSG_LIST_URL, //TODO:: change to agentcandidatesservlet
        data: "msgVersion=" + msgVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data is of the next form:
             {
                "entries": [
                    {
                        "candidate":"Hi",
                        "username":"bbb",
                    },
                    {
                        "candidate":"Hello",
                        "username":"bbb",
                    }
                ],
                "version":1
             }
             */
            console.log("Server msg version: " + data.version + ", Current msg version: " + msgVersion);
            if (data.version !== msgVersion) {
                msgVersion = data.version;
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
        url: MSG_LIST_URL,
        data: "msgVersion=" + msgVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data is of the next form:
             {
                "entries": [
                    {
                        "candidate":"Hi",
                        "username":"bbb",
                    },
                    {
                        "candidate":"Hello",
                        "username":"bbb",
                    }
                ],
                "version":1
             }
             */
            console.log("Server msg version: " + data.version + ", Current msg version: " + msgVersion);
            if (data.version !== msgVersion) {
                msgVersion = data.version;
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

function ajaxAliesList() {
    //TODO :: STOP when alies number.
    $.ajax({
        url: "./alieslist",
        success: function (alies_list) {
            refreshParticipatingAlies(alies_list);
        },
        error: function (xhr) {
            var html = $.parseHTML(xhr.responseText)
            console.error(html[5].innerText.replace("Message",""));
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