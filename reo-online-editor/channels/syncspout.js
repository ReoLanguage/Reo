function createSyncSpout(syncspout, x1, y1, x2, y2) {
  // create a channel...
  syncspout.name = 'syncspout';
  syncspout.end1 = 'sink';
  syncspout.end2 = 'sink';

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
  syncspout.components.push(line);

  // ...and an arrowhead
  let a1 = new fabric.Triangle({
    left: x1 + arrowOffsetOut,
    top: y2,
    width: arrowFactor * lineStrokeWidth,
    height: arrowFactor * lineStrokeWidth,
    baseAngle: 270,
    angle: 270,
    rotate: true,
    scale: false,
    fill: lineFillColour,
    hasBorders: false,
    hasControls: false,
    selectable: false,
    hoverCursor: 'default',
    referencePoint: 'node1',
    referenceDistance: arrowOffsetOut,
    referenceAngle: 90
  });
  syncspout.components.push(a1);

  // ...and an arrowhead
  let a2 = new fabric.Triangle({
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
  syncspout.components.push(a2);

  return syncspout;
} //createSyncSpout
