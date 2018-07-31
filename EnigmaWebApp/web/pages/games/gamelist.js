

var GAME_LIST_URL = buildUrlWithContextPath("../games/gameslist");
var JOIN_GAME_URL = buildUrlWithContextPath("gamepage");
var refreshRate = 2000; //mili seconds

function refreshGamesList(games) {
    //clear all current users
    $("#gameslist").empty();

    // rebuild the list of games: scan all users and add them to the list
    $.each(games || [], function(index, game) {
        //console.log("Adding user #" + index + ": " + game.getName());

        //game will be described by jason:
        //{battlefieldName : name, managerName: managerName, isActive:is/isn't(true/false), difficulty:difficulty,
        //actualNumOfUsers:actualNumOfUsers , neededNumOfUsers:neededNumOfUsers}
        $('<button>',{ type : "button", onclick : "clickOnGame()"})
            .append('<div>',{class: "gameObj",id: game.battlefieldName}
                .append($('<ul>')
                    .append('<li>' + "Battlefield Name: "+ game.battlefieldName+'</li>')
                    .append('<li>' + "Manager Name: "+ game.managerName+'</li>')
                    .append('<li>' + "Game" + game.isActive + "active" +'</li>')
                    .append('<li>' + "Difficulty Level" + game.difficulty  +'</li>')
                    .append('<li>' + game.actualNumOfUser + "/" + NeededNumOfUser +'</li>'))
                .appendTo($("#gameslist")));
    });
}

function ajaxGamesList() {
    $.ajax({
        url: "./gamelist",
        success: function(games) {
            refreshGamesList(games);
        }
    });


}

function clickOnGame()
{
    var t = this;
    $.ajax({
    method:'POST',
    url: "/pages/joingame",

    data: {battleName:name},
    timeout: 4000,
    error: function(xhr) {
        var html = $.parseHTML(xhr.responseText)
        console.error("Failed to submit");
        if (html)
            $("#result").text(html[5].innerText);
    },
    success: function(data) {
        console.log("joined game");
        $("#result").empty();
        var newDoc = document.open("text/html", "replace");
        newDoc.write(data);
        //$("html").html(data);
    }
});
    // return value of the submit operation
    // by default - we'll always return false so it doesn't redirect the user.
    return false;

}

$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The games list is refreshed automatically every second
    setInterval(ajaxGamesList, refreshRate);

    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    //triggerAjaxChatContent();
});