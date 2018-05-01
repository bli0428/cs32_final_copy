const MESSAGE_TYPE = {
  CONNECT: 0,
  MOVE: 1,
  PLACEMENT: 2,
  UPDATE: 3,
  GAMEOVER: 4,
  PROMOTE: 5,
  CREATEGAME: 6,
  JOINGAME: 7,
  HIGHLIGHT: 8,
  TOHIGHLIGHT: 9
};

let conn;
let myId = -1;

// Setup the WebSocket connection for live updating of scores.
const setup_live_moves = () => {
  conn = new WebSocket("ws://localhost:4567/play"); //TODO: change this

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    console.log("here!!!");
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        console.log("in frontend connect")
        myId = data.payload.id;
        //TODO: maybe need to know whether player is black or white
        break;
      case MESSAGE_TYPE.HIGHLIGHT:
        console.log("highlight");
        validMoves = [];
        var backendValidMoves = data.payload.validMoves;
        console.log(backendValidMoves);
        for (var i = 0; i < backendValidMoves.length; i++) {
          validMoves[i] = convertBackToFrontCoordinates(backendValidMoves[i]);
        }
        console.log(validMoves);
        if (validMoveFunctionality) {
            displayValidMoves();
        }
        break;
      case MESSAGE_TYPE.UPDATE:
        currId = data.payload.id;

        //TODO: update board with appropriate move 
        // might have to do different cases for different players
        break;
    }
  };
}


const new_tohighlight = currPiece => {
  var toSendPayload = {
    id: myId,
    piece: convertFrontToBackCoordinates(currPiece)
  }

  var toSend = {
    type: 9,
    payload: toSendPayload
  }
  conn.send(JSON.stringify(toSend));

  console.log(toSend);

  console.log("sent");
}


const new_move = move => {
  var toSendPayload = {
    id: myId,
    moveFrom: convertFrontToBackCoordinates(move[0]),
    moveTo: convertFrontToBackCoordinates(move[1])
  }

  var toSend = {
    type: 1,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}











//////////////////////////////////////////////////



var placement = [];

const new_placement = placement => {
  var toSendPayload = {
    id: myId,
    bankIndex: placement[0], //TODO: index in bank
    moveTo: placement[1] //TODO: pass in new coordinate
  }

  var toSend = {
    type: 2,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}


