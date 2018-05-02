
var validMoves = []; // the valid moves of the currPiece
var currPiece = ""; // the selected piece to be moved
var currPieces = []; // the player's curr pieces
var selected = false; // boolean indicating if there is a current piece selected

var myTurn = true; // boolean indicating whether or not it is my turn TODO: CHANGE (assume I move first)


function movePiece(start, end) {
    var startPiece = $("#" + start).text(); // piece to be moved

    if (validMoves.includes(end)) { // confirms that piece is being moved to a valid square
        $("#" + start).html("");
        $("#" + end).html(startPiece);

        // unselect currPiece
        $("#" + start).toggleClass('selected');

        // clear valid move squares
        if (validMoveFunctionality) {
            displayValidMoves();
        }

        currPiece = "";
        selected = false;

        // update player's currPieces
        removePieceFromCurrPieces(start);
        currPieces.push(end);

        checkCastling(start, end, startPiece);

        var move = [start, end];
        new_move(move); // send the move to the backend
    }
}

function moveOpponent(start, end) {
    var startPiece = $("#" + start).text(); // piece to be moved
    $("#" + start).html("");
    $("#" + end).html(startPiece);
    if (currPieces.includes(end)) { // removes curr player's piece from play if captured
        removePieceFromCurrPieces(end);
    }
    checkCastling(start, end, startPiece);
}

function getMoves(id) {
    if (currPieces.includes(id)) {
        new_tohighlight(id); // sends request to backend to get highlighted moves
    }
}

// toggle on and off the valid moves
function displayValidMoves() {
    for (var i = 0; i < validMoves.length; i++) {
        $("#" + validMoves[i]).toggleClass('validMove');
    }
}


function squareContainsPiece(id) {
    return ($("#" + id).html() !==  "");
}

function validPiece(id) {
    return currPieces.includes(id);
}

function removePieceFromBoard(id) {
    var removeIndex = currPieces.indexOf(id);
    currPieces.splice(removeIndex, 1);
    $("#" + id).html("");
}

function removePieceFromCurrPieces(id) {
    var removeIndex = currPieces.indexOf(id);
    currPieces.splice(removeIndex, 1);
}

$("#chessboard").on("click", "td", function(e){
    var currId = e.target.id;
    console.log(currId);
    if (myTurn) {
        if (currPiece == currId) { //clicking on piece that is currently selected (deselect)
            $("#" + currPiece).toggleClass('selected');
            selected = false;
            currPiece = "";
            if (validMoveFunctionality) {
                displayValidMoves();  // clear the valid moves of the current piece
            }
        } else if (!selected && validPiece(currId)) { // first click
            currPiece = currId;
            $("#" + currPiece).toggleClass('selected');
            getMoves(currPiece);
            selected = true;
        } else if (selected && validPiece(currId)) { // another square has been picked
            $("#" + currPiece).toggleClass('selected');
            if (validMoveFunctionality) {
                displayValidMoves(); // reset the previously selected valid moves
            }
            currPiece = currId;
            $("#" + currPiece).toggleClass('selected');
            getMoves(currPiece);
        } else if (selected && currId != currPiece && validMoves.includes(currId)) {
            movePiece(currPiece, currId);
            myTurn = false;
            printTurn(myTurn);
        }

        //TODO: more cases???
    }

});

function promotePiece(coordinates) {
    $("#promotionMenu").on("click", ".promoteOption", function(e){
        var piece = e.target.id;
        $(".modal").css("display", "none");
        setPromotionPiece(piece, coordinates);
    });
}

function setPromotionPiece(piece, coordinates) {
    if (black) {
        if (piece === "rook") {
            $("#" + coordinates).html('&#9820');
        } else if (piece === "queen") {
            $("#" + coordinates).html('&#9819');
        } else if (piece === "knight") {
            $("#" + coordinates).html('&#9822');
        } else if (piece === "bishop") {
            $("#" + coordinates).html('&#9821');
        }
    } else {
        if (piece === "rook") {
            $("#" + coordinates).html('&#9814');
        } else if (piece === "queen") {
            $("#" + coordinates).html('&#9813');
        } else if (piece === "knight") {
            $("#" + coordinates).html('&#9816');
        } else if (piece === "bishop") {
            $("#" + coordinates).html('&#9815');
        }
    }
}


function checkCastling(start, end, king) {

    if (king === "♔" || king === "♚") {
        if (getRow(start) === getRow(end)) {
            var kingRow = getRow(start);
            var kingCol = getCol(end);
            var rookCoordinates = "";
            var rook = "";
            var newRookCoordinates = "";
            if (getCol(start) - getCol(end) === 2) {
                rookCoordinates = kingRow.toString() + "-" + "0";
                rook = $("#" + rookCoordinates).text();
                newRookCoordinates = kingRow.toString() + "-" + (kingCol + 1).toString();
                $("#" + rookCoordinates).html("");
                $("#" + newRookCoordinates).html(rook);
                removePieceFromCurrPieces(rookCoordinates);
                currPieces.push(newRookCoordinates);
            } else if (getCol(start) - getCol(end) === -2) {
                rookCoordinates = kingRow.toString() + "-" + "7";
                rook = $("#" + rookCoordinates).text();
                newRookCoordinates = kingRow.toString() + "-" + (kingCol - 1).toString();
                $("#" + rookCoordinates).html("");
                $("#" + newRookCoordinates).html(rook);
                removePieceFromCurrPieces(rookCoordinates);
                currPieces.push(newRookCoordinates);
            }
        }
    }

}
