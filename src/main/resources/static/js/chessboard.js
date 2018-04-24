
var BOARD_DIM = 8;
var validMoves = []; // the valid moves of the currPiece
var currPiece = ""; // the selected piece to be moved
var black = false; // boolean indicating whether player is black or white
var currPieces = []; // an array of ids of the player's curr pieces
var selected = false;

var validMoveFunctionality = true; // boolean indicating whether or not to display valid moves

$(document).ready(() => {
        startBoard(black);
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

function startBoard(black) {
    for (var r = 0; r < BOARD_DIM; r++) {
        var col = "";
        for (var c = 0; c < BOARD_DIM; c++) {
            var id = r.toString() + "-" + c.toString();
            col += "<td id='" + id + "' class='" + getColor(r,c) + "'></td>";
        }
        $("#chessboard").append("<tr>" + col + "</tr>");
    }

    if (black) {
        // TODO: initialize board when player is black
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
    }
}

function getMoves(id) {
    if (currPieces.includes(id)) {
        // might have to change based on backend
        var splitId = id.split("-");
        var row = parseInt(splitId[0]);
        var col = parseInt(splitId[1]);

        const postParameters = {row: row, col: col};

        $.post("/chess", postParameters, responseJSON => {
            const responseObject = JSON.parse(responseJSON);
            validMoves = responseObject.validMoves;
            if (validMoveFunctionality) {
                displayValidMoves();
            }
        });
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


