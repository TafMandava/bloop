var dropdownCountry;
var listedStates;
var stateField;

$(document).ready(function() {
    dropdownCountry = $("#country");
    listedStates = $("#states");
    stateField = $("#state");

    dropdownCountry.on("change", function() {
        loadStatesAggregatedByCountry();

        /**
         * After the country is selected we need to clear the text in the text view 
         */
        stateField.val("").focus();
    });

});

function loadStatesAggregatedByCountry() {
    var selectedCountry = $("#country option:selected"); 
    var countryId = selectedCountry.val();

    url = contextPath + "states/list-by-country/" + countryId;

    /**
     * Perform an AJAX call to the given url via HTTP GET method 
     */
    $.get(url, function(jsonResponse) {
        /**
         * When we get the response empty the list of states 
         */
         listedStates.empty();

        /**
         * When we receive the reponse from the server, iterate through each state record returned and add each element to the listedStates
         */
        $.each(jsonResponse, function(index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(listedStates)
        });

    });
}

/**
 * Validate the two password fields 
 */
function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value != $("#password").val()) {
        confirmPassword.setCustomValidity("Passwords do not match");
    } else {
        confirmPassword.setCustomValidity("");
    }
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showErrorDialog(message) {
    showModalDialog("Error", message);
}

function showWarningDialog(message) {
    showModalDialog("Warning", message);
}