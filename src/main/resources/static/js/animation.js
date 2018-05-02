var ChessBackground = {
  pieceHeight: 400,
  pieceWidth: 400,
  pieces: [],
  pieceImage: 'http://i63.tinypic.com/hx0osh.png',
  maxPieces: 15,
  minScale: 0.4,
  draw: function() {
    this.setCanvasSize();
    this.ctx.clearRect(0, 0, this.w, this.h);
    for (var i = 0; i < this.pieces.length; i++) {
      var piece = this.pieces[i];
      piece.image = new Image();
      piece.image.style.height = piece.height;
      //pawn,
      var images = ['http://i63.tinypic.com/hx0osh.png', 
      'http://i65.tinypic.com/2a4py1g.png', 
      'http://i63.tinypic.com/2dvkd3k.png',
      'http://i66.tinypic.com/2q83sat.png',
      'http://i63.tinypic.com/qxo0ow.png',
      'http://i67.tinypic.com/inwbpg.png'];
      if (i < 6) {
          piece.image.src = images[0];
      } else {
          if (i < 9) {
              piece.image.src = images[1];
          } else {
              if (i < 12) {
                  piece.image.src = images[2];
              } else {
                  if (i < 14) {
                      piece.image.src = images[3];
                  } else {
                      if (i < 15) {
                          piece.image.src = images[4];
                      } else {
                          piece.image.src = images[5];
                      }
                  }
              }
          }
      }
      
      this.ctx.globalAlpha = piece.opacity;
      this.ctx.drawImage (piece.image, piece.x, piece.y, piece.width, piece.height);
    }
    this.move();
  },
  move: function() {
    for(var b = 0; b < this.pieces.length; b++) {
      var piece = this.pieces[b];
      
      piece.y += piece.ys;
      if(piece.y > this.h) {
        piece.x = Math.random() * this.w;
        piece.y = -1 * this.pieceHeight;
      }
    }
  },
  setCanvasSize: function() {
    this.canvas.width = window.innerWidth;
    this.canvas.height = window.innerHeight;
    this.w = this.canvas.width;
    this.h = this.canvas.height;
  },
  initialize: function() {
    this.canvas = $('#canvas')[0];

    if(!this.canvas.getContext)
      return;

    this.setCanvasSize();
    this.ctx = this.canvas.getContext('2d');

    for(var a = 0; a < this.maxPieces; a++) {
      var scale = (Math.random() * (1 - this.minScale)) + this.minScale;
      this.pieces.push({
        x: Math.random() * this.w,
        y: Math.random() * this.h,
        ys: Math.random() + 1,
        height: scale * this.pieceHeight,
        width: scale * this.pieceWidth,
        opacity: scale
      });
    }

    setInterval($.proxy(this.draw, this), 30);
  }
};

$(document).ready(function(){
  ChessBackground.initialize();
});