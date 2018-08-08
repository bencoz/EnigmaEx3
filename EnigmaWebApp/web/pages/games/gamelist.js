

var GAME_LIST_URL = buildUrlWithContextPath("../games/gameslist");
var JOIN_GAME_URL = buildUrlWithContextPath("gamepage");
var refreshRate = 2000; //mili seconds
var gameListInvervalId;
function refreshGamesList(games) {
    //clear all current users
    $("#game-list").empty();

    // rebuild the list of games: scan all users and add them to the list
    $.each(games || [], function(index, game) {
        //console.log("Adding user #" + index + ": " + game.getName());

        //game will be described by jason:
        //{battlefieldName : name, managerName: managerName, isActive:is/isn't(true/false), difficulty:difficulty,
        //actualNumOfUsers:actualNumOfUsers , neededNumOfUsers:neededNumOfUsers}
        var id = game.battlefieldName;
        var nameDiv = $('<div>').text("Battlefield Name: "+ game.name);
        var managerDiv = $('<div>').text("Manager Name: "+ game.makerName);
        var difficultyDiv = $('<div>').text("Difficulty Level: " + game.level);
        var signedDiv = $('<div>').text("In game alies:" + game.numOfAliesSigned + "/" + game.neededNumOfAlies);
        var statusDiv = $('<div>').text("Status: " + game.status);

        var btn = $('<button>', {
            type: "button",
            class: "gameObj",
            id: id
            });
        btn.on("click", {name: game.name},clickOnGame);
        btn.append(nameDiv);
        btn.append(managerDiv);
        btn.append(difficultyDiv);
        btn.append(signedDiv);
        btn.append(statusDiv);

        btn.appendTo($("#game-list"));
    });
}

function refreshAliesDetails(aliesDetails) {
    //clear all current users
    $("#alies-details").empty();

    var nameLi = $('<li>' + "Name:" + aliesDetails.aliesName + '</li>');
    var portLi = $('<li>' + "Port:" + aliesDetails.portNumber + '</li>');

    nameLi.appendTo($("#alies-details"));
    portLi.appendTo($("#alies-details"));
}

function ajaxGamesList() {
    $.ajax({
        url: "./gamelist",
        success: function(games) {
            refreshGamesList(games);
        }
    });


}

function ajaxAliesDetails() {
    $.ajax({
        url: "./aliesDetails",
        success: function(aliesDetails) {
            refreshAliesDetails(aliesDetails);
        }
    });
}

function clickOnGame(e)
{
    var data = e.data.name;
    $.ajax({
        method:'POST',
        url: "./joingame",
        data: {battleName:data},
        error: function(xmlhttprequest, textstatus, message) {
            console.log(xmlhttprequest);
            console.log(message);
            console.error("Failed to submit");
        },
        success: function(data) {
            console.log("success");
            clearInterval(gameListInvervalId);
            document.write(data);
            document.close();
        }
});
    // return value of the submit operation
    // by default - we'll always return false so it doesn't redirect the user.
    return false;
}

function AliesLogout() { //(alies)
    console.log("alies logout");
    $.ajax({
        method:'POST',
        url: './logout',
        error: function(xhr) {
            console.error("Failed to submit");
        },
        success: function(data) {
            clearInterval(gameListInvervalId);
            console.log("success");
        }
    });
    return false;
}

$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The games list is refreshed automatically every second
    ajaxAliesDetails();
    gameListInvervalId = setInterval(ajaxGamesList, refreshRate);

});