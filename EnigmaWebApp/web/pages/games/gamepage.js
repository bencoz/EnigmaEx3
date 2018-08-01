var msgVersion = 0;
var refreshRate = 2000; //mili seconds
var MSG_LIST_URL = buildUrlWithContextPath("msglist");
var ALIES_LIST_URL = buildUrlWithContextPath("alieslist");

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
    var hours = date.getHours();
    var minutes = "0" + date.getMinutes();
    var seconds = "0" + date.getSeconds();
    var formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
    return $("<span class=\"success\">").append(entry.username + "> " + entry.candidate +"("+formattedTime+")");
}

function ajaxMsgContent() {
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
                        "time":1485548397514
                    },
                    {
                        "candidate":"Hello",
                        "username":"bbb",
                        "time":1485548397514
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
    $('#fieldset').attr('disabled', 'disabled');
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
                console.log(data);
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
        url: './load',
        data : $('#aliesConfig').serialize(),
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
            console.log(data);
            disableMachineConfig();
            addOutputText(data);
            OutputRed(false);
        }
    });
    return false;
}

function refreshParticipatingAlies(alies_list) {
    //clear all current users
    $("#alies-info-container").empty();

    // rebuild the list of alies: scan all alies and add them to the list
    $.each(alies_list || [], function(index, alies) {

        var id = alies.name;
        var nameDiv = $('<div>').text("Alies Name: "+ alies.aliesName);
        var numOfAgentDiv = $('<div>').text("Number Of Agent: "+ alies.numOfAgents);

        var aliesDiv = $('<div>', {
            class: "AliesObj",
            id: id
        });
        aliesDiv.append(nameDiv);
        aliesDiv.append(numOfAgentDiv);

        aliesDiv.appendTo($("#alies-info-container"));
    });
}

function ajaxAliesList() {
    //TODO :: STOP when alies number.
    $.ajax({
        url: "./alieslist",
        success: function (alies_list) {
            refreshParticipatingAlies(alies_list);
        }
    });
}
//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxAliesList, refreshRate);

    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    //triggerAjaxMsgContent();
});