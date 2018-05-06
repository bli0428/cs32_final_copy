const JOIN_MESSAGE_TYPE = {
  CONNECT: 0,
  UPDATE: 1,
  JOIN_USER: 2,
  START_CHESS_GAME: 3,
  START_BUGHOUSE_GAME: 4,
  SWITCH_TEAM: 5,
  ADD_AI: 6,
  LEAVE_GAME: 7
};

let menuConn;
let myMenuId = -1;
let ip;

const setupMenu = () => {
  const postParameters = {};
  $.post("/getIp", postParameters, responseJSON => {

    // Parse the JSON response into a JavaScript object.
    const responseObject = JSON.parse(responseJSON);
    ip = responseObject.ip;
    //menuConn = new WebSocket("ws://" + ip + ":4567/join");
    menuConn = new WebSocket("ws://localhost:4567/join");


  menuConn.onerror = err => {
    console.log('Connection error:', err);
  };

  menuConn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case JOIN_MESSAGE_TYPE.CONNECT:
        myMenuId = data.payload.id;
        console.log("id" + myMenuId);

        const postParameters = {};
        $.post("/getUser", postParameters, responseJSON => {

          // Parse the JSON response into a JavaScript object.
          const responseObject = JSON.parse(responseJSON);
          new_join_lobby(responseObject.session);
        });
        break;
      case JOIN_MESSAGE_TYPE.UPDATE:
        $("#users").html(data.payload.list);
        break;
      case JOIN_MESSAGE_TYPE.START_CHESS_GAME:
        $(location).attr('href', '/chessgame/' + $("#gameId").html());
        break;
      case JOIN_MESSAGE_TYPE.START_BUGHOUSE_GAME:
        $(location).attr('href', '/bughousegame/' + $("#gameId").html());
        break;
    }
  };
  });
}

function addAI(index) {
  var toSendPayload = {
    id: myId,
    gameId: $("#gameId").html(),
    index: index
  }

  var toSend = {
    type: 6,
    payload: toSendPayload
  }
  menuConn.send(JSON.stringify(toSend));
}

function switchTeam() {
  const postParameters = {};
  $.post("/getUser", postParameters, responseJSON => {
    // Parse the JSON response into a JavaScript object.
    const responseObject = JSON.parse(responseJSON);
    var toSendPayload = {
      id: myId,
      sparkSession: responseObject.session,
      userId: responseObject.id,
      gameId: $("#gameId").html()
    }

    var toSend = {
      type: 5,
      payload: toSendPayload
    }
    menuConn.send(JSON.stringify(toSend));
  });
}

function leaveGame() {
  const postParameters = {};
  $.post("/getUser", postParameters, responseJSON => {
    // Parse the JSON response into a JavaScript object.
    const responseObject = JSON.parse(responseJSON);
    var toSendPayload = {
      id: myId,
      sparkSession: responseObject.session,
      userId: responseObject.id,
      gameId: $("#gameId").html()
    }

    var toSend = {
      type: 7,
      payload: toSendPayload
    }
    menuConn.send(JSON.stringify(toSend));
    $(location).attr('href', '/home');
  });
}

const new_join_lobby = sparkSession => {
  var toSendPayload = {
    id: myId,
    sparkSession: sparkSession,
    gameId: $("#gameId").html()
  }

  var toSend = {
    type: 2,
    payload: toSendPayload
  }
  menuConn.send(JSON.stringify(toSend));
}

function addGame(type) {
  const postParameters = {gameType:type};
  $.post("/addGame", postParameters, responseJSON => {

    // Parse the JSON response into a JavaScript object.
    const responseObject = JSON.parse(responseJSON);
    $("#menu").html(responseObject.games);
    // const suggestionElements = $("#suggestions1 li");
    // suggestionElements.on('click', event => {
    //   console.log(event);
    //   $("#searchbar1").val(event.currentTarget.innerHTML);
    // });
  });
}
