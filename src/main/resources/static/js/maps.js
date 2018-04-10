const CANVAS_WIDTH = 600;
const CANVAS_HEIGHT = 600;
const TILE_SIZE = .004;

// Global reference to the canvas element.
let canvas;

// Global reference to the canvas' context.
let ctx;

let startTop = 41.828402;
let startLeft = -71.404751;
let startBottom = 41.823035;
let startRight = -71.396564;

let xDown;
let yDown;

let scrollLock = false;

let tiles = [];

/*
	When the document is ready, this runs.
*/
$(document).ready(() => {
    // Setting up the canvas.
    canvas = $('#map')[0];
    canvas.width = CANVAS_WIDTH;
    canvas.height = CANVAS_HEIGHT;

    // Set up the canvas context.
    ctx = canvas.getContext("2d");

    ctx.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
    ctx.rotate(-90 * Math.PI / 180);
    ctx.translate(-CANVAS_WIDTH/2, -CANVAS_HEIGHT/2);
    paintMap();

    canvas.addEventListener("mousedown", function(e){
        xDown = e.pageX;
        yDown = e.pageY;
    });
    canvas.addEventListener("mouseup", function(f){
      let xUp = f.pageX;
      let yUp = f.pageY;
      changeBox(xUp - xDown, yUp - yDown);
    });
    document.addEventListener("scroll", function(g) {
      if (scrollLock) return;
      scrollLock = true;
      //Put something here
      scrollLock = false;
    });
});

/*
	Paints the boggle board.
*/
const paintMap = () => {

	// Setting the context's font and lineWidth.
	// Feel free to play around with this!
    ctx.font = '56px Andale Mono';
    ctx.lineWidth = 1;

    let currX = Math.floor(startLeft / TILE_SIZE) * TILE_SIZE;
    let currY = Math.floor(startBottom / TILE_SIZE) * TILE_SIZE;

    for (let i = currY; i < startTop; i += TILE_SIZE) {
      for (let j = currX; j < startRight; j += TILE_SIZE) {
        if (tiles[i + " " + j] === undefined) {
          tiles[i + " " + j] = [];
		      const postParameters = {top: i + TILE_SIZE, left: j,
			         bottom: i, right: j + TILE_SIZE};
		      $.post("/results", postParameters, responseJSON => {

			    // Parse the JSON response into a JavaScript object.
			    const responseObject = JSON.parse(responseJSON);
          ctx.beginPath();
			    for (let way of responseObject.ways) {
				       let top = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, way[0][0]);
				       let left = coordToPosn(CANVAS_WIDTH, startLeft, startRight, way[0][1]);
				       let bottom = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, way[1][0]);
				       let right = coordToPosn(CANVAS_WIDTH, startLeft, startRight, way[1][1]);

              tiles[i + " " + j].push({top: top, left: left, bottom: bottom, right: right});

				       ctx.moveTo(top, left);
				       ctx.lineTo(bottom, right);
			     }
           ctx.closePath();
           ctx.stroke();
         });
       } else {
         ctx.beginPath();
         for (let way of tiles[i + " " + j]) {
              ctx.moveTo(way.top, way.left);
              ctx.lineTo(way.bottom, way.right);
          }
          ctx.closePath();
          ctx.stroke();
       }
     }
   }
};

function coordToPosn(canvasSize, min, max, coord) {
	mapSize = Math.abs(max - min);
	return (coord - min) * (canvasSize / mapSize);
}

function changeBox(offsetX, offsetY) {
  let ratioX = offsetX / CANVAS_WIDTH;
  let ratioY = offsetY / CANVAS_HEIGHT;
  const upDownOffset = ((startTop - startBottom) * ratioY);
  const leftRightOffset = ((startLeft - startRight) * ratioX);
  startTop = startTop + upDownOffset;
  startBottom = startBottom + upDownOffset;
  startRight = startRight + leftRightOffset;
  startLeft = startLeft + leftRightOffset;
  ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
  paintMap();
}
