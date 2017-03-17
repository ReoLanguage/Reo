(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  var active, isDown, origX, origY;
  var mode = 'select';
  var id = 'a';
  
  document.getElementById("select").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'select';
    this.style.border = '2px solid black';
  };
  
  document.getElementById("component").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'component';
    this.style.border = '2px solid black';
  };
  
  document.getElementById("sync").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'sync';
    this.style.border = '2px solid black';
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

  function makeCircle(left, top) {
    var c = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: 5,
      radius: 12,
      fill: '#fff',
      stroke: '#000',
      hasBorders: false,
      hasControls: false,
      class: 'node',
      component: main
    });
    
    // give the node an identifier and increment it for the next node
    c.set({'id': id});
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');

    // these are the channels that are connected to this node
    c.linesIn = [];
    c.linesOut = [];

    return c;
  } //makeCircle

  function drawLine(x1, y1, x2, y2) {
    // create a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'line'
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
      hasControls: false,
      selectable: false,
      hoverCursor: 'default'
    });
    
    // ...and two circles
    var c1 = makeCircle(x1,y1);
    var c2 = makeCircle(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    c1.linesOut.push(line);
    c2.linesIn.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateLine(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,c1,c2);
    canvas.renderAll();
    
    return line;
  } //drawLine
  
  function updateNode(node) {
    if (node.linesIn.length) {
      if (node.linesOut.length)
        node.set({'nodetype':'mixed','fill':'#ff5'});
      else
        node.set({'nodetype':'drain','fill':'#f55'});
    }
    else
      node.set({'nodetype':'source','fill':'#55f'});
  }
  
  function updateLine(line, end) {
    // we first have to reset the end coordinates
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

    return ((angle * 180 / Math.PI) + 90) % 360;
  } //calcArrowAngle
  
  function enumerate() {
    document.getElementById('text').innerHTML = "";
    canvas.forEachObject(function(obj) {
      if (obj.class == 'node')
        document.getElementById('text').innerHTML += obj.id + " ";
    });
  }
  
  function updateText() {
    var s = '';
    // TODO: traverse id's
    canvas.forEachObject(function(obj) {
      if (obj.type == "line")
        s += 'sync(' + obj.circle1.id + ',' + obj.circle2.id + ') ';
    });
    document.getElementById('text').innerHTML = s;
  }
  
  function snapToComponent(node,comp) {
    var right = comp.left + comp.width;
    var bottom = comp.top + comp.height;
    if (node.left > right) // right side
      node.set({'left': right});
    if (node.left < comp.left) // left side
      node.set({'left': comp.left});
    if (node.top > bottom) // bottom side
      node.set({'top': bottom});
    if (node.top < comp.top) // top side
      node.set({'top': comp.top});
    node.setCoords();
    for (i = 0; i < node.linesIn.length; i++) {
      updateLine(node.linesIn[i], 2);
    }
    for (i = 0; i < node.linesOut.length; i++) {
      updateLine(node.linesOut[i], 1);
    }
  }
  
  function snapOutComponent(node, comp, connectednode) {
    var right = comp.left + comp.width;
    var bottom = comp.top + comp.height;
    if (connectednode.left > right) // right side
      node.set({left: right});
    if (connectednode.left < comp.left) // left side
      node.set({left: comp.left});
    if (connectednode.top > bottom) // bottom side
      node.set({top: bottom});
    if (connectednode.top < comp.top)
      node.set({top: comp.top});
    node.setCoords();
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
    if (mode == 'sync') {
      var line = drawLine(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateLine(line,2);
    }
    if (mode == 'component') {
      origX = pointer.x;
      origY = pointer.y;
      var comp = drawComponent(pointer.x,pointer.y,pointer.x,pointer.y);
      canvas.setActiveObject(comp);
    }
  }); //mouse:down
  
  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var p = canvas.getActiveObject();
    if (!p)
      return;
    var pointer = canvas.getPointer(e.e);
    if (p.class == 'component') {
      if (origX > pointer.x)
        p.set({left:pointer.x});
      if (origY > pointer.y)
        p.set({top:pointer.y});
      p.set({width:Math.abs(origX - pointer.x)});
      p.set({height:Math.abs(origY - pointer.y)});
      p.setCoords();
      p.label.set({left: p.left + (p.width/2), top: p.top - 15});
      p.label.setCoords();
    }
    if (p.class == 'node') {
      p.set({'left': pointer.x, 'top': pointer.y});
      p.setCoords();
      canvas.forEachObject(function(obj) {
        if (obj !== p && obj.get('type') === "circle" && p.intersectsWithObject(obj)) {
          if(Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
            p.set({'left': obj.getLeft(), 'top': obj.getTop()});
            p.setCoords();
          }
        }
      });
      for (i = 0; i < p.linesIn.length; i++)
        updateLine(p.linesIn[i], 2);
      for (i = 0; i < p.linesOut.length; i++)
        updateLine(p.linesOut[i], 1);
    }
    canvas.renderAll();
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p) {
      if (p.class == 'node') {
        p.setCoords();
        p.set({component:main});
        canvas.forEachObject(function(obj) {
          if (!obj || obj.get('id') == p.get('id') || (obj.get('class') !== 'node' && obj.get('class') !== 'component'))
            return;
          if (p.intersectsWithObject(obj)) {
            if (obj.get('class') == 'node') {
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
                updateNode(obj);
                obj.bringToFront();
                canvas.renderAll();
              }
            }
            if (obj.get('class') == 'component') {
              if (obj.size < p.component.size) {
                p.component = obj;
              }
            }
          }
        });
        snapToComponent(p,p.component);
        for (j = 0; j < p.linesIn.length; j++) {
          if (p.linesIn[j].circle1.component.size < p.component.size)
            snapOutComponent(p.linesIn[j].circle1,p.linesIn[j].circle1.component,p);
          p.linesIn[j].circle1.component = p.component;
          snapToComponent(p.linesIn[j].circle1,p.component);
          updateLine(p.linesIn[j], 2);
        }
        for (k = 0; k < p.linesOut.length; k++) {
          if (p.linesOut[k].circle2.component.size < p.component.size)
            snapOutComponent(p.linesOut[k].circle2,p.linesOut[k].circle2.component,p);
          p.linesOut[k].circle2.component = p.component;
          snapToComponent(p.linesOut[k].circle2,p.component);
          updateLine(p.linesOut[k], 1);
        }
        canvas.deactivateAll();
        canvas.calcOffset();
      }
      if (p.class == 'component') {
        canvas.sendToBack(p);      
      }
    }
  });
  
  // Double-click event handler
  var doubleClick = function (obj, handler) {
    return function () {
      if (obj.clicked) handler(obj);
      else {
        obj.clicked = true;
        setTimeout(function () {
          obj.clicked = false;
        }, 500);
      }
    };
  };
  
  function drawComponent(x1,y1,x2,y2) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;
  
    var label = new fabric.IText('name', {
      left: left + (width / 2),
      top: top - 15,
      fontSize: 32
    });
  
    var rect = new fabric.Rect({
      left: left,
      top: top,
      width: width,
      height, height,
      size: width * height,
      fill: 'transparent',
      stroke: '#000',
      strokeWidth: 1,
      hoverCursor: 'default',
      originX: 'left',
      originY: 'top',
      label: label,
      hasBorders: false,
      hasControls: false,
      class: 'component'
    });
    
    // give the component an identifier and increment it for the next one
    rect.set({'id': id});
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');
  
    label.on('mousedown', doubleClick(label, function (obj) {
      label.enterEditing();
      label.selectAll();
    }));
  
    canvas.add(rect,label);
    rect.setCoords();
    canvas.renderAll();
    return rect;
  }
  
  var main = drawComponent(50,50,750,550);
  main.set({id:'main',hasBorders:false,hasControls:false,selectable:false});
  main.label.set({'text': 'main'});
  document.getElementById("select").click();
  drawLine(100,100,200,100);
  drawLine(300,100,400,100);

})();




