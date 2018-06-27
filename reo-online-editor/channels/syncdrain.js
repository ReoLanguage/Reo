function createSyncDrain(syncdrain, x1, y1, x2, y2) {
  // create a channel...
  syncdrain.name = 'syncdrain';
  syncdrain.end1 = 'source';
  syncdrain.end2 = 'source';

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
  syncdrain.parts.push(line);

  // ...and an arrowhead
  let a1 = new fabric.Triangle({
    left: x1 + arrowOffsetIn,
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
    referencePoint: 'node1',
    referenceDistance: arrowOffsetIn,
    referenceAngle: 90
  });
  syncdrain.parts.push(a1);

  // ...and an arrowhead
  let a2 = new fabric.Triangle({
    left: x2 - arrowOffsetIn,
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
    referencePoint: 'node2',
    referenceDistance: arrowOffsetIn,
    referenceAngle: 270
  });
  syncdrain.parts.push(a2);

  return syncdrain;
} //createSyncDrain
