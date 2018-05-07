

var bughouseSelected = false;
var currBughousePiece = "";

function initializeBank(black) {
	let chooseArray = [];
	if (black) {
		chooseArray = ['&#9823','&#9820','&#9822','&#9821','&#9819'];
	} else {
		chooseArray = ['&#9817','&#9814','&#9816','&#9815','&#9813'];
	}
	let col = "";
	for (let c = 0; c < 5; c++) {
		col += "<td id='" + c + "' class='bughousePiece'>" + chooseArray[c] + "<span id='" + c + "' class=' badge badge-pill badge-primary'>0</span></td>";
	}
	$("#bank").append("<tr>" + col + "</tr>");
}


$("#bank").on("click", "td", function(e){
	let chooseArrayId = e.target.id;
	let currCountId = "span#" + chooseArrayId + ".badge";
    let pieceCountString = $(currCountId).html(); // gets curr piece count
    let pieceCount = parseInt(pieceCountString);

    if (myTurn) {
    	if (pieceCount !== 0) {
    		let selectTd = $(".bughousePiece#" + chooseArrayId).html();
    		let selectedPiece = selectTd.split("<")[0];
    		if (bughouseSelected && chooseArrayId == currBughousePiece) {
    			$(".bughousePiece#" + chooseArrayId).toggleClass('selected');
    			currBughousePiece = "";
    			bughouseSelected = false;
    		} else if (selected && !bughouseSelected) {
    			$("#" + currPiece).toggleClass('selected');
    			selected = false;
    			currPiece = "";
    			$(".bughousePiece#" + chooseArrayId).toggleClass('selected');
    			currBughousePiece = chooseArrayId;
    			bughouseSelected = true;
    		} else if (!selected && bughouseSelected) {
    			$(".bughousePiece#" + currBughousePiece).toggleClass('selected');
    			$(".bughousePiece#" + chooseArrayId).toggleClass('selected');
    			currBughousePiece = chooseArrayId;
    		} else if (!selected && !bughouseSelected) {
    			$(".bughousePiece#" + chooseArrayId).toggleClass('selected');
    			currBughousePiece = chooseArrayId;
    			bughouseSelected = true;
    		}
    		$("#chessboard").on("click", "td", function(e){
    			let currId = e.target.id;
    			if (!squareContainsPiece(currId)) {
    				$("#" + currId).html(selectedPiece);
    				$(".bughousePiece#" + chooseArrayId).toggleClass('selected');
    				currPieces.push(currId);
    				let placement = [chooseArrayId, currId];
    				new_placement(placement);
    				updateBankIndex(chooseArrayId, -1);
    				currBughousePiece = "";
    				bughouseSelected = false;
    				myTurn = false;
    				printTurn(myTurn);
    			}
    		});
    	}

    }
});

function updateBankIndex(index, x) {
	let countId = "span#" + index.toString() + ".badge";
	let pieceCountString = $(countId).html(); // gets curr piece count
	let pieceCount = parseInt(pieceCountString);
	let newPieceCount = pieceCount + x;
	$(countId).html(newPieceCount.toString());
}

$("#listRequest").on("click", ".list-group-item-action", function(e){
	let currId = e.target.id;
	let gameId = $("#gameId").text();
	new_request(currId, gameId);
});



function createRequestAlert(piece) {
	let curr = "";
	if (piece == "r") {
		curr = "Rook";
	} else if (piece == "q") {
		curr = "Queen";
	} else if (piece == "k") {
		curr = "Knight";
	} else if (piece == "b") {
		curr = "Bishop";
	} else if (piece == "p") {
		curr = "Pawn";
	}
	let html = "<div class='alert alert-info alert-success fade show' role='alert'>" +
	"<strong>Holy guacamole!</strong>Your teammate requests a " + curr + "." +
	"<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
	"<span aria-hidden='true'>&times;</span></button></div>"
	$("#alertBox").text(html);
}


