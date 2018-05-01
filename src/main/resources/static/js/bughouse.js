//some kind of conversion method to refer to players??


//need to be able to click piece in bank and place it on board

//need to not be able to make a move unless it is the player's turn -> boolean

function initializeBank() {
	for (var r = 0; r < BOARD_DIM; r++) {
		var col = "";
		for (var c = 0; c < BOARD_DIM; c++) {
			var id = r.toString() + "-" + c.toString();
			col += "<td id='" + id + "''></td>";
		}
		$("#bank").append("<tr>" + col + "</tr>");
	}
}