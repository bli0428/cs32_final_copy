const MESSAGE_TYPE = {
  CONNECT: 0,
  MOVE: 1,
  PLACEMENT: 2,
  UPDATE: 3
};

let conn;
let myId = -1;

// Setup the WebSocket connection for live updating of scores.
const setup_live_scores = () => {
  conn = new WebSocket("ws://localhost:4567/chess"); //TODO: change this

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
        black = data.payload.black; //TODO: maybe change based on payload scheme -> need to know whether player is black or white
        break;
      case MESSAGE_TYPE.UPDATE:
        currId = data.payload.id;

        //TODO: update board with appropriate move 
        // might have to do different cases for different players
        break;
    }
  };
}

var move = [];
var placement = [];
//TODO: WHERE TO CALL THIS?????????
const new_move = move => {
  var toSendPayload = {
    id: myId,
    moveFrom: convertCoordinates(move[0]),
    moveTo: move[1]
  }

  var toSend = {
    type: 1,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}

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
