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
  });
  
  canvas.add(rect);

  var c = new fabric.Circle({
    left: 200,
    top: 200,
    strokeWidth: 5,
    radius: 12,
    fill: '#fff',
    stroke: '#000',
    hasControls: false
  });
   
  canvas.add(c);
  
  var group = new fabric.Group([ rect.clone(), c.clone() ]);
  
  canvas.remove(rect,c);
  
  canvas.add(group);

})();