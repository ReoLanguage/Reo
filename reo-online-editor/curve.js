(function() {
  var canvas = this.__canvas = new fabric.Canvas('c');
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  
  var curve = new fabric.Path('M 300 300 c -100, -80, 100, -80, 0, 0', { fill: '', stroke: 'black', objectCaching: false });
  canvas.add(curve);

})();
