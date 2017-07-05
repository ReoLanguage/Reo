"use strict";

// Polyfills for older browsers
window.requestAnimationFrame = window.requestAnimationFrame || (function(){
  return window.webkitRequestAnimationFrame
          || window.mozRequestAnimationFrame
          || function(callback){
    return window.setTimeout(callback, 1000 / 60, Date.now());
  };
})();

window.cancelAnimationFrame = window.cancelAnimationFrame || (function(){
  return window.webkitCancelAnimationFrame
          || window.mozCancelAnimationFrame
          || function(requestID){
    return window.clearTimeout(requestID);
  };
})();

var Reo = Reo || {};

Reo.Animation = function(canvas, options){
  // Animation object.
  if(!(this instanceof Reo.Animation))
    return;

  this.options = options || {
    labelMerge: true,
    stepTime: 3000
  };

  this.canvas = new Canvas(canvas);
  this.connector = null;

  this.io = [];

  this.pending = [];
  this.blocked = [];

  this.state = "idle";
  this.tickStart = -1;

  var lastFrame = -1;
  (this.startAnimation = (function(n){
    this.animationFrame(n);
    lastFrame = window.requestAnimationFrame(this.startAnimation);
  }).bind(this))();

  this.stopAnimation = function(){
    window.cancelAnimationFrame(lastFrame);
  };

  this.finishCallback = null;
};

Reo.Animation.prototype = {
  constructor: Reo.Animation.prototype.constructor,
  set connector(v){
    this._connector = v;
    this.state = "idle";
    this.tickStart = -1;
  },
  get connector(){
    return this._connector;
  }
};

Reo.Animation.prototype.drawChannel = function(chan){
  // Draw a channel [chan] with optional coloring [col].
  // get channel end coordinates
  var type = chan.animationType || chan.type;
  var ends = chan.ends,
      from = ends[0].node.coord,
      to = ends[1].node.coord;

  // determine angle towards to-end
  var ang = Math.atan2(to[0] - from[0], to[1] - from[1]);

  // adjust coords to end at the node rather than inside the node
  var p04 = [from[0] + Math.sin(ang) * 5,
             from[1] + Math.cos(ang) * 5],
      p44 = [to[0] - Math.sin(ang) * 5,
             to[1] - Math.cos(ang) * 5];

  // calculate points partway along the channel (fourths)
  var p24 = [(from[0] + to[0]) / 2,
             (from[1] + to[1]) / 2],
      p14 = [(p24[0] + from[0]) / 2,
             (p24[1] + from[1]) / 2],
      p34 = [(p24[0] + to[0]) / 2,
             (p24[1] + to[1]) / 2];

  // flow marks
  if(ends[0].color=="-"){
    this.canvas.drawLine(from, p24,
                         {stroke: "rgba(0, 0, 255, 0.5)", lineWidth: 15});
  }
  if(ends[1].color=="-"){
    this.canvas.drawLine(p24, to,
                         {stroke: "rgba(0, 0, 255, 0.5)", lineWidth: 15});
  }

  // the channel itself
  type.draw && type.draw(this.canvas, p04, p44, [p14, p24, p34], ang);

  // no-flow marks
  if(ends[0].color==">" || ends[0].color=="<"){
    var n = (ends[0].color==">")?1:-1;
    // flip if this is not a source end
    if(chan.sinks.indexOf(ends[0]) >= 0)
      n = -n;

    this.canvas.drawPolygon(p14,
                            {n: 3, radius: 10,
                             angle: n * (Math.PI / 2) - ang},
                            {stroke: "#c00", lineWidth: 3});
  }
  if(ends[1].color==">" || ends[1].color=="<"){
    var n = (ends[1].color==">")?1:-1;
    // flip if this is not a sink end
    if(chan.sources.indexOf(ends[1]) >= 0)
      n = -n;

    this.canvas.drawPolygon(p34,
                            {n: 3, radius: 10,
                             angle: n * (Math.PI / 2) - ang},
                            {stroke: "#c00", lineWidth: 3});
  }
};

