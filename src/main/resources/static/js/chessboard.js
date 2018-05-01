
var BOARD_DIM = 8;
var validMoves = []; // the valid moves of the currPiece
var currPiece = ""; // the selected piece to be moved
var black = false; // boolean indicating whether player is black or white
var currPieces = []; // an array of ids of the player's curr pieces
var selected = false; // is there a current piece
const PLAYER_NUM = 0; // the id number of the session/player

//TODO: attatch button functionality
var validMoveFunctionality = true; // boolean indicating whether or not to display valid moves

$(document).ready(() => {
        initializeBoard(black);
        initializeBank();
    });

function getColor(row, col) {
    if (row % 2 == 0 && col % 2 == 0) {
        return "white";
    } else if (row == col) {
        return "white";
    } else if (row % 2 == 1 && col % 2 == 1) {
        return "white";
    } else {
        return "black";
    }
}

function initializeBoard(black) {
    for (var r = 0; r < BOARD_DIM; r++) {
        var col = "";
        for (var c = 0; c < BOARD_DIM; c++) {
            var id = r.toString() + "-" + c.toString();
            col += "<td id='" + id + "' class='" + getColor(r,c) + "'></td>";
        }
        $("#chessboard").append("<tr>" + col + "</tr>");
    }

    if (black) {
        // white pieces
        $('#0-0').html('&#9814');
        $('#0-1').html('&#9816');
        $('#0-2').html('&#9815');
        $('#0-3').html('&#9813');
        $('#0-4').html('&#9812');
        $('#0-5').html('&#9815');
        $('#0-6').html('&#9816');
        $('#0-7').html('&#9814');

        $('#1-0').html('&#9817');
        $('#1-1').html('&#9817');
        $('#1-2').html('&#9817');
        $('#1-3').html('&#9817');
        $('#1-4').html('&#9817');
        $('#1-5').html('&#9817');
        $('#1-6').html('&#9817');
        $('#1-7').html('&#9817');

         // black pieces
        $('#7-0').html('&#9820');
        $('#7-1').html('&#9822');
        $('#7-2').html('&#9821');
        $('#7-3').html('&#9819');
        $('#7-4').html('&#9818');
        $('#7-5').html('&#9821');
        $('#7-6').html('&#9822');
        $('#7-7').html('&#9820');

        $('#6-0').html('&#9823');
        $('#6-1').html('&#9823');
        $('#6-2').html('&#9823');
        $('#6-3').html('&#9823');
        $('#6-4').html('&#9823');
        $('#6-5').html('&#9823');
        $('#6-6').html('&#9823');
        $('#6-7').html('&#9823');
    } else {
        // black pieces
        $('#0-0').html('&#9820');
        $('#0-1').html('&#9822');
        $('#0-2').html('&#9821');
        $('#0-3').html('&#9819');
        $('#0-4').html('&#9818');
        $('#0-5').html('&#9821');
        $('#0-6').html('&#9822');
        $('#0-7').html('&#9820');

        $('#1-0').html('&#9823');
        $('#1-1').html('&#9823');
        $('#1-2').html('&#9823');
        $('#1-3').html('&#9823');
        $('#1-4').html('&#9823');
        $('#1-5').html('&#9823');
        $('#1-6').html('&#9823');
        $('#1-7').html('&#9823');

        // white pieces
        $('#7-0').html('&#9814');
        $('#7-1').html('&#9816');
        $('#7-2').html('&#9815');
        $('#7-3').html('&#9813');
        $('#7-4').html('&#9812');
        $('#7-5').html('&#9815');
        $('#7-6').html('&#9816');
        $('#7-7').html('&#9814');

        $('#6-0').html('&#9817');
        $('#6-1').html('&#9817');
        $('#6-2').html('&#9817');
        $('#6-3').html('&#9817');
        $('#6-4').html('&#9817');
        $('#6-5').html('&#9817');
        $('#6-6').html('&#9817');
        $('#6-7').html('&#9817');
    }
    currPieces = ["7-0", "7-1", "7-2", "7-3", "7-4", "7-5", "7-6", "7-7",
                    "6-0", "6-1", "6-2", "6-3", "6-4", "6-5", "6-6", "6-7"];

}

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

        //TODO: send update to backend, also need to think about pieces getting removed
        // maybe: pass in a piece to remove, if its null, no piece has to be removed, if it contains something remove the piece and update the board
        // piece needs to be added to the bank
    }
}

function getMoves(id) {
    if (currPieces.includes(id)) {
        new_tohighlight();
    } 
}

// toggle on and off the valid moves
function displayValidMoves() {
    for (var i = 0; i < validMoves.length; i++) {
        $("#" + validMoves[i]).toggleClass('validMove');
    }
}

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
    console.log(e.target.id);
    var currId = e.target.id;
    console.log(selected);

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
    } 

    else if (selected && currId != currPiece && validMoves.includes(currId)) {
        movePiece(currPiece, currId);
    }

    //TODO: more cases

});

function convertFrontToBackCoordinates(id) {
    var splitId = id.split("-");
    var row = BOARD_DIM - parseInt(splitId[0]);
    var col = parseInt(splitId[1]) + 1;
    var toReturn = col.toString() + "," + row.toString();
    return toReturn;
}

function convertBackToFrontCoordinates(stringCoordinates) {
    var splitCoordinates = stringCoordinates.split(",");
    var row = BOARD_DIM - parseInt(splitCoordinates[1]);
    var col = parseInt(splitCoordinates[0]) - 1;
    var toReturn = row.toString() + "-" + col.toString();
    return toReturn;
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


