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
  TOHIGHLIGHT: 9,
  TOPROMOTE: 10,
  DISPLAY: 11,
  BANKADD: 12
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
    switch (data.type) {
        default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        myId = data.payload.id;
        let payloadJoin = {
          id: $("#gameId").html()
        }
        let msgJoin = {
          type: MESSAGE_TYPE.JOINGAME,
          payload: payloadJoin
        }
        console.log(msgJoin);
        conn.send(JSON.stringify(msgJoin));
        break;
      case MESSAGE_TYPE.HIGHLIGHT:
        validMoves = [];
        let backendValidMoves = data.payload.validMoves;
        for (let i = 0; i < backendValidMoves.length; i++) {
          validMoves[i] = convertBackToFrontCoordinates(backendValidMoves[i]);
        }
        if (validMoveFunctionality) {
          displayValidMoves();
        }
        break;
      case MESSAGE_TYPE.UPDATE:
        let moveFrom = convertBackToFrontCoordinates(data.payload.moveFrom);
        let moveTo = convertBackToFrontCoordinates(data.payload.moveTo);
        moveOpponent(moveFrom, moveTo);
        myTurn = true;
        printTurn(myTurn);
        break;
      case MESSAGE_TYPE.GAMEOVER:
        let winner = data.payload.winner;
        printGameOver(winner);
        break;
      case MESSAGE_TYPE.PROMOTE:
        $(".modal").css("display", "block");
        let position = convertBackToFrontCoordinates(data.payload.position);
        let piece = promotePiece(position);
        new_promotion(piece, data.payload.position);
        break;
      case MESSAGE_TYPE.DISPLAY:
        initializeBank(data.payload.color);
        initializeBoard(data.payload.color);
        if (data.payload.color === "white") {
          myTurn = true;
        }
        printTurn(myTurn);
        break;
      case MESSAGE_TYPE.BANKADD:
        let pieceIndex = data.payload.idx;
        updateBankIndex(pieceIndex);
    }
  };
}

//TODO: UPDATE myTurn and printTurn

const new_tohighlight = currPiece => {
  let toSendPayload = {
    id: myId,
    piece: convertFrontToBackCoordinates(currPiece)
  }

  let toSend = {
    type: 9,
    payload: toSendPayload
  }
  conn.send(JSON.stringify(toSend));
}


const new_move = move => {
  let toSendPayload = {
    id: myId,
    moveFrom: convertFrontToBackCoordinates(move[0]),
    moveTo: convertFrontToBackCoordinates(move[1])
  }

  let toSend = {
    type: 1,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
  console.log("Sent move");
}


const new_promotion = (piece, position) => {
  let toSendPayload = {
    id: myId,
    piece: piece,
    position: position
  }

  let toSend = {
    type: 10,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}


const new_placement = placement => {
  let toSendPayload = {
    id: myId,
    bankIndex: placement[0],
    moveTo: convertFrontToBackCoordinates(placement[1])
  }

  let toSend = {
    type: 2,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}

