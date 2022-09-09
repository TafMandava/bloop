$(document).ready(function() {
    $("#buttonCancel").on("click", function() {
        window.location = moduleUrl;
    });
    
    /**
     * Handle the click event of the photo upload button
     */
    $("#imageFile").change(function() {
        fileSize = this.files[0].size;

        /**
         * 1024 * 1024 = 1048576 (1MB) 
         */
        if(fileSize > 1048576) {
            this.setCustomValidity("Please choose an image less than 1 MB");
            /**
             * The browser will prevent the form from being submitted to the server displaying the error message
             */
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }

    });
});

/**
 * Display Image thumbnail 
 */
function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    /**
     * Specify the onload event for the reader function 
     */
    reader.onload = function(e) {
        /**
         * Select the thumbnail image and set the attribute of the src element to e.target.result 
         */
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}
