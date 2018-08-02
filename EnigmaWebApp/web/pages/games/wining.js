// When the user clicks the button, open the modal
$('.logout-btn').on('click', function() {
    $('#myModal').css('display', 'block');
});

// When the user clicks on <span> (x), close the modal
$(".close").on("click", function() {
    $('#myModal').css('display', "none");

});
