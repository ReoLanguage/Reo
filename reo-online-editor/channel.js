(function() {

  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });

  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';

  fabric.Object.prototype.objectCaching = false;
  var active, isDown, origX, origY, origLeft, origTop;
  var mode = 'select';
  var id = '0';
  
  // drawing parameters
  
  nodeFillColourSource = '#fff';
  nodeFillColourDrain  = '#fff';
  nodeFillColourMixed  = '#000';
  nodeFactor           =      4;
  
  lineFillColour       = '#000';
  lineStrokeColour     = '#000';
  lineStrokeWidth      =      1;
  
  arrowFactor          =      8;
  arrowOffsetOut       = lineStrokeWidth * nodeFactor + 4;
  arrowOffsetIn        = arrowOffsetOut + arrowFactor;
  
  fifoHeight           =     30;
  fifoWidth            =     10;
  fifoFillColour       = '#fff';
  
  buttonBorderOff      = '2px solid white';
  buttonBorderOn       = '2px solid black';
  
  document.getElementById("select").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'select';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': true});
      }
    });
  };
  
  document.getElementById("component").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'component';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("sync").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'sync';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("lossysync").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'lossysync';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("syncdrain").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'syncdrain';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("syncspout").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'syncspout';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("fifo1").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'fifo1';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("downloadsvg").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.svg";
    a.href = 'data:image/svg+xml;base64,' + window.btoa(canvas.toSVG());
    a.click();
  };
  
  document.getElementById("downloadpng").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.png";
    a.href = canvas.toDataURL('image/png');
    a.click();
  };
  
  // generate a new object ID
  // ID will only contain letters, e.g. z is followed by aa
  function generateId() {
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');
    return id;
  }

  function createNode(left, top) {
    var node = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      hasControls: false,
      class: 'node',
      id: generateId()
    });

    // these are the channels that are connected to this node
    node.channels = [];
    
    var label = new fabric.IText(node.id, {
      left: left + 20,
      top: top - 20,
      fontSize: 20,
      object: node,
      class: 'label',
      hasControls: false
      //visible: false
    });
    
    node.set({'label': label, 'labelOffsetX': 20, 'labelOffsetY': -20});
    
    label.on('editing:exited', function(e) {
      label.object.set({id: label.text});
    });
    
    return node;

  } //createNode
  
  function createAnchor(left, top) {
    var anchor = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      hasControls: false,
      class: 'anchor',
      opacity: 0
    });
    return anchor;

  } //createAnchor
  
  function createChannel(x1, y1, x2, y2) {
    // create a channel...
    var channel = {};
    channel.components = [];
    
    // ...two nodes...
    channel.node1 = createNode(x1,y1);
    channel.node2 = createNode(x2,y2);
    
    // ...and two anchors
    // TODO
    channel.anchor1 = createAnchor(133,100);
    channel.anchor2 = createAnchor(167,100);
    
    // link the channel to the nodes
    channel.node1.channels.push(channel);
    channel.node2.channels.push(channel);
    
    return channel;
  } //createChannel

  function createSync(x1, y1, x2, y2) {
    // create a channel...
    var sync = createChannel(x1, y1, x2, y2);
    
    // ...a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: lineFillColour,
      stroke: lineStrokeColour,
      strokeWidth: lineStrokeWidth,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
    });
    sync.components.push(line);
    
    // ...and an arrowhead
    var a = new fabric.Triangle({
      left: x2,
      top: y2,
      width: arrowFactor * lineStrokeWidth,
      height: arrowFactor * lineStrokeWidth,
      baseAngle: 90,
      rotate: true,
      fill: lineFillColour,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      offset: arrowOffsetOut,
      reference: 'node2'
    });
    sync.components.push(a);
    
    drawChannel(sync);
  } //createSync
  
  function drawChannel(channel) {
    for (i = 0; i < channel.components.length; i++)
      canvas.add(channel.components[i]);
    canvas.add(channel.node1, channel.node2, channel.node1.label, channel.node2.label, channel.anchor1, channel.anchor2);
    updateChannel(channel);
  }
  
  function calculateAngle(channel, baseAngle) {
    var angle = 0;
    var x = (channel.node2.get('left') - channel.node1.get('left'));
    var y = (channel.node2.get('top')  - channel.node1.get('top'));

    if (x === 0) {
      angle = (y === 0) ? 0 : (y > 0) ? Math.PI / 2 : Math.PI * 3 / 2;
    } else if (y === 0) {
      angle = (x > 0) ? 0 : Math.PI;
    } else {
      angle = (x < 0) ? Math.atan(y / x) + Math.PI : (y < 0) ? Math.atan(y / x) + (2 * Math.PI) : Math.atan(y / x);
    }
    
    return ((angle * 180 / Math.PI) + baseAngle) % 360;
  } //calculateAngle
  
  function updateChannel (channel) {
    var x1 = channel.node1.get('left');
    var y1 = channel.node1.get('top');
    var x2 = channel.node2.get('left');
    var y2 = channel.node2.get('top');
    
    var length = Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1,2));
    var x = Math.atan(Math.abs(y1 - y2)/Math.abs(x1 - x2));
    
    for(i = 0; i < channel.components.length; i++) {
      var c = channel.components[i];
      switch (c.type) {
        case "line":
          c.set({'x1': x1});
          c.set({'y1': y1});
          c.set({'x2': x2});
          c.set({'y2': y2});
          break;
        case "triangle":
          if (x2 > x1) {
            c.set({'left': x2 - c.offset * Math.cos(x)});
          }
          else
          {
            c.set({'left': x2 + c.offset * Math.cos(x)});
          }
          if (y2 > y1) {
            c.set({'top': y2 - c.offset * Math.sin(x)});
          }
          else
          {
            c.set({'top': y2 + c.offset * Math.sin(x)});
          }
          if (c.rotate == true)
            c.setAngle(calculateAngle(channel, c.baseAngle));
          else
            c.setAngle(c.baseAngle);
          break;
      }
    }
    canvas.renderAll();
  }

  canvas.on('object:moving', function(e) {
    e.target.setCoords();
  }); //object:moving
  
  canvas.on('mouse:over', function(e) {
    if (e.target && e.target.class == "anchor")
    {
      e.target.set('opacity', '100');
      canvas.renderAll();
    }
  }); //mouse:over
  
  canvas.on('mouse:out', function(e) {
    if (e.target && e.target.class == "anchor")
    {
      e.target.set('opacity', '0');
      canvas.renderAll();
    }
  }); //mouse:out
  
  canvas.on('mouse:down', function(e) {
    isDown = true;
    var pointer = canvas.getPointer(e.e);
    origX = pointer.x;
    origY = pointer.y;
    var p = canvas.getActiveObject();
    if (p && mode != 'select') {
      origLeft = p.left;
      origTop = p.top;
      return;
    }    
    if (mode == 'select') {
      //console.log('Mode is select');
    }
    if (mode == 'sync') {
      canvas.deactivateAll();
      var channel = createSync(pointer.x,pointer.y,pointer.x,pointer.y);
      canvas.setActiveObject(channel.node2);
    }
  }); //mouse:down
  
  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var p = canvas.getActiveObject();
    if (!p)
      return;
    var pointer = canvas.getPointer(e.e);
    if (p.class == 'node') {
      p.set({'left': pointer.x, 'top': pointer.y});
      p.setCoords();
      canvas.forEachObject(function(obj) {
        if (obj !== p && p.intersectsWithObject(obj)) {
          if (obj.class === 'node') {
            if (Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
              p.set({'left': obj.left, 'top': obj.top});
              p.setCoords();
            }
          }
        }
      });
      
      for (i = 0; i < p.channels.length; i++)
        updateChannel(p.channels[i]);

      p.label.set({left: p.left + p.labelOffsetX});
      p.label.set({top: p.top + p.labelOffsetY});
      p.label.setCoords();
    }
    canvas.renderAll();
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p) {
      p.setCoords();
      if (p.class == 'node') {
        p.label.setCoords();
        p.set({labelOffsetX: p.label.left - p.left, labelOffsetY: p.label.top - p.top});
        canvas.forEachObject(function(obj) {
          if (!obj || obj.get('id') == p.get('id') || (obj.get('class') !== 'node' && obj.get('class') !== 'component'))
            return;
          if (p.intersectsWithObject(obj)) {
            if (obj.get('class') == 'node') {
              if(Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
                for (i = 0; i < p.channels.length; i++) {
                  p.channels[i].node2 = obj;
                  obj.channels.push(p.channels[i]);
                }
                for (i = 0; i < p.channels.length; i++) {
                  p.channels[i].circle1 = obj;
                  obj.channels.push(p.channels[i]);
                }
                canvas.remove(p.label, p);
                obj.bringToFront();
                canvas.renderAll();
              }
            }
          }
        });
        canvas.calcOffset();
      }
      if (p.class == 'label') {
        p.setCoords();
        p.object.set({'labelOffsetX': p.left - p.object.left, 'labelOffsetY': p.top - p.object.top});    
      }
      else
        canvas.deactivateAll();
      canvas.renderAll();
    }
  }); //mouse:up
  
  id = '0';
  document.getElementById("select").click();
  createSync(100,100,200,100);
})();