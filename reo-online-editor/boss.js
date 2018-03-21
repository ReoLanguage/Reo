var canvas = new fabric.Canvas('c');
var boss = new fabric.Line([0, 0, 100, 0], {
         stroke: 'red',
         strokeWidth: 5,
         originX: 'center',
         originY: 'center'
    });
var minion1 = new fabric.Triangle(
  { width: 10, height: 10, fill: 'blue' });

canvas.add(boss, minion1);

boss.on('moving', updateMinions);
boss.on('rotating', updateMinions);
boss.on('scaling', updateMinions);

var multiply = fabric.util.multiplyTransformMatrices;
var invert = fabric.util.invertTransform;

function updateMinions() {
  var minions = canvas.getObjects().filter(o => o !== boss);
  minions.forEach(o => {
    if (!o.relationship) {
      return;
    }
    var relationship = o.relationship;
    var newTransform = multiply(
      boss.calcTransformMatrix(),
      relationship
    );
    opt = fabric.util.qrDecompose(newTransform);
    o.set({
      flipX: false,
      flipY: false,
    });
    o.setPositionByOrigin(
      { x: opt.translateX, y: opt.translateY },
      'center',
      'center'
    );
    o.set(opt);
    o.setCoords();
  });
}

document.getElementById('bind').onclick = function() {
  var minions = canvas.getObjects().filter(o => o !== boss);
  var bossTransform = boss.calcTransformMatrix();
  var invertedBossTransform = invert(bossTransform);
  minions.forEach(o => {
    var desiredTransform = multiply(
      invertedBossTransform,
      o.calcTransformMatrix()
    );
    // save the desired relation here.
    o.relationship = desiredTransform;
  });
}