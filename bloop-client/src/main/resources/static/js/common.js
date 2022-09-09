$(document).ready(function() {

    $("#logoutLink").on("click", function(e) {
        /**
         * Prevent the default behaviour of the hyperlink 
         */
        e.preventDefault();
        /**
         * Submit the form 
         */
        document.logoutForm.submit();
    });

    $(".navbar .dropdown").hover(
        /**
         * Slide down when the mouse hovers over the dropdown link
         */
        function() {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
        },

        /**
         * Slide up when the mouse moves away from the dropdown link
         */        
        function() {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
        }

    );    

});

function customizeDropDownMenu() {

    $(".navbar .dropdown").hover(
        /**
         * Slide down when the mouse is hovered over the dropdown link
         */
        function() {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
        },

        /**
         * Slide up when the mouse moves away from the dropdown link
         */        
        function() {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
        }

    );

    /**
     * Select the a tag in the dropdown and override the click function 
     */
    $(".dropdown > a").click(function() {
        /**
         * Set the location href attribute of the browser to the href attrbute of the hyperlink itself
         */
        location.href = this.href;
    });
}            
