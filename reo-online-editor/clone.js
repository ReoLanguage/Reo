(function() {
  document.getElementById("copy").onclick    = function() {Copy()};
  document.getElementById("paste").onclick   = function() {Paste()};

  function Copy() {
    // clone what are you copying since you
    // may want copy and paste on different moment.
    // and you do not want the changes happened
    // later to reflect on the copy.
    _clipboard = new fabric.Rect();
    fabric.util.object.extend(_clipboard, canvas.getActiveObject());
    console.log(canvas.getActiveObject());
  }

  function Paste() {
    // clone again, so you can do multiple copies.
    clonedObj = new fabric.Rect()
    fabric.util.object.extend(clonedObj, _clipboard);
    canvas.discardActiveObject();
    clonedObj.set({
      left: clonedObj.left + 10,
      top: clonedObj.top + 10,
      evented: true,
    });
    if (clonedObj.type === 'activeSelection') {
      // active selection needs a reference to the canvas.
      clonedObj.canvas = canvas;
      clonedObj.forEachObject(function(obj) {
        canvas.add(obj);
      });
      // this should solve the unselectability
      clonedObj.setCoords();
    } else {
      canvas.add(clonedObj);
    }
    _clipboard.top += 10;
    _clipboard.left += 10;
    canvas.setActiveObject(clonedObj);
    canvas.requestRenderAll();
    console.log(clonedObj);
  }

  // Extend the default Fabric.js object type to include additional positional parameters
  fabric.Object.prototype.toObject = (function (toObject) {
    return function () {
      return fabric.util.object.extend(toObject.call(this), {
        referenceAngle:    this.referenceAngle,
        referenceDistance: this.referenceDistance,
        referencePoint:    this.referencePoint,
        rotate:            this.rotate,
        scale:             this.scale
      });
    };
  })(fabric.Object.prototype.toObject);

  var canvas = this.__canvas = new fabric.Canvas('c');
  fabric.Object.prototype.objectCaching = false;

  // create a rectangle object
  var rect = new fabric.Rect({
    left: 100,
    top: 50,
    fill: '#D81B60',
    width: 100,
    height: 100,
    strokeWidth: 2,
    stroke: "#880E4F",
    rx: 10,
    ry: 10,
    angle: 45,
    hasControls: true
  });

  canvas.add(rect);

  // create a rectangle object
  var rect2 = new fabric.Rect({
    left: 200,
    top: 50,
    fill: '#F06292',
    width: 100,
    height: 100,
    strokeWidth: 2,
    stroke: "#880E4F",
    rx: 10,
    ry: 10,
    angle: 45,
    hasControls: true
  });

  canvas.add(rect2);

  var circle1 = new fabric.Circle({
    radius: 65,
    fill: '#039BE5',
    left: 0
  });

  var circle2 = new fabric.Circle({
    radius: 65,
    fill: '#4FC3F7',
    left: 110,
    opacity: 0.7
  });

  var group = new fabric.Group([circle1, circle2, ], {
    left: 40,
    top: 250
  });

  canvas.add(group);

})();