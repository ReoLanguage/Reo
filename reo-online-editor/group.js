(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: true });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';

  var circle1 = new fabric.Circle({
    left: 0,
    top: 0,
    strokeWidth: 5,
    radius: 12,
    fill: '#fff',
    stroke: '#000'
  });
  
  var circle2 = new fabric.Circle({
    left: 200,
    top: 0,
    strokeWidth: 5,
    radius: 12,
    fill: '#fff',
    stroke: '#000'
  });
  
  var line = new fabric.Line([0,0,177,0], {
    fill: '#000',
    stroke: '#000',
    strokeWidth: 5,
    lockScalingY: true
  });
  
  var arrow = new fabric.Triangle({
      left: 177,
      top: 0,
      width: 20,
      height: 20,
      angle: 90,
      fill: '#000'
    });
  
  var group1 = new fabric.Group([ line,circle1,circle2,arrow ], { left:400, top:300 });
  canvas.add(group1);

})();




