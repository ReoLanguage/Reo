"use strict";

function Canvas(canvas, options){
  // Canvas abstraction object.
  if(!(this instanceof Canvas))
    return;

  if(!(canvas instanceof HTMLCanvasElement))
    throw new TypeError("Canvas must be HTMLCanvasElement");

  this.options = options || {
    style: {
      lineWidth: 2,
      fill: "#fff",
      stroke: "#000",
      font: "bold 1em sans-serif"
    }
  };

  this.canvas = canvas;
  this.context = this.canvas.getContext("2d");
  if(!this.context)
    throw new Error("Cannot get CanvasRenderingContext2D");
}

Canvas.prototype._endPath = function(style){
  // End a path with stroke and/or fill. If there is no style, the path will
  //  be stroked.
  this.context.lineWidth = (style && style.lineWidth)
                            || this.options.style.lineWidth;

  this.context.fillStyle = (style && style.fill)
                            || this.options.style.fill;
  (style && style.fill) && this.context.fill();

  this.context.strokeStyle = (style && style.stroke)
                              || this.options.style.stroke;
  (style && !style.stroke) || this.context.stroke();
};

Canvas.prototype.clear = function(){
  // Clear the canvas.
  this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
};

Canvas.prototype.drawLine = function(from, to, style){
  // Draw a line from [from] to [to].
  this.context.beginPath();
  this.context.moveTo(from[0], from[1]);
  this.context.lineTo(to[0], to[1]);
  this._endPath(style);
};

Canvas.prototype.drawPath = function(coords, style){
  // Draw a path along given [coords].
  this.context.beginPath();

  var waypoint = coords.shift();
  this.context.moveTo(waypoint[0], waypoint[1]);
  while(waypoint = coords.shift()){
    this.context.lineTo(waypoint[0], waypoint[1]);
  }

  this._endPath(style);
};

Canvas.prototype.drawDashedLine = function(from, to, style){
  // Draw a dashed line from [from] to [to].
  this.context.save();
  this.context.translate(from[0], from[1]);
  this.context.beginPath();

  var dx = to[0] - from[0],
      dy = to[1] - from[1];

  // rotate so [from] is up
  var angle = (2 * Math.PI) - Math.atan2(dx, dy);
  this.context.rotate(angle);
  this.context.moveTo(0, 0);

  var len = Math.sqrt(dx * dx + dy * dy);

  var mid = 0;
  while(mid < len){
    mid += 10;
    this.context.lineTo(0, mid > len?len:mid);
    mid += 10;
    this.context.moveTo(0, mid);
  }

  this._endPath(style);
  this.context.restore();
};

Canvas.prototype.drawCircle = function(coord, radius, style){
  // Draw a circle of [radius] with its center at [coord].
  this.context.beginPath();
  this.context.arc(coord[0], coord[1], radius, 0, 2 * Math.PI);
  this._endPath(style);
};

Canvas.prototype.drawRect = function(coord, width, height, opt, style){
  // Draw a rectangle of [width]x[height] at [coord].
  // Options:
  //   angle: angle around [coord]
  //   center: [coord] is center of rect rather than top left
  this.context.save();
  this.context.translate(coord[0], coord[1]);
  (opt && opt.angle) && this.context.rotate(opt.angle);
  this.context.beginPath();

  // draw center at coord if [opt.center] is true
  var mul = (opt && opt.center)?-0.5:0;
  this.context.rect(mul * width, mul * height, width, height);

  this._endPath(style);
  this.context.restore();
};

Canvas.prototype.drawPolygon = function(coord, opt, style){
  // Draw a [opt.n]-sided polygon of [opt.radius], centered at [coord].
  //  The first point will be to the right of the center point.
  // Options:
  //   n: amount of points (required)
  //   radius: radius around center (required)
  //   angle: angle around [coord]
  this.context.save();
  this.context.translate(coord[0], coord[1]);
  (opt && opt.angle) && this.context.rotate(opt.angle);

  this.context.beginPath();
  for(var i = 1; i <= opt.n; i++){
    this.context.lineTo(Math.cos(i * (2 * Math.PI / opt.n)) * opt.radius,
                        Math.sin(i * (2 * Math.PI / opt.n)) * opt.radius);
  }
  this.context.closePath();

  this._endPath(style);
  this.context.restore();
};

Canvas.prototype.drawText = function(coord, text, opt, style){
  // Draw [text] at [coord]. The text will always be filled, and optionally
  //  stroked if one is specified.
  // Options:
  //   angle: angle around [coord]
  //   align: horizontal text alignment
  //   baseline: vertical text alignment
  //   font: text font
  //   max: maximum width for text (shrink text if exceeded)
  this.context.save();
  this.context.translate(coord[0], coord[1]);
  (opt && opt.angle) && this.context.rotate(opt.angle);

  this.context.textAlign = (opt && opt.align) || "start";
  this.context.textBaseline = (opt && opt.baseline) || "alphabetic";
  this.context.font = (opt && opt.font) || this.options.style.font;

  this.context.fillStyle = (style && style.fill)
                            || this.options.style.stroke;
  if(opt && opt.max)
    this.context.fillText(text, 0, 0, opt.max);
  else
    this.context.fillText(text, 0, 0);

  if(style && style.stroke){
    this.context.strokeStyle = style.stroke;
    if(opt && opt.max)
      this.context.strokeText(text, 0, 0, opt.max);
    else
      this.context.strokeText(text, 0, 0);
  }

  this.context.restore();
};

Canvas.prototype.drawArrow = function(from, to, opt, style){
  // Draw an arrow from [from] to [to].
  // Options:
  //   dashed: whether to draw dashed arrow
  //   text: text to add to the edge
  if(opt && opt.dashed)
    this.drawDashedLine(from, to, style);
  else
    this.drawLine(from, to, style);

  // draw arrowhead
  this.context.save();
  this.context.translate(to[0], to[1]);
  this.context.beginPath();

  var dx = to[0] - from[0],
      dy = to[1] - from[1];

  // rotate so [from] is up
  var angle = (2 * Math.PI) - Math.atan2(dx, dy);
  this.context.rotate(angle);

  this.context.moveTo(0, 0);
  this.context.lineTo(3, -15);

  this.context.lineTo(0, -10);
  this.context.lineTo(-3, -15);
  this.context.closePath();

  // always fill arrowhead
  this.context.fillStyle = (style && style.stroke)
                             || this.options.style.stroke;
  this.context.fill();
  this._endPath(style);

  // draw text, flip if rotated >= 180deg
  if(opt && opt.text){
    var len = Math.sqrt(dx * dx + dy * dy),
        side = (angle / Math.PI) < 2;

    this.drawText([side?15:-15, -len / 2], opt.text,
                  {align: "center", max: len,
                   angle: (side?1:-1) * (Math.PI / 2)});
  }

  this.context.restore();
};
