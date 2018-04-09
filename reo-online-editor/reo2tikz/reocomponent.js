function ReoComponent() {

}

ReoComponent.prototype.define = function (definestate, outp) {
  throw 'not defined';
};

ReoComponent.prototype.draw = function (outp) {
  throw 'not defined';
};

ReoComponent.prototype.genPath = function (waypoints) {
  let path = [];
  for (let wp of waypoints) {
    path.push('(@@)'.format(wp));
  }
  return path.join(' to ');
};

if (typeof module !== 'undefined') {
  module.exports = ReoComponent;
}