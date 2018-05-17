function createSync(sync, x1, y1, x2, y2) {
  // create a channel...
  sync.name = 'sync';
  sync.end1 = 'source';
  sync.end2 = 'sink';

  // ...a line...
  let line = new fabric.Line([x1, y1, x2, y2], {
    fill: lineFillColour,
    stroke: lineStrokeColour,
    strokeWidth: lineStrokeWidth,
    hasBorders: false,
    hasControls: false,
    evented: false,
    hoverCursor: 'default',
    originX: 'center',
    originY: 'center',
  });
  sync.components.push(line);

  // ...and an arrowhead
  let a = new fabric.Triangle({
    left: x2 - arrowOffsetOut,
    top: y2,
    width: arrowFactor * lineStrokeWidth,
    height: arrowFactor * lineStrokeWidth,
    baseAngle: 90,
    angle: 90,
    rotate: true,
    scale: false,
    fill: lineFillColour,
    hasBorders: false,
    hasControls: false,
    selectable: false,
    hoverCursor: 'default',
    referencePoint: 'node2',
    referenceDistance: arrowOffsetOut,
    referenceAngle: 270
  });
  sync.components.push(a);

  return sync;
} //createSync