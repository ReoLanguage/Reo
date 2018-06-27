function createFIFO1(FIFO1, x1, y1, x2, y2) {
  // create a channel...
  FIFO1.name = 'fifo1';
  FIFO1.end1 = 'source';
  FIFO1.end2 = 'sink';

  let diffX = Math.abs(x1-x2);
  let diffY = Math.abs(y1-y2);

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
  FIFO1.parts.push(line);

  // ...an arrowhead...
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
  FIFO1.parts.push(a);

  // ...and a rectangle
  let rect = new fabric.Rect({
    left: Math.min(x1,x2) + diffX / 2,
    top: Math.min(y1,y2) + diffY / 2,
    width: fifoWidth,
    height: fifoHeight,
    baseAngle: 90,
    angle: 90,
    rotate: true,
    scale: false,
    fill: fifoFillColour,
    stroke: lineStrokeColour,
    strokeWidth: lineStrokeWidth,
    hasBorders: false,
    hasControls: false,
    selectable: false,
    hoverCursor: 'default',
    originX: 'center',
    originY: 'center',
    referencePoint: 'middle',
    referenceDistance: 0,
    referenceAngle: 0
  });
  FIFO1.parts.push(rect);

  return FIFO1;
} //createFIFO1
