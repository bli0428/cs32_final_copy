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
  conn = new WebSocket("ws://localhost:4567/scores"); //TODO: change this

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
        break;
    }
  };
}

//TODO: WHERE TO CALL THIS?????????
function new_move {
  var toSendPayload = {
    id: myId,
    moveFrom: , //TODO: pass in og coordinate
    moveTo: //TODO: pass in new coordinate
  }

  var toSend = {
    type: 1,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}

function new_placement {
  var toSendPayload = {
    id: myId,
    bankIndex: , //TODO: index in bank
    moveTo: //TODO: pass in new coordinate
  }

  var toSend = {
    type: 2,
    payload: toSendPayload
  }

  conn.send(JSON.stringify(toSend));
}