Reo.Animation.prototype.drawConnector = function(){
  // Draws the current connector to the canvas.
  // draw outgoing channels
  for(var i in this.connector.channels){
    var chan = this.connector.channels[i];
    this.drawChannel(chan);
  }

  // draw components
  for(var i in this.connector.components){
    var comp = this.connector.components[i];
    this.canvas.drawRect(comp.coord, comp.size[0], comp.size[1] + 20,
                         {center: true}, {stroke: "#000", fill: "#fff"});

    var label = comp.connector.id + (Reo.debug?(" (" + comp.id + ")"):"");
    this.canvas.drawText(comp.coord, label,
                         {align: "center", baseline: "middle"});
  }

  // draw nodes
  for(var i in this.connector.nodes){
    var node = this.connector.nodes[i];
    switch(node.type){
      case "read":
      case "write":
        this.canvas.drawRect(node.coord, 10, 10, {center: true},
                             {stroke: "#000", fill: "#fff"});
      break;
      case "xrouter":
        this.canvas.drawCircle(node.coord, 8,
                               {stroke: "#000", fill: "#fff"});

        this.canvas.drawPolygon(node.coord,
                                {n: 2, radius: 8, angle: 1 * (Math.PI / 4)});
        this.canvas.drawPolygon(node.coord,
                                {n: 2, radius: 8, angle: 3 * (Math.PI / 4)});
      break;
      default:
        this.canvas.drawCircle(node.coord, 5,
                               {stroke: "#000", fill: "#fff"});
      break;
    }

    // don't place labels on merge nodes
    if(!this.options.labelMerge && node.type=="merge")
      continue;

    // naïve heuristic for label placement
    var xOff = node.coord[0] - (this.canvas.canvas.width / 2),
        yOff = node.coord[1] - (this.canvas.canvas.height / 2);
    if(xOff!=0)
      xOff = ((xOff / Math.abs(xOff)) * 15);
    if(yOff!=0)
      yOff = ((yOff / Math.abs(yOff)) * 15);
    if(xOff==0 && yOff==0)
      yOff = -15;

    // draw label
    this.canvas.drawText([node.coord[0] + xOff, node.coord[1] + yOff],
                         node.id, {align: "center", baseline: "middle"});
  }
};

Reo.Animation.prototype._originStep = function(t){
  // Draw origin step.
  // Returns whether animation is finished.
  var rel = 2 * (t % this.options.stepTime) / this.options.stepTime,
      finished = (rel > 1);

  var noItems = true;
  for(var i in this.connector.channels){
    var chan = this.connector.channels[i],
        ends = chan.ends;
    if(!chan.type.origin)
      continue;

    var from = ends[0].node.coord,
        to = ends[1].node.coord,
        mid = [(from[0] + to[0]) / 2,
               (from[1] + to[1]) / 2];

    for(var j in chan.sinks){
      var sink = chan.sinks[j],
          sinkCoord = sink.node.coord;
      if(sink.color!="-")
        continue;

      noItems = false;

      chan.animationType = chan.type.out;
      this.canvas.drawPolygon([mid[0] + (rel * (sinkCoord[0] - mid[0])),
                               mid[1] + (rel * (sinkCoord[1] - mid[1]))],
                              {n: 5, radius: 8, angle: -Math.PI / 2},
                              {fill: "#fc0"});
    }
  }

  return finished || noItems;
};

Reo.Animation.prototype._propagateInStep = function(t){
  // Draw propagation step.
  // Returns whether animation is finished.
  var rel = 2 * (t % this.options.stepTime) / this.options.stepTime,
      finished = false;
  if(rel >= 1){
    this.pending.forEach(function(end){
      var chan = end.channel;
      if(end.color!="-" || chan.sinks.indexOf(end) >= 0)
        return;

      if(chan.animationType)
        chan.animationType = chan.animationType.in;
      else
        chan.animationType = chan.type.in;
    });
    finished = true;
  }

  var noItems = true;
  for(var i in this.pending){
    var end = this.pending[i],
        chan = end.channel,
        ends = chan.ends;

    // calculate middle
    var from = ends[0].node.coord,
        to = ends[1].node.coord,
        mid = [(from[0] + to[0]) / 2,
               (from[1] + to[1]) / 2];

    // not incoming or no flow
    if(end.color!="-" || chan.sinks.indexOf(end) >= 0)
      continue;

    noItems = false;

    var here = end.node.coord;
    this.canvas.drawPolygon([here[0] + (rel * (mid[0] - here[0])),
                             here[1] + (rel * (mid[1] - here[1]))],
                            {n: 5, radius: 8, angle: -Math.PI / 2},
                            {fill: "#fc0"});
  }

  return finished || noItems;
};

