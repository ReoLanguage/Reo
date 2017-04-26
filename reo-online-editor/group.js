(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  var active, isDown, origX, origY, origLeft, origTop;
  var mode = 'select';
  var id = '0';
  
  fabric.Node = fabric.util.createClass(fabric.Circle, {
    type: 'node',
    class: 'node',
    component: '',
    id: '',
    
    initialize: function (options) {
      options = options || {};
      this.callSuper('initialize', options);
    },
    
    _render: function (ctx) {
      this.callSuper('_render', ctx);
    }
  });

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

  var c = new fabric.Node({
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
    console.log('origX: ' + origX + ' origY: ' + origY);
    var p = canvas.getActiveObject();
    if (p)
      console.log(p);
    if (p && p.class == 'component') {
      var group = new fabric.Group([ p.clone() ], {
        left: p.left,
        top: p.top,
        originX: 'left',
        originY: 'top'
      });
      canvas.forEachObject(function(obj) {
        if (obj.class != 'component' && obj.component == p) {
          group.addWithUpdate(obj.clone());
          canvas.remove(obj);
        }
      });
      canvas.remove(p);
      canvas.renderAll();
      canvas.add(group);
      canvas.setActiveObject(group);
      origLeft = group.left;
      origTop = group.top;
      console.log('origLeft: ' + origLeft + ' origTop: ' + origTop);
    }
  }); //mouse:down
  
  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var p = canvas.getActiveObject();
    if (!p)
      return;
    var pointer = canvas.getPointer(e.e);
    if (p.type == 'group') {
      p.set({left: origLeft + pointer.x - origX});
      p.set({top: origTop + pointer.y - origY});
      p.setCoords();
      canvas.renderAll();
    }
  }); //mouse:move
  
  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p && p.type == 'group') {
      var items = p._objects;
      p._restoreObjectsState();
      canvas.remove(p);
      for (var i = 0; i < items.length; i++) {
        canvas.add(items[i]);
      }
      canvas.renderAll();
    }
  }); //mouse:down

})();