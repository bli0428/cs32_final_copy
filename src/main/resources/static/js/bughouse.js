//some kind of conversion method to refer to players??


//need to be able to click piece in bank and place it on board

//need to not be able to make a move unless it is the player's turn -> boolean

function initializeBank() {
	if (black) {
		const chooseArray = ['&#9823','&#9820','&#9822','&#9821','&#9819'];
		var col = "";
		for (var c = 0; c < 5; c++) {
			col += "<td id='" + c + "''><p class='piece'>" + chooseArray[c] + "</p><p class='count'>0</p></td>";
		}
		$("#bank").append("<tr>" + col + "</tr>");
	} else {
		const chooseArray = ['&#9817','&#9814','&#9816','&#9815','&#9813'];
		var col = "";
		for (var c = 0; c < 5; c++) {
			col += "<td id='" + c + "''><p class='piece'>" + chooseArray[c] + "</p><p class='count'>0</p></td>";
		}
		$("#bank").append("<tr>" + col + "</tr>");
	}
}