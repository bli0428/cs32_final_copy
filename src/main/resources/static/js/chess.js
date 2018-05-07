
var validMoves = []; // the valid moves of the currPiece
var currPiece = ""; // the selected piece to be moved
var currPieces = []; // the player's curr pieces
var selected = false; // boolean indicating if there is a current piece selected

var myTurn = false; // boolean indicating whether or not it is my turn TODO: CHANGE (assume I move first)

var black = true;

var cachedPiece = "";
var cachedCoordinates = "";

//when I get a piece that I'm promoting, store the piece type and coordinates, when I get a moveOpponent to that square place that thing


function movePiece(start, end) {
    let startPiece = $("#" + start).text(); // piece to be moved

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
        validMoves = [];

        // update player's currPieces
        removePieceFromCurrPieces(start);
        currPieces.push(end);

        checkCastling(start, end, startPiece);

        let move = [start, end];
        new_move(move); // send the move to the backend
    }
}

function moveOpponent(start, end) {
    console.log("in moveOpponent");
    if (cachedPiece !== "") {
        $("#" + start).html("");
        $("#" + end).html(cachedPiece);
        cachedPiece = "";
    } else {
        let startPiece = $("#" + start).text(); // piece to be moved
        $("#" + start).html("");
        $("#" + end).html(startPiece);
        checkCastling(start, end, startPiece);
    }
    if (currPieces.includes(end)) { // removes curr player's piece from play if captured
            removePieceFromCurrPieces(end);
    }
    
}

function getMoves(id) {
    if (currPieces.includes(id)) {
        new_tohighlight(id); // sends request to backend to get highlighted moves
    }
}

// toggle on and off the valid moves
function displayValidMoves() {
    for (let i = 0; i < validMoves.length; i++) {
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
    let removeIndex = currPieces.indexOf(id);
    currPieces.splice(removeIndex, 1);
    $("#" + id).html("");
}

function removePieceFromCurrPieces(id) {
    let removeIndex = currPieces.indexOf(id);
    currPieces.splice(removeIndex, 1);
}

$("#chessboard").on("click", "td", function(e){
    let currId = e.target.id;
    if (myTurn) {
        if (selected && currPiece == currId) { //clicking on piece that is currently selected (deselect)
            $("#" + currPiece).toggleClass('selected');
            selected = false;
            currPiece = "";
            if (validMoveFunctionality) {
                displayValidMoves();  // clear the valid moves of the current piece
            }
            validMoves = [];
        } else if (!selected && !bughouseSelected && validPiece(currId)) { // first click
            currPiece = currId;
            $("#" + currPiece).toggleClass('selected');
            selected = true;
            getMoves(currPiece);
        } else if (selected && !bughouseSelected && validPiece(currId)) { // another square has been picked
            $("#" + currPiece).toggleClass('selected');
            if (validMoveFunctionality) {
                displayValidMoves(); // reset the previously selected valid moves
            }
            currPiece = currId;
            $("#" + currPiece).toggleClass('selected');
            getMoves(currPiece);
        } else if (!selected && bughouseSelected && validPiece(currId)) {
            $(".bughousePiece#" + currBughousePiece).toggleClass('selected');
            currBughousePiece = "";
            bughouseSelected = false;
            currPiece = currId;
            $("#" + currPiece).toggleClass('selected');
            selected = true;
            getMoves(currPiece);
        } else if (selected && currId != currPiece && validMoves.includes(currId)) { // a move is being made
            movePiece(currPiece, currId);
            selected = false;
            myTurn = false;
            printTurn(myTurn);
        }

        //TODO: more cases???
    }

});

function promotePiece(coordinates) {
    console.log("in promotePiece");
    let piece = "";
    $('#modal').modal('show')
    $("#promotionMenu").on("click", "li", function(e){
        piece = e.target.id;
        $('#modal').modal('hide')
        new_promotion(piece, convertFrontToBackCoordinates(coordinates));
        setPromotionPiece(piece, coordinates);
    });
}

function setPromotionPiece(piece, coordinates) {
    console.log("in setPromotionPiece");
    if (black) {
        if (piece == "rook" || piece == "r") {
            $("#" + coordinates).html('&#9820');
        } else if (piece == "queen" || piece == "q") {
            $("#" + coordinates).html('&#9819');
        } else if (piece == "knight" || piece == "k") {
            $("#" + coordinates).html('&#9822');
        } else if (piece == "bishop" || piece == "b") {
            $("#" + coordinates).html('&#9821');
        }
    } else {
        if (piece == "rook" || piece == "r") {
            $("#" + coordinates).html('&#9814');
        } else if (piece == "queen" || piece == "q") {
            $("#" + coordinates).html('&#9813');
        } else if (piece == "knight" || piece == "k") {
            $("#" + coordinates).html('&#9816');
        } else if (piece == "bishop" || piece == "b") {
            $("#" + coordinates).html('&#9815');
        }
    }
}

function setPromotionPiecePupdate(piece) {
    console.log("in setPromotionPiecePupdate");
    console.log("piece: " + piece);

    if (!black) {
        if (piece == "rook" || piece == "r") {
            cachedPiece = '&#9820';
        } else if (piece == "queen" || piece == "q") {
            cachedPiece = '&#9819';
        } else if (piece == "knight" || piece == "k") {
            cachedPiece = '&#9822';
        } else if (piece == "bishop" || piece == "b") {
            cachedPiece = '&#9821';
        }
    } else {
        if (piece == "rook" || piece == "r") {
            cachedPiece = '&#9814';
        } else if (piece == "queen" || piece == "q") {
            cachedPiece = '&#9813';
            $("#" + coordinates).html();
        } else if (piece == "knight" || piece == "k") {
            cachedPiece = '&#9816';
        } else if (piece == "bishop" || piece == "b") {
            cachedPiece = '&#9815';
        }
    }
}

function setPlacement(black, piece, coordinates) {
    if (black) { // if true (black = 1)
        if (piece == "r") {
            $("#" + coordinates).html('&#9820');
        } else if (piece == "q") {
            $("#" + coordinates).html('&#9819');
        } else if (piece == "k") {
            $("#" + coordinates).html('&#9822');
        } else if (piece == "b") {
            $("#" + coordinates).html('&#9821');
        } else if (piece == "p") {
            $("#" + coordinates).html('&#9823');
        }
    } else {
        if (piece == "r") {
            $("#" + coordinates).html('&#9814');
        } else if (piece == "q") {
            $("#" + coordinates).html('&#9813');
        } else if (piece == "k") {
            $("#" + coordinates).html('&#9816');
        } else if (piece == "b") {
            $("#" + coordinates).html('&#9815');
        } else if (piece == "p") {
            $("#" + coordinates).html('&#9817');
        }
    }
}



function checkCastling(start, end, king) {
    if (king == "♔" || king == "♚") {
        if (getRow(start) == getRow(end)) {
            let kingRow = getRow(start);
            let kingCol = getCol(end);
            let rookCoordinates = "";
            let rook = "";
            let newRookCoordinates = "";
            if (getCol(start) - getCol(end) == 2) {
                rookCoordinates = kingRow.toString() + "-" + "0";
                rook = $("#" + rookCoordinates).text();
                newRookCoordinates = kingRow.toString() + "-" + (kingCol + 1).toString();
                $("#" + rookCoordinates).html("");
                $("#" + newRookCoordinates).html(rook);
                removePieceFromCurrPieces(rookCoordinates);
                currPieces.push(newRookCoordinates);
            } else if (getCol(start) - getCol(end) == -2) {
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
