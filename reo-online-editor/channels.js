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
    selectable: false,
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
    selectable: false,
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
    selectable: false,
    hoverCursor: 'default',
    originX: 'center',
    originY: 'center',
  });
  syncdrain.components.push(line);

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
  syncdrain.components.push(a1);

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
  syncdrain.components.push(a2);

  return syncdrain;
} //createSyncDrain

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
    selectable: false,
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
    selectable: false,
    hoverCursor: 'default',
    originX: 'center',
    originY: 'center',
  });
  FIFO1.components.push(line);

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
  FIFO1.components.push(a);

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
    originX: 'center',
    originY: 'center'
  });
  FIFO1.components.push(rect);

  return FIFO1;
} //createFIFO1
