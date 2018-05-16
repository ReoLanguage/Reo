function createLossySync(lossysync, x1, y1, x2, y2) {
  // create a channel...
  lossysync.name = 'lossysync';
  lossysync.end1 = 'source';
  lossysync.end2 = 'sink';

  // ...a line...
  let line = new fabric.Line([x1, y1, x2, y2], {
    fill: lineFillColour,
    stroke: lineStrokeColour,
    strokeWidth: lineStrokeWidth,
    strokeDashArray: [8,8],
    hasBorders: false,
    hasControls: false,
    evented: false,
    hoverCursor: 'default',
    originX: 'center',
    originY: 'center',
  });
  lossysync.components.push(line);

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
  lossysync.components.push(a);

  return lossysync;
} //createLossySync
