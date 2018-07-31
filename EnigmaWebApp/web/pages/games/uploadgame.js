$(function() { // onload...do
    $("#uploadForm").submit(function() {
        var file1 = this[0].files[0];
        var formData = new FormData();
        formData.append("fake-key-1", file1);

        $.ajax({
            method:'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function(xhr) {
                var html = $.parseHTML(xhr.responseText)
                console.error("Failed to submit");
                if (html)
                    $("#result").text(html[5].innerText);
            },
            success: function(data) {
                console.log("data was successfully uploaded");
                $("#result").empty();
                var newDoc = document.open("text/html", "replace");
                newDoc.write(data);
                //$("html").html(data);
            }
        });
        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
})