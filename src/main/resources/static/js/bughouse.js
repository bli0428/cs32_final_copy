//some kind of conversion method to refer to players??


//need to be able to click piece in bank and place it on board

//need to not be able to make a move unless it is the player's turn -> boolean

function initializeBank(black) {
	var chooseArray = [];
	if (black) {
		chooseArray = ['&#9823','&#9820','&#9822','&#9821','&#9819'];
	} else {
		chooseArray = ['&#9817','&#9814','&#9816','&#9815','&#9813'];
	}
	var col = "";
	for (var c = 0; c < 5; c++) {
		col += "<td id='" + c + "' class='bughousePiece'>" + chooseArray[c] + "<span id='" + c + "' class=' badge badge-pill badge-primary'>0</span></td>";
	}
	$("#bank").append("<tr>" + col + "</tr>");
}


$("#bank").on("click", "td", function(e){
	var currCountId = "p#" + e.target.id + ".badge";
    var pieceCountString = $(currCountId).html(); // gets curr piece count
    var pieceCount = parseInt(pieceCountString);
    console.log(pieceCount);

    var currCount = parseInt($(currCountId).html())
    var newCount = currCount + 1;

    $(currCountId).html(newCount.toString());


    if (myTurn && pieceCount != 0) {

    } else if (pieceCount == 0) {

    }

});
