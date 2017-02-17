(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  var line, isDown;
  var id = 'a';

  function makeCircle(left, top) {
    var c = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: 5,
      radius: 12,
      fill: '#fff',
      stroke: '#000',
      hasBorders: false,
      hasControls: false
    });
    
    // give the node an identifier and increment it for the next node
    c.id = id;
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');

    // these are the channels that are connected to this node
    c.linesIn = [];
    c.linesOut = [];

    return c;
  } //makeCircle

  function drawLine(x1, y1, x2, y2) {
    // create a line...
    line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false
    });
    
    // ...an arrowhead...
    var a = new fabric.Triangle({
      left: x2,
      top: y2,
      width: 20,
      height: 20,
      angle: calcArrowAngle(x1,y1,x2,y2),
      fill: '#000',
      hasBorders: false,
      hasControls: false
    });
    
    // ...and two circles
    var c1 = makeCircle(x1,y1);
    var c2 = makeCircle(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    c1.linesOut.push(line);
    c2.linesIn.push(line);
    
    // magic
    updateLine(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,c1,c2);
    canvas.renderAll();
  } //drawLine
  
  function updateLine(line, end) {
    // we first have update the end coordinates
    var x1 = line.circle1.get('left');
    var y1 = line.circle1.get('top');
    var x2 = line.circle2.get('left');
    var y2 = line.circle2.get('top');
    line.set({'x1':x1, 'y1':y1, 'x2':x2, 'y2':y2});
    
    if (x1 == x2 && y1 == y2) {
      line.arrow.set({'left': line.get('x2'), 'top': line.get('y2')});
    }
    else {
      // calculate the position of the arrow
      var length = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
      length = length - 22;//circle2.get('radius') - circle2.get('stroke');
      var x = Math.atan(Math.abs(y1-y2)/Math.abs(x1-x2));
      document.getElementById("x").value = x;
      if (end) {
        if (x2 > x1) {
          line.set({'x2': x1 + length * Math.cos(x)});
        } else {
          line.set({'x2': x1 - length * Math.cos(x)});
        }
        if (y2 > y1) {
          line.set({'y2': y1 + length * Math.sin(x)});
        } else {
          line.set({'y2': y1 - length * Math.sin(x)});
        }
      }
      line.arrow.set({'left': line.get('x2'), 'top': line.get('y2')});
      var angle = calcArrowAngle(line.get('x1'), line.get('y1'), line.get('x2'), line.get('y2'));
      line.arrow.set({'angle': angle});
    }
    
    canvas.renderAll();
    document.getElementById("x1").value = x1;
    document.getElementById("y1").value = y1;
    document.getElementById("x2").value = x2;
    document.getElementById("y2").value = y2;
    document.getElementById("angle").value = angle;
  } //updateLine
  
  // calculate the correct angle for the arrowhead
  function calcArrowAngle(x1, y1, x2, y2) {
    var angle = 0, x, y;
    x = (x2 - x1);
    y = (y2 - y1);

    if (x === 0) {
      angle = (y === 0) ? 0 : (y > 0) ? Math.PI / 2 : Math.PI * 3 / 2;
    } else if (y === 0) {
      angle = (x > 0) ? 0 : Math.PI;
    } else {
      angle = (x < 0) ? Math.atan(y / x) + Math.PI : (y < 0) ? Math.atan(y / x) + (2 * Math.PI) : Math.atan(y / x);
    }

    return (angle * 180 / Math.PI) + 90;
  } //calcArrowAngle
  
  function updateText() {
    var s = '';
    canvas.forEachObject(function(obj) {
      if (obj.type == "line")
        s += 'sync(' + obj.circle1.id + ',' + obj.circle2.id + ') ';
    });
    document.getElementById('text').innerHTML = s;
  }

  canvas.on('object:moving', function(e) {
    var p = e.target;
    p.setCoords();
  }); //object:moving
  
  canvas.on('object:added', function(e) {
    updateText();
  }); //object:added
  
  canvas.on('object:removed', function(e) {
    updateText();
  }); //object:added
  
  canvas.on('mouse:down', function(e) {
    isDown = true;
    if (canvas.getActiveObject())
      return;
    var pointer = canvas.getPointer(e.e);
    drawLine(pointer.x,pointer.y,pointer.x,pointer.y);
    canvas.setActiveObject(line.circle2);
    updateLine(line,2);
  }); //mouse:down
  
  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var pointer = canvas.getPointer(e.e);
    var p = canvas.getActiveObject();
    p.set({left: pointer.x, top: pointer.y});
    
    canvas.forEachObject(function(obj) {
      if (obj === p || obj.get('type') !== "circle") return;
      if (p.intersectsWithObject(obj)) {
        if(Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
          p.setLeft(obj.getLeft());
          p.setTop(obj.getTop());
          p.setCoords();
        }
      }
    });
    
    for (i = 0; i < p.linesIn.length; i++)
      updateLine(p.linesIn[i], 2);
    for (i = 0; i < p.linesOut.length; i++)
      updateLine(p.linesOut[i], 1);
    //canvas.renderAll();
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    canvas.forEachObject(function(obj) {
      if (!obj || obj === p || obj.get('type') !== "circle") return;
      if (p.intersectsWithObject(obj)) {
        if(Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
          for (i = 0; i < p.linesIn.length; i++) {
            p.linesIn[i].circle2 = obj;
            obj.linesIn.push(p.linesIn[i]);
          }
          for (i = 0; i < p.linesOut.length; i++) {
            p.linesOut[i].circle1 = obj;
            obj.linesOut.push(p.linesOut[i]);
          }
          canvas.remove(p);
          obj.bringToFront();
        }
      }
    });
  });
  
  drawLine(100,100,200,100);
  drawLine(300,100,400,100);
  
})();
