function addGame(type) {
  const postParameters = {gameType:type};
  $.post("/addGame", postParameters, responseJSON => {

    // Parse the JSON response into a JavaScript object.
    const responseObject = JSON.parse(responseJSON);
    $("#menu").html(responseObject.games);
    // const suggestionElements = $("#suggestions1 li");
    // suggestionElements.on('click', event => {
    //   console.log(event);
    //   $("#searchbar1").val(event.currentTarget.innerHTML);
    // });
  });
}
