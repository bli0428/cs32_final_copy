// Waits for DOM to load before running
$(document).ready(() => {
  $("#searchbar1").keyup(function() {
    if ($("#searchbar1").textLength != 0) {
      const currWord = $("#searchbar1").val();
      const postParameters = {userInput: currWord};
      // Make a POST request to the "/validate" endpoint with the word information
      $.post("/suggestions", postParameters, responseJSON => {

        // Parse the JSON response into a JavaScript object.
        const responseObject = JSON.parse(responseJSON);
        $("#suggestions1").html(responseObject.suggestions);
        const suggestionElements = $("#suggestions1 li");
        suggestionElements.on('click', event => {
          console.log(event);
          $("#searchbar1").val(event.currentTarget.innerHTML);
        });
      });
    };
  });
  $("#searchbar1").click(function() {
    $("#suggestions1").toggle();
  });
  $("#searchbar2").keyup(function() {
    if ($("#searchbar2").textLength != 0) {
      const currWord = $("#searchbar2").val();
      const postParameters = {userInput: currWord};
      // Make a POST request to the "/validate" endpoint with the word information
      $.post("/suggestions", postParameters, responseJSON => {

        // Parse the JSON response into a JavaScript object.
        const responseObject = JSON.parse(responseJSON);
        $("#suggestions2").html(responseObject.suggestions);
        const suggestionElements = $("#suggestions2 li");
        suggestionElements.on('click', event => {
          console.log(event);
          $("#searchbar2").val(event.currentTarget.innerHTML);
        });
      });
    };
  });
  $("#searchbar2").click(function() {
    $("#suggestions2").toggle();
  });
});
