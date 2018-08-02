function showLogo() {
    $(".logo").css("opacity","100");
    $(".logo").css("display","inline-block");
    $(".logo").fadeIn(2500);
}
function hideIntro() {
    $(".intro").fadeOut(3500,showLogo);
}

$(document).ready(function() {
    $(".logo").css("display","none");
    window.setTimeout(function animation() {
            console.log("animation!!!")
            hideIntro();
        }, 400); //call fade in 3 seconds
    }
)