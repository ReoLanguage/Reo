(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  var active, isDown, origX, origY, origLeft, origTop;
  var mode = 'select';
  var id = '0';
  
  document.getElementById("select").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'select';
    this.style.border = '2px solid black';
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': true});
      }
    });
  };
  
  document.getElementById("component").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'component';
    this.style.border = '2px solid black';
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("sync").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'sync';
    this.style.border = '2px solid black';
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("lossysync").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'lossysync';
    this.style.border = '2px solid black';
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("syncdrain").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'syncdrain';
    this.style.border = '2px solid black';
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("syncspout").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'syncspout';
    this.style.border = '2px solid black';
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };
  
  document.getElementById("fifo1").onclick = function() {
    document.getElementById(mode).style.border = '2px solid white';
    mode = 'fifo1';
    this.style.border = '2px solid black';
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
  
  function generateId() {
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');
    return id;
  }

  function createNode(left, top) {
    var c = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: 5,
      radius: 12,
      fill: '#fff',
      stroke: '#000',
      hasControls: false,
      class: 'node',
      component: main,
      id: generateId()
    });

    // these are the channels that are connected to this node
    c.linesIn = [];
    c.linesOut = [];
    
    var label = new fabric.IText(c.id, {
      left: left + 20,
      top: top - 20,
      fontSize: 20,
      object: c,
      class: 'label',
      hasControls: false
      //visible: false
    });
    
    c.set({'label': label, 'labelOffsetX': 20, 'labelOffsetY': -20});
    canvas.add(label);
    
    label.on('editing:exited', function(e) {
      label.object.set({id: label.text});
    });

    return c;
  } //createNode

  function drawSync(x1, y1, x2, y2) {
    // create a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'channel',
      channel: 'sync'
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
      hoverCursor: 'default',
      class: 'arrow',
      offset: 22
    });
    
    // ...and two nodes
    var c1 = createNode(x1,y1);
    var c2 = createNode(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    c1.linesOut.push(line);
    c2.linesIn.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateChannel(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,c1,c2);
    canvas.renderAll();
    
    return line;
  } //drawSync
  
    function drawLossySync(x1, y1, x2, y2) {
    // create a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeDashArray: [10,5],
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'channel',
      channel: 'lossysync'
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
      hoverCursor: 'default',
      class: 'arrow',
      offset: 22
    });
    
    // ...and two nodes
    var c1 = createNode(x1,y1);
    var c2 = createNode(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    c1.linesOut.push(line);
    c2.linesIn.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateChannel(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,c1,c2);
    canvas.renderAll();
    
    return line;
  } //drawLossySync
  
  function drawSyncDrain(x1, y1, x2, y2) {
    // create a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'channel',
      channel: 'syncdrain'
    });
    
    // ...an arrowhead...
    var a = new fabric.Triangle({
      left: x2,
      top: y2,
      width: 20,
      height: 20,
      angle: calcArrowAngle(x1,y1,x2,y2) + 180,
      fill: '#000',
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'arrow',
      offset: 30
    });
    
    // ...another arrowhead...
    var b = new fabric.Triangle({
      left: x1,
      top: y1,
      width: 20,
      height: 20,
      angle: calcArrowAngle(x1,y1,x2,y2),
      fill: '#000',
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'arrow',
      offset: 30
    });
    
    // ...and two nodes
    var c1 = createNode(x1,y1);
    var c2 = createNode(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'arrow2': b, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    b.set({'line': line});
    c1.linesOut.push(line);
    c2.linesOut.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateChannel(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,b,c1,c2);
    canvas.renderAll();
    
    return line;
  } //drawSyncDrain
  
    function drawSyncSpout(x1, y1, x2, y2) {
    // create a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'channel',
      channel: 'syncspout'
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
      hoverCursor: 'default',
      class: 'arrow',
      offset: 22
    });
    
    // ...another arrowhead...
    var b = new fabric.Triangle({
      left: x1,
      top: y1,
      width: 20,
      height: 20,
      angle: calcArrowAngle(x1,y1,x2,y2) + 180,
      fill: '#000',
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'arrow',
      offset: 22
    });
    
    // ...and two nodes
    var c1 = createNode(x1,y1);
    var c2 = createNode(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'arrow2': b, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    b.set({'line': line});
    c1.linesIn.push(line);
    c2.linesIn.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateChannel(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,b,c1,c2);
    canvas.renderAll();
    
    return line;
  } //drawSyncSpout
  
  function drawFIFO1(x1, y1, x2, y2) {
    // create a line...
    var line = new fabric.Line([x1, y1, x2, y2], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      hoverCursor: 'default',
      class: 'channel',
      channel: 'FIFO1'
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
      hoverCursor: 'default',
      class: 'arrow',
      offset: 22
    });
    
    var rect = new fabric.Rect({
      left: (x1+x2)/2,
      top: (y1+y2)/2,
      width: 20,
      height: 20,
      fill: '#fff',
      stroke: '#000',
      strokeWidth: 5,
      hasBorders: false,
      hasControls: false,
      selectable: false,
      class: 'rect',
    });
    
    // ...and two nodes
    var c1 = createNode(x1,y1);
    var c2 = createNode(x2,y2);
    
    // ...link them all together
    line.set({'arrow': a, 'rect': rect, 'circle1': c1, 'circle2': c2});
    a.set({'line': line});
    rect.set({'line': line});
    c1.linesOut.push(line);
    c2.linesIn.push(line);
    updateNode(c1);
    updateNode(c2);
    
    // magic
    updateChannel(line, 2);
    
    // draw everything on the canvas
    canvas.add(line,a,rect,c1,c2);
    canvas.renderAll();
    
    return line;
  } //drawFIFO1  
  
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
  
  function updateChannel(line, end) {
    // we first have to reset the end coordinates
    var x1 = line.circle1.get('left');
    var y1 = line.circle1.get('top');
    var x2 = line.circle2.get('left');
    var y2 = line.circle2.get('top');
    line.set({'x1': x1, 'y1': y1, 'x2': x2, 'y2': y2});
    
    if (x1 == x2 && y1 == y2) {
      line.arrow.set({'left': line.get('x2'), 'top': line.get('y2')});
      //if (line.arrow2)
      //  line.arrow2.set({'left': line.get('x1'), 'top': line.get('y1')});
    }
    else {
      // calculate the position of the arrow
      var length = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
      if (line.channel != 'syncdrain')
        length = length - 22;
      var x = Math.atan(Math.abs(y1-y2)/Math.abs(x1-x2));
      if (end) {
        if (x2 > x1) {
          line.set({'x2': x1 + length * Math.cos(x)});
          line.arrow.set({'left': x2 - line.arrow.offset * Math.cos(x)});
          if (line.arrow2) {
            line.set({'x1': x2 - length * Math.cos(x)});
            line.arrow2.set({'left': x1 + line.arrow2.offset * Math.cos(x)});
          }
        } else {
          line.set({'x2': x1 - length * Math.cos(x)});
          line.arrow.set({'left': x2 + line.arrow.offset * Math.cos(x)});
          if (line.arrow2) {
            line.set({'x1': x2 + length * Math.cos(x)});
            line.arrow2.set({'left': x1 - line.arrow2.offset * Math.cos(x)});
          }
        }
        if (y2 > y1) {
          line.set({'y2': y1 + length * Math.sin(x)});
          line.arrow.set({'top': y2 - line.arrow.offset * Math.sin(x)});
          if (line.arrow2) {
            line.set({'y1': y2 - length * Math.sin(x)});
            line.arrow2.set({'top': y1 + line.arrow2.offset * Math.sin(x)});
          }
        } else {
          line.set({'y2': y1 - length * Math.sin(x)});
          line.arrow.set({'top': y2 + line.arrow.offset * Math.sin(x)});
          if (line.arrow2) {
            line.set({'y1': y2 + length * Math.sin(x)});
            line.arrow2.set({'top': y1 - line.arrow2.offset * Math.sin(x)});
          }
        }
      }
      var angle = calcArrowAngle(line.get('x1'), line.get('y1'), line.get('x2'), line.get('y2'));
      if (line.channel == 'syncdrain') {
        line.arrow.set({'angle': angle + 180});
        line.arrow2.set({'angle': angle});
      }
      else {
        line.arrow.set({'angle': angle});
        if (line.arrow2)
          line.arrow2.set({'angle': angle + 180});
      }
      if (line.rect) {
        line.rect.set({'left': (x1+x2)/2, 'top': (y1+y2)/2, 'angle': angle});
      }
    }
    
    canvas.renderAll();
  } //updateChannel
  
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
  
  function isBoundaryNode (node, component) {
    if (node.left == component.left ||
        node.top  == component.top  ||
        node.left == component.left + component.width ||
        node.top  == component.top  + component.height)
    {
      return true;
    }
    return false;
  }
  
  function updateText() {
    if (main) {
      var s1 = main.label.text + '(';
      var s2 = '';
      var space1 = space2 = '';
      
      canvas.forEachObject(function(obj) {
        if (obj.class == 'node') {
          //console.log('node ' + obj.id + ' in component ' + obj.component.id);
          if (obj.component.id == 'main' && isBoundaryNode(obj,obj.component)) {
            s1 += space1 + obj.label.text;
            space1 = ',';
            
          }
        }
        if (obj.class == 'channel') {
          if (obj.circle1.component === main ||
              obj.circle2.component === main ||
               (isBoundaryNode(obj.circle1,obj.circle1.component) &&
                isBoundaryNode(obj.circle2,obj.circle2.component) &&
                obj.circle1.component != obj.circle2.component
               )
             )
          {
            s2 += space2 + obj.channel + '(' + obj.circle1.label.text + ',' + obj.circle2.label.text + ')';
            space2 = ' ';
          }
        }
        if (obj.class == 'component' && obj != main) {
          var s3 = obj.label.text + '(';
          var space3 = '';
          canvas.forEachObject(function(obj2) {
            if (obj2.class == 'node' && obj2.component === obj && isBoundaryNode(obj2, obj2.component)) {
              s3 += space3 + obj2.label.text;
              space3 = ',';
            }
          });
          s3 += ')';
          s2 += space2 + s3;
          space2 = ' ';
        }
      });
      
      s1 = s1 + ') {\n  ' + s2 + '\n}';
      document.getElementById('text').innerHTML = s1;
    }
  }
  
  function snapToComponent(node, comp) {
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
    node.label.set({'left': node.left + node.labelOffsetX, 'top': node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.linesIn.length; i++) {
      updateChannel(node.linesIn[i], 2);
    }
    for (i = 0; i < node.linesOut.length; i++) {
      updateChannel(node.linesOut[i], 1);
    }
    updateText();
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
    if (connectednode.top < comp.top) // top side
      node.set({top: comp.top});
    node.setCoords();
    node.label.set({'left': node.left + node.labelOffsetX, 'top': node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.linesIn.length; i++) {
      updateChannel(node.linesIn[i], 2);
    }
    for (i = 0; i < node.linesOut.length; i++) {
      updateChannel(node.linesOut[i], 1);
    }
    updateText();
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
  }); //object:removed
  
  canvas.on('text:changed', function(e) {
    updateText();
  }); //text:editing:exited
  
  function copy(obj) {
    var obj2 = obj.clone();
    if (obj.class == 'component') {
      obj2.set({
        'size': obj.size,
        'class': obj.class,
        'status': obj.status,
        'id': obj.id,
        'label': obj.label
      });
      obj2.label.object = obj2;
    }
    if (obj.class == 'node') {
      obj2.set({
        'class': obj.class,
        'component': obj.component,
        'id': obj.id,
        'label': obj.label,
        'linesIn': obj.linesIn,
        'linesOut': obj.linesOut
      });
      obj2.label.object = obj2;
      for (i = 0; i < obj.linesIn.length; i++) {
        obj.linesIn[i].c2 = obj2;
      }
      for (i = 0; i < obj.linesOut.length; i++) {
        obj.linesOut[i].c1 = obj2;
      }
    }
    if (obj.class == 'channel') {
      obj2.set({
        'class': obj.class,
        'channel': obj.channel,
        'arrow': obj.arrow,
        'circle1': obj.circle1,
        'circle2': obj.circle2
      });
      obj.arrow.line = obj2;
      obj.circle1.line = obj2;
      obj.circle2.line = obj2;
    }
    if (obj.class == 'label') {
      obj2.set({
        'class': obj.class,
        'object': obj.object
      });
      obj.object.label = obj2;
    }
    return obj2;
  }
  
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
      console.log('Mode is select');
      if (p && p.class == 'component') {
        console.log('p is ' + p.type + ' ' + p.id);
        console.log('Creating a group');
        var p2 = copy(p);
        var group = new fabric.Group([ p2 ], {
          left: p.left,
          top: p.top,
          label: p.label,
          labelOffsetX: p.labelOffsetX,
          labelOffsetY: p.labelOffsetY,
          originX: 'left',
          originY: 'top',
          class: 'group'
        });
        p.label.set({'object': group});
        // Temporarily disabled because it is buggy
        /*canvas.forEachObject(function(obj) {
          //console.log(obj.id);
          console.log('Checking object type ' + obj.type + ' with class ' + obj.class + ' and id ' + obj.id);
          if(obj.type == 'i-text')
            console.log(' references ' + obj.object.id);
          if ((obj.class != 'component' && obj.component == p) || (obj.class == 'label' && obj.object.component == p))
          {
            var obj2 = copy(obj);
            group.addWithUpdate(obj2);
            canvas.remove(obj);
          }
        });*/
        canvas.remove(p);
        canvas.renderAll();
        canvas.add(group);
        canvas.setActiveObject(group);
        origLeft = group.left;
        origTop = group.top;
      }
    }
    if (mode == 'component') {
      canvas.deactivateAll();
      var comp = drawComponent(pointer.x,pointer.y,pointer.x,pointer.y);
      canvas.setActiveObject(comp);
    }
    if (mode == 'sync') {
      canvas.deactivateAll();
      var line = drawSync(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateChannel(line,2);      
    }
    if (mode == 'lossysync') {
      canvas.deactivateAll();
      var line = drawLossySync(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateChannel(line,2);      
    }
    if (mode == 'syncdrain') {
      canvas.deactivateAll();
      var line = drawSyncDrain(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateChannel(line,2);      
    }
    if (mode == 'syncspout') {
      canvas.deactivateAll();
      var line = drawSyncSpout(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateChannel(line,2);      
    }
    if (mode == 'fifo1') {
      canvas.deactivateAll();
      var line = drawFIFO1(pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(line.circle1,main);
      canvas.setActiveObject(line.circle2);
      updateChannel(line,2);      
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
      if (p.status == 'drawing') {
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
    }
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
          else if (obj.class === 'component') {
            if (Math.abs(p.left - obj.left) < 10)
              p.set({'left': obj.left});
            if (Math.abs(p.top - obj.top) < 10)
              p.set({'top': obj.top});
            if (Math.abs(p.left - (obj.left + obj.width)) < 10)
              p.set({'left': obj.left + obj.width});
            if (Math.abs(p.top - (obj.top + obj.height)) < 10)
              p.set({'top': obj.top + obj.height});
            p.setCoords();
          }
        }
      });
      for (i = 0; i < p.linesIn.length; i++)
        updateChannel(p.linesIn[i], 2);
      for (i = 0; i < p.linesOut.length; i++)
        updateChannel(p.linesOut[i], 1);
      p.label.set({left: p.left + p.labelOffsetX});
      p.label.set({top: p.top + p.labelOffsetY});
      p.label.setCoords();
    }
    if (p.class == 'group') {
      p.set({left: origLeft + pointer.x - origX});
      p.set({top: origTop + pointer.y - origY});
      p.setCoords();
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
        p.set({'component': main});
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
                canvas.remove(p.label, p);
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
          else {
            p.linesIn[j].circle1.component = p.component;
            snapToComponent(p.linesIn[j].circle1,p.component);
          }
          updateChannel(p.linesIn[j], 2);
        }
        for (k = 0; k < p.linesOut.length; k++) {
          if (p.linesOut[k].circle2.component.size < p.component.size)
            snapOutComponent(p.linesOut[k].circle2,p.linesOut[k].circle2.component,p);
          else {
            p.linesOut[k].circle2.component = p.component;
            snapToComponent(p.linesOut[k].circle2,p.component);
          }
          updateChannel(p.linesOut[k], 1);
        }
        canvas.calcOffset();
        canvas.deactivateAll();
      }
      if (p.class == 'component') {
        p.label.setCoords();
        reorderComponents(p);
        p.set({'labelOffsetX': p.label.left - p.left, 'labelOffsetY': p.label.top - p.top, status: 'design'});
        canvas.deactivateAll();
      }
      if (p.class == 'label') {
        p.setCoords();
        p.object.set({'labelOffsetX': p.left - p.object.left, 'labelOffsetY': p.top - p.object.top});    
      }
      if (p.class == 'group') {
        var items = p._objects;
        p._restoreObjectsState();
        canvas.remove(p);
        var comp = items[0];
        comp.set({'labelOffsetX': p.labelOffsetX, 'labelOffsetY': p.labelOffsetY});
        canvas.add(comp);
        console.log("comp is");
        console.log(comp);
        for (var i = 1; i < items.length; i++) {
          items[i].set({'component': comp});
          canvas.add(items[i]);
        }
        canvas.renderAll();
      }
    }
  }); //mouse:up
  
  /* Reorders the components so that all components are behind the other elements and p is in front of the other components */
  function reorderComponents(p) {
    canvas.sendToBack(p.label);
    canvas.sendToBack(p);
    canvas.forEachObject(function(obj) {
      if (obj.class == 'component' && obj != p) {
        canvas.sendToBack(obj.label);
        canvas.sendToBack(obj);
      }
    });
  }
  
  function drawComponent(x1,y1,x2,y2) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;
  
    var rect = new fabric.Rect({
      left: left,
      top: top,
      width: width,
      height: height,
      fill: '#fff',
      stroke: '#000',
      strokeWidth: 1,
      hoverCursor: 'default',
      originX: 'left',
      originY: 'top',
      //hasBorders: false,
      //hasControls: false,
      size: width * height,
      class: 'component',
      status: 'drawing',
      id: generateId()
    });
    
    var label = new fabric.IText('name', {
      left: left + (width / 2),
      top: top - 15,
      fontSize: 32,
      class: 'label',
      object: rect,
      hasControls: false
    });
    
    rect.set({'label': label, 'labelOffsetX': left + (width / 2), 'labelOffsetY': -15});
  
    rect.setCoords();
    canvas.add(rect,label);
    canvas.renderAll();
    return rect;
  }
  
  var main = drawComponent(50,50,750,550);
  main.set({id: 'main', fill: 'transparent', hasBorders: false, hasControls: false, evented: false});
  main.label.set({'text': 'main'});
  id = '0';
  document.getElementById("select").click();
  drawSync(100,100,200,100);
})();