Reo.Animation.prototype._propagateOutStep = function(t){
  // Draw propagation step.
  // Returns whether animation is finished.
  var rel = 2 * (t % this.options.stepTime) / this.options.stepTime,
      finished = (rel > 1);

  var noItems = true;
  for(var i in this.pending){
    var end = this.pending[i],
        chan = end.channel,
        ends = chan.ends;
    if(chan.type.origin)
      continue;

    // calculate middle
    var from = ends[0].node.coord,
        to = ends[1].node.coord,
        mid = [(from[0] + to[0]) / 2,
               (from[1] + to[1]) / 2];

    // find outgoing end (we have incoming end)
    end = chan.ends[1 - chan.ends.indexOf(end)];

    // not outgoing or no flow
    if(end.color!="-" || chan.sources.indexOf(end) >= 0)
      continue;

    noItems = false;

    var here = end.node.coord;
    this.canvas.drawPolygon([mid[0] + (rel * (here[0] - mid[0])),
                             mid[1] + (rel * (here[1] - mid[1]))],
                            {n: 5, radius: 8, angle: -Math.PI / 2},
                            {fill: "#fc0"});
  }

  return finished || noItems;
};

Reo.Animation.prototype.animationFrame = function(n){
  // Draw a single frame of animation.
  this.canvas.clear();
  if(!this.connector)
    return;

  function _checkBlockers(end, pending, blocked){
    // Returns if an [end]'s channel is blocking and its other end is not
    //  present, and move it to [blocked] if so. If the other end is also
    //  blocking, move the other end to [pending].
    if(end.channel.sinks.length==0){
      // drain channel
      var ends = end.channel.sources,
          there = ends[1 - ends.indexOf(end)];

      var bi = blocked.indexOf(there);
      if(bi >= 0){
        pending.push(blocked.splice(bi)[0]);
      }else if(pending.indexOf(there) < 0 && there.color=="-"){
        blocked.push(end);
        return true;
      }
    }
    return false;
  }

  this.drawConnector();

  if(Reo.debug){
    // show activity with rotating throbber
    this.canvas.drawPolygon([30, 30],
                            {n: 5, radius: 15,
                             angle: (n / 400) % (2 * Math.PI)},
                            {stroke: "#080", fill: "#0c0"});

    // display which state we're in
    this.canvas.drawText([60, 30], this.state, {baseline: "middle"});

    // display amount of colorings
    if(this.connector._colorings){
      var colTotal = this.connector._colorings.length + " total colorings",
          colPeak = this.connector._peak + " at peak";
      this.canvas.drawText([this.canvas.canvas.width - 30, 30],
                           colTotal + ", " + colPeak,
                           {align: "right", baseline: "middle"});
      this.canvas.drawText([this.canvas.canvas.width - 30, 50],
                           "(w/o nextables)",
                           {align: "right", baseline: "middle"});
    }
  }

  var t = n - this.tickStart;
  switch(this.state){
    case "start":
      this.pending = [];
      this.blocked = [];

      for(var i in this.io){
        var end = this.io[i];

        if(end.color!="-" || end.node.type!="write")
          continue;

        if(!_checkBlockers(end, this.pending, this.blocked))
          this.pending.push(end);
      }

      // unmark all ends
      for(var i in this.connector.nodes){
        var node = this.connector.nodes[i];
        node.ends.forEach(function(n){
          n.visited = false;
        });
      }

      // set origin sinks to pending
      var hasOrigins = false;
      for(var i in this.connector.channels){
        var chan = this.connector.channels[i];

        // clear intermediate type
        chan.animationType = null;

        if(!chan.type.origin)
          continue;

        for(var j in chan.sinks){
          var end = chan.sinks[j];
          // even though there may be no sinks (and thus no 'regular' pending),
          // we should still animate the origin step (sink may be read node)
          if(end.color=="-")
            hasOrigins = true;
          else
            continue;

          end.node.sinks.forEach(function(n){
            if(n.color!="-")
              return;

            if(!_checkBlockers(n, this.pending, this.blocked))
              this.pending.push(n);
          }, this);
        }
      }

      t = 0;
      this.tickStart = n;

      if(this.pending.length==0 && !hasOrigins)
        this.state = "delay";
      else
        this.state = "origin";

      Reo._debug.clear();
      Reo._debug.log("n: [" + this.pending.map(function(n){
        return n.node.id + "/" + n.channel.id;
      }) + "]\nb: [" + this.blocked.map(function(n){
        return n.node.id + "/" + n.channel.id;
      }) + "]");
    break;

    // there are no data items; delay a bit
    case "delay":
      if(t >= (0.5 * this.options.stepTime))
        this.state = "done";
    break;

    // draw data items originating from channels
    case "origin":
      if(this._originStep(t)){
        this.state = "propagateIn";
        this.tickStart = n;
      }
    break;

    // draw data items being propagated from pending nodes into channels
    case "propagateIn":
      if(this._propagateInStep(t)){
        this.state = "propagateOut";
        this.tickStart = n;
      }
    break;

    // draw data items being propagated from channels into next pending nodes
    case "propagateOut":
      if(this._propagateOutStep(t)){
        this.state = "propagateNext";
        this.tickStart = n;
      }
    break;

    // determine next pending nodes
    case "propagateNext":
      var next = [],
          block = [];
      for(var i; i = this.pending.shift();){
        i.visited = true;

        // there is only one sink (spouts are origins)
        var sink = i.channel.sinks[0];
        if(!sink)
          continue;
        sink = sink.node;

        // determine which sinks are outgoing
        var sinks = sink.sinks;
        if(sink.type=="edge"){
          // TODO/1: this will output when the first of the inputs arrive,
          // TODO/2: which is not always correct (see lossyChooser).
          var comp = this.connector.map[sink.id].component,
              ends = [];
          // get all of the component's read nodes
          for(var j in comp.connector.nodes){
            var node = comp.connector.nodes[j];
            if(node.type=="read")
              ends.push(node.id);
          }

          sinks = ends.reduce((function(ret, n){
            return ret.concat(this.connector.nodes[comp.map[n]].sinks);
          }).bind(this), []);
        }

        // add all outgoing sink ends
        for(var j in sinks){
          var end = sinks[j];

          if(_checkBlockers(end, next, block)
              || end.color!="-" || end.visited)
            continue;

          next.push(end);
          end.visited = true;
        }
      }

      // check for unblocks
      for(var i in this.blocked){
        var ends = this.blocked[i].channel.sources,
            here = this.blocked[i],
            there = ends[1 - ends.indexOf(here)];

        var j = block.indexOf(there);
        if(j >= 0){
          next.push(here);
          next.push(block.splice(j)[0]);
        }else{
          block.push(here);
        }

        continue;
      }

      this.pending = next;
      this.blocked = block;

      Reo._debug.log("n: [" + this.pending.map(function(n){
        return n.node.id + "/" + n.channel.id;
      }) + "]\nb: [" + this.blocked.map(function(n){
        return n.node.id + "/" + n.channel.id;
      }) + "]");

      if(this.pending.length > 0){
        this.tickStart = n;
        this.state = "propagateIn";
        this._propagateInStep(0);
      }else{
        this.state = "done";
      }
    break;

    case "done":
      this.state = "idle";
      (this.finishCallback) && this.finishCallback();
    break;

    default:
    break;
  }

  // draw all blocked items
  // (except origin blockers since those haven't 'propagated out' yet)
  if(this.state=="idle" || this.state=="origin")
    return;

  for(var i in this.blocked){
    this.canvas.drawPolygon(this.blocked[i].node.coord,
                            {n: 5, radius: 8, angle: -Math.PI / 2},
                            {fill: "#c00"});
  }
};
