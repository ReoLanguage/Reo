(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';

  function makeCircle(left, top) {
    var c = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: 5,
      radius: 12,
      fill: '#fff',
      stroke: '#000'
    });
    c.hasControls = c.hasBorders = false;

    c.linesIn = [];
    c.linesOut = [];

    return c;
  }

  function drawLine(x1, y1, x2, y2) {
    var l = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      selectable: false,
      circle1: null,
      circle2: null
    });
    
    var a = new fabric.Triangle({
      left: l.get('x2'),
      top: l.get('y2'),
      width: 20,
      height: 20,
      angle: calcArrowAngle(x1,y1,x2,y2),
      fill: '#000',
      hasBorders: false,
      hasControls: false,
      line: l
    });
    
    var c1 = makeCircle(x1,y1);
    var c2 = makeCircle(x2,y2);
    
    l.set({arrow: a, circle1: c1, circle2: c2});
    
    var length = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    length = length - 22;//circle2.get('radius') - circle2.get('stroke');
    var x = Math.atan((y2 - y1)/(x2 - x1));
    if (x2 > x1) {
      l.set({'x2': x1 + length * Math.cos(x)});
    } else {
      l.set({'x2': x1 - length * Math.cos(x)});
    }
    if (y2 > y1) {
      l.set({'y2': y1 + length * Math.sin(x)});
    } else {
      l.set({'y2': y1 - length * Math.sin(x)});
    }
    a.set({left: l.get('x2'), top: l.get('y2')});
    
    c1.linesOut.push(l);
    c2.linesIn.push(l);
    
    canvas.add(l,a,c1,c2);
    canvas.sendToBack(l);
    canvas.renderAll();
  } //drawLine
  
  function diff(a,b) {
    if (a > b)
      return a-b;
    return b-a;
  }
  
  function updateLine(line) {
    var x1 = line.get('x1'),
        y1 = line.get('y1'),
        x2 = line.get('x2'),
        y2 = line.get('y2');
        
    var length = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    length = length - 22;//circle2.get('radius') - circle2.get('stroke');
    var x = Math.atan(diff(y1,y2)/diff(x1,x2));
    document.getElementById("x").value = x;
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
    line.arrow.set({'left': line.get('x2'), 'top': line.get('y2')});
    var angle = calcArrowAngle(line.get('x1'), line.get('y1'), line.get('x2'), line.get('y2'));
    line.arrow.set({'angle': angle});
    canvas.renderAll();
    document.getElementById("x1").value = x1;
    document.getElementById("y1").value = y1;
    document.getElementById("x2").value = x2;
    document.getElementById("y2").value = y2;
    document.getElementById("angle").value = angle;
  }
  
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
  }
  
  drawLine(100,100,200,100);

  canvas.on('object:moving', function(e) {
    var p = e.target;
    if (p.get('type') == "circle") {
      for (i = 0; i < p.linesIn.length; i++) {
        p.linesIn[i].set({ 'x2': p.left, 'y2': p.top });
        updateLine(p.linesIn[i]);
      }
      for (i = 0; i < p.linesOut.length; i++) {
        p.linesOut[i].set({ 'x1': p.left, 'y1': p.top });
        updateLine(p.linesOut[i]);
      }
    }
    if (p.get('type') == "triangle") {
      p.line.set({ 'x2': p.left, 'y2': p.top });
      var angle = calcArrowAngle(p.line.get('x1'), p.line.get('y1'), p.line.get('x2'), p.line.get('y2'));
      p.set({'angle':angle});
      document.getElementById("angle").value = angle;
    }
    if (p.get('type') == "line") {
      p.arrow.set({ 'left': p.x2, 'top': p.y2 });
      var angle = calcArrowAngle(p.get('x1'), p.get('y1'), p.get('x2'), p.get('y2'))
      p.arrow.set({'angle':angle});
      document.getElementById("angle").value = angle;
    }
    canvas.renderAll();
  });
})();
