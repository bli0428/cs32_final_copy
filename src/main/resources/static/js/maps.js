const CANVAS_WIDTH = 600;
const CANVAS_HEIGHT = 600;
const TILE_SIZE = .1;

// Global reference to the canvas element.
let canvas;

// Global reference to the canvas' context.
let ctx;

let startTop = 41.828402;
let startLeft = -71.404751;
let startBottom = 41.823035;
let startRight = -71.396564;

let totalZoom = 1;

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

    canvas.addEventListener("mousedown", function(e) {
        xDown = e.pageX;
        yDown = e.pageY;
    });
    canvas.addEventListener("mouseup", function(f) {
      let xUp = f.pageX;
      let yUp = f.pageY;
      changeBox(xUp - xDown, yUp - yDown);
    });
    
        canvas.addEventListener("click", function(g) {
            
            let xcoord = g.pageX - $('canvas').offset().left;
            let ycoord = g.pageY - $('canvas').offset().top;
            
            console.log(xcoord + " " + ycoord);
            
            let lon = posnToCoord(CANVAS_WIDTH, startLeft, startRight, xcoord);
            let lat = posnToCoord(CANVAS_HEIGHT, startBottom, startTop, ycoord);
            
            const postParameters = {lat: lat, lon: lon};
            
            $.post("/nearest", postParameters, responseJSON => {
                
                const responseObject = JSON.parse(responseJSON);
                
                let finalY = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, responseObject.node[0]);
				let finalX = coordToPosn(CANVAS_WIDTH, startLeft, startRight, responseObject.node[1]);
                
                //Wrong because of rotation of canvas.(maybe)
                console.log(finalX + " " + finalY);
                
                ctx.fillStyle = "red";
                ctx.fillRect(finalX, finalY, 10, 10);
                
            });
            
            
            
    });
    
    canvas.addEventListener("mousewheel", scrollZoom);
    
    
    
});

function scrollZoom(e){
    
    let amount = 0.05*e.deltaY/CANVAS_HEIGHT;
    
    if(totalZoom + amount > 0.9981){
    totalZoom += amount;
    console.log(totalZoom);
        
    startTop += amount;
  startBottom -= amount;
  startRight += amount;
  startLeft -= amount;
  ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
  paintMap();
    }
    
}


function key(lat, lng) {
  return lat.toFixed(4)+" "+lng.toFixed(4);
}
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
        const k = key(i,j);
        if (tiles[k] === undefined) {
          tiles[k] = [];
		      const postParameters = {
              top: i + TILE_SIZE, left: j,
			        bottom: i, right: j + TILE_SIZE};
		      $.post("/results", postParameters, responseJSON => {

			    // Parse the JSON response into a JavaScript object.
			    const responseObject = JSON.parse(responseJSON);
          //ctx.beginPath();
			    for (let way of responseObject.ways) {
				       let top = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, way[0][0]);
				       let left = coordToPosn(CANVAS_WIDTH, startLeft, startRight, way[0][1]);
				       let bottom = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, way[1][0]);
				       let right = coordToPosn(CANVAS_WIDTH, startLeft, startRight, way[1][1]);

                    
               tiles[k].push(way);

                    ctx.beginPath();
				       ctx.moveTo(top, left);
				       ctx.lineTo(bottom, right);
                    
                               ctx.closePath();
           ctx.stroke();
			     }
//           ctx.closePath();
//           ctx.stroke();
         });
       } else {
         ctx.beginPath();
         for (let way of tiles[key(i,j)]) {
           let top = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, way[0][0]);
           let left = coordToPosn(CANVAS_WIDTH, startLeft, startRight, way[0][1]);
           let bottom = coordToPosn(CANVAS_HEIGHT, startBottom, startTop, way[1][0]);
           let right = coordToPosn(CANVAS_WIDTH, startLeft, startRight, way[1][1]);
           ctx.moveTo(top, left);
           ctx.lineTo(bottom, right);
          }
          ctx.closePath();
          ctx.stroke();
       }
     }
   }
};

function zoom(amount) {
  startTop += amount;
  startBottom -= amount;
  startRight += amount;
  startLeft -= amount;
  ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
  paintMap();
}

function coordToPosn(canvasSize, min, max, coord) {
	mapSize = Math.abs(max - min);
	return (coord - min) * (canvasSize / mapSize);
}

function posnToCoord(canvasSize, min, max, coord){
    
    mapSize = Math.abs(max - min);
    return min + (coord*(mapSize/canvasSize));
    
}

function changeBox(offsetX, offsetY) {
  let ratioX = offsetX / CANVAS_WIDTH;
  let ratioY = offsetY / CANVAS_HEIGHT;
  const upDownOffset = ((startTop - startBottom) * ratioY);
  const leftRightOffset = ((startLeft - startRight) * ratioX);
  startTop += upDownOffset;
  startBottom += upDownOffset;
  startRight += leftRightOffset;
  startLeft += leftRightOffset;
  ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
  paintMap();
}
