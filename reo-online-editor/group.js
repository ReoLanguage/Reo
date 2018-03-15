(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  var active, isDown, origX, origY, origLeft, origTop;
  var mode = 'select';
  var id = '0';

 var rect = new fabric.Rect({
    left: 100,
    top: 100,
    width: 300,
    height: 300,
    fill: 'transparent',
    stroke: '#000',
    strokeWidth: 1,
    hoverCursor: 'default',
    originX: 'left',
    originY: 'top',
    //hasBorders: false,
    //hasControls: false,
    class: 'component',
    id: 'rect'
  });
  
  canvas.add(rect);

  var c = new fabric.Circle({
    left: 200,
    top: 200,
    strokeWidth: 5,
    radius: 12,
    fill: '#fff',
    stroke: '#000',
    hasControls: false,
    class: 'node',
    component: rect,
    id: 'c'
  });
   
  canvas.add(c);
  
  canvas.on('mouse:down', function(e) {
    isDown = true;
    var pointer = canvas.getPointer(e.e);
    origX = pointer.x;
    origY = pointer.y;
    var p = canvas.getActiveObject();
    if (p)
      console.log(p);
    if (p && p.class == 'component') {
      var copy = p.clone();
      copy.set({
        'class': p.class,
        'id': p.id
      });
      var group = new fabric.Group([ copy ], {
        left: p.left,
        top: p.top,
        originX: 'left',
        originY: 'top',
        class: 'group'
      });
      canvas.forEachObject(function(obj) {
        if (obj.class != 'component' && obj.component == p) {
          var copy = obj.clone();
          copy.set({
            'class': obj.class,
            'component': obj.component,
            'id': obj.id  
          });
          group.addWithUpdate(copy);
          canvas.remove(obj);
        }
      });
      canvas.remove(p);
      canvas.renderAll();
      canvas.add(group);
      canvas.setActiveObject(group);
      origLeft = group.left;
      origTop = group.top;
    }
  }); //mouse:down
  
  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var p = canvas.getActiveObject();
    if (!p)
      return;
    var pointer = canvas.getPointer(e.e);
    if (p.class == 'group') {
      p.set({left: origLeft + pointer.x - origX});
      p.set({top: origTop + pointer.y - origY});
      p.setCoords();
      canvas.renderAll();
    }
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p && p.class == 'group') {
      var items = p._objects;
      p._restoreObjectsState();
      canvas.remove(p);
      var comp = items[0];
      canvas.add(comp);
      for (var i = 1; i < items.length; i++) {
        items[i].set({'component': comp});
        canvas.add(items[i]);
      }
      canvas.renderAll();
    }
  }); //mouse:up

})();