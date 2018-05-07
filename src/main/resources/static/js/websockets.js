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
  BANKADD: 12,
  REQUEST: 13
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
          id: $("#gameId").html(),
          gamePosition: $("#gamePosition").html()
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
        console.log(data.payload.moveFrom);
        if (data.payload.moveFrom === "0,0") {
          console.log("placement");
          let piece = data.payload.piece;
          let color = data.payload.color; // 0 for white, 1 for black
          let moveTo = convertBackToFrontCoordinates(data.payload.moveTo);
          setPlacement(color, piece, moveTo);
        } else {
          console.log("move");
          let moveFrom = convertBackToFrontCoordinates(data.payload.moveFrom);
          let moveTo = convertBackToFrontCoordinates(data.payload.moveTo);
          moveOpponent(moveFrom, moveTo);
        }
        myTurn = true;
        printTurn(myTurn);
        break;
      case MESSAGE_TYPE.GAMEOVER:
        let winner = data.payload.winner;
        printGameOver(winner);
        break;
      case MESSAGE_TYPE.PROMOTE:
        $('#modal').modal({backdrop: 'static', keyboard: false});
        let position = convertBackToFrontCoordinates(data.payload.position);
        promotePiece(position);
        myTurn = false;
        printTurn(myTurn);
        break;
      case MESSAGE_TYPE.DISPLAY:
        initializeBoard(data.payload.color);
        if (data.payload.game == false) { // false = bughouse
          initializeBank(data.payload.color);
        }
        if (data.payload.color == 0) { // 0 = false
          myTurn = true;
        }
        printTurn(myTurn);
        break;
      case MESSAGE_TYPE.BANKADD:
        let pieceIndex = data.payload.idx;
        updateBankIndex(pieceIndex, 1);
    }
  };
}


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


const new_request = (piece, gameId) => {
  let toSendPayload = {
    id: myId,
    piece: piece,
    gameId: gameId
  }

  let toSend = {
    type: 13,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}



