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
  
  function generateId() {
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');
    return id;
  }
  
  function updateText() {
    if (main) {
      var s1 = main.label.text + '(';
      var s2 = '';
      var space1 = space2 = '';
      
      canvas.forEachObject(function(obj) {
        if (obj.class == 'node') {
          if (obj.left == main.left || obj.top == main.top || obj.left == main.left + main.width || obj.top == main.top + main.height) {
            s1 += space1 + obj.label.text;
            space1 = ', '
            
          }
        }
        if (obj.class == 'channel')
          s2 += space2 + 'sync(' + obj.circle1.label.text + ',' + obj.circle2.label.text + ')';
          space2 = ' ';
      });
      
      s1 = s1 + ') {\n  ' + s2 + '\n}';
      document.getElementById('text').innerHTML = s1;
    }
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
  
  canvas.on('mouse:down', function(e) {
    isDown = true;
    var pointer = canvas.getPointer(e.e);
    origX = pointer.x;
    origY = pointer.y;
    var p = canvas.getActiveObject();
    console.log(p);
    if (p) {
      origLeft = p.left;
      origTop = p.top;
      return;
    }
    if (mode == 'component') {
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
      if (p.status == 'design') {
        p.set({left: origLeft + pointer.x - origX});
        p.set({top: origTop + pointer.y - origY});
        p.setCoords();
        p.label.set({left: p.left + p.labelOffsetX});
        p.label.set({top: p.top + p.labelOffsetY});
        p.label.setCoords();
      }
    }
    canvas.renderAll();
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p) {
      p.setCoords();
      if (p.class == 'component') {
        p.label.setCoords();
        p.set({'labelOffsetX': p.label.left - p.left, 'labelOffsetY': p.label.top - p.top, status: 'design'});    
      }
      if (p.class == 'label') {
        p.object.set({'labelOffsetX': p.left - p.object.left, 'labelOffsetY': p.top - p.object.top});    
      }
    }
  });
  
  function drawComponent(x1,y1,x2,y2) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;
  
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
      //hasBorders: false,
      //hasControls: false,
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
  main.set({id: 'main'});
  main.label.set({'text': 'main'});
  id = '0';
  document.getElementById("component").click();

})();