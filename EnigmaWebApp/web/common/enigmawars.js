var audio;

function showLogo() {
    $(".logo").fadeIn(3500);
    $('#loginform').show();
    $('button').show();
    //$(".logo").animate({height: '350px', width: '650px'});
    setTimeout(stopMusic, 9000);
}

function hideIntro() {
    $(".intro").fadeOut(6500,showLogo);
}

function startMovie() {
    console.log("starting movie...");
    $('body > :not(.logo)').show();
    $('#click').hide();
    $('.logo').hide();
    $('#loginform').hide();
    $('button').hide();
    $('.intro').show();
    audio = new Audio('./common/images/Star_Wars_original_crawl.mp3');
    audio.currentTime = 0;
    audio.play();
    setTimeout(hideIntro, 2000);
}

function stopMusic() {
    audio.pause();
    audio.currentTime = 0;
    console.log("movie ended...");
}

$(function() {
    $('body > :not(#click)').hide();  //hide all nodes directly under the body
    $('#click').appendTo('body');  // move #myDiv up to the body
})