
var BOARD_DIM = 8;
var validMoves = []; // the valid moves of the currPiece
var currPiece = ""; // the selected piece to be moved
var black = false; // boolean indicating whether player is black or white
var currPieces = []; // an array of ids of the player's curr pieces
var selected = false; // is there a current piece
var myTurn = true; // boolean indicating whether or not it is my turn (assume I move first)

var winner = ""; // might not need winner variable

const PLAYER_NUM = 0; // the id number of the session/player

//TODO: attatch button functionality
var validMoveFunctionality = true; // boolean indicating whether or not to display valid moves


function movePiece(start, end) {
    var startPiece = $("#" + start).text();

    if (validMoves.includes(end)) { // confirms that piece is being moved to a valid square
        $("#" + start).html("");
        $("#" + end).html(startPiece);

        $("#" + start).toggleClass('selected');
        if (validMoveFunctionality) {
            displayValidMoves();
        }

        currPiece = "";
        selected = false;
        removePieceFromCurrPieces(start);
        currPieces.push(end);

        checkCastleing(start, end, startPiece);

        var move = [start, end];
        new_move(move);
    }
}

function moveOpponent(start, end) {
    var startPiece = $("#" + start).text();
    $("#" + start).html("");
    $("#" + end).html(startPiece);
    if (currPieces.includes(end)) {
        removePieceFromCurrPieces(end);
    }    
}

function getMoves(id) {
    if (currPieces.includes(id)) {
        console.log("currPiece includes id");
        new_tohighlight(id);
    }
}

// toggle on and off the valid moves
function displayValidMoves() {
    for (var i = 0; i < validMoves.length; i++) {
        $("#" + validMoves[i]).toggleClass('validMove');
    }
}

//TODO: NEEED TO CHANGE CHESS LOGIC REMOVE PIECE IF PIECE IS IS IN SQUARE MOVED TO??
function squareContainsPiece(id) {
    if ($("#" + id).html() !=  "") {
        return true;
    } else {
        return false;
    }
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


    $(".modal").css("display", "block");
    getPromotionPiece();

    console.log(e.target.id);
    var currId = e.target.id;
    if (myTurn) {
        if (currPiece == currId) { //clicking on piece that is currently selected (deselect)
            console.log(1);
            $("#" + currPiece).toggleClass('selected');
            selected = false;
            currPiece = "";
            if (validMoveFunctionality) {
                displayValidMoves();  // clear the valid moves of the current piece
            }
        } else if (!selected && validPiece(currId)) { // first click
            console.log(2);
            currPiece = currId;
            $("#" + currPiece).toggleClass('selected');
            getMoves(currPiece);
            selected = true;
        } else if (selected && validPiece(currId)) { // another square has been picked
            console.log(3);
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

        //TODO: more cases
}

});

function getPromotionPiece() {
    $("#promotionMenu").on("click", "li", function(e){
        console.log(e.target.id);
    });
}


function checkCastleing(start, end, king) {
    if (king === '&#9818' || king ==== '&#9812') {
        if (getRow(start) === getRow(end)) {
            var kingRow = getRow(start);
            var kingCol = getCol(end);
            var rookCoordinates = "";
            var rook = "";
            var newRookCoordinates = "";
            if (getCol(start) - getCol(end) === 2) {
                rookCoordinates = kingRow.toString() + "-" + "0";
                rook = $("#" + rookCoordinates).html();
                newRookCoordinates = kingRow.toString() + "-" + (kingCol + 1).toString();
                $("#" + rookCoordinates).html("");
                $("#" + newRookCoordinates).html(rook);
                removePieceFromCurrPieces(rookCoordinates);
                currPieces.push(newRookCoordinates);
            } else if (getCol(start) - getCol(end) === -2) {
                rookCoordinates = kingRow.toString() + "-" + "7";
                rook = $("#" + rookCoordinates).html();
                newRookCoordinates = kingRow.toString() + "-" + (kingCol - 1).toString();
                $("#" + rookCoordinates).html("");
                $("#" + newRookCoordinates).html(rook);
                removePieceFromCurrPieces(rookCoordinates);
                currPieces.push(newRookCoordinates);
            }
        }
    }

}


function getRow(pos) {
    var split = pos.split("-");
    return parseInt(split[0]);
}

function getCol(pos) {
    var split = pos.split("-");
    return parseInt(split[1]);
}





// $("#moveToggle").on("click", function(){
//     toggleMoveFunctionality();
// });

// function toggleMoveFunctionality() {
//     if (validMoveFunctionality) {
//         validMoveFunctionality = false;
//     } else {
//         validMoveFunctionality = true;
//     }
// }
