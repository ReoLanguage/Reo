function ReoComponentImplementation(network, cName, implName, argsIn, argsOut) {
  this.network = network;
  this.cName = cName;
  this.implName = implName;
  this.argsIn = argsIn;
  this.argsOut = argsOut;
  this.isIO = {};
  this.nodes = {};
  this.innerComponents = [];
  this.waypoints = {};
  this.generatedWaypointCount = 0;
  this.dependencies = {};
  this.drawNodeSpacing = 1;
  this.labels = {};
}

ReoComponentImplementation.prototype.genWaypointName = function () {
  let wpname = this.implName + 'waypoint' + String.fromCharCode(97 + this.generatedWaypointCount);
  this.generatedWaypointCount++;
  return wpname;
};

ReoComponentImplementation.prototype.genNodeName = function (ident, env) {
  let r = /^(\w+)\[([^\]]+)\]$/g;
  let m = r.exec(ident);
  let wpname = this.implName + ident;
  if (m) {
    wpname = this.implName + m[1] + parseNumber(m[2], env);
  }
  return wpname;
};

ReoComponentImplementation.prototype.scale = function (sx, sy) {
  for (let n in this.waypoints) {
    this.waypoints[n][0] *= sx;
    this.waypoints[n][1] *= sy;
  }

  for (let cidx = 0; cidx < this.innerComponents.length; cidx++) {
    let comp = this.innerComponents[cidx];
    if (comp.pos) {
      comp.pos[0] *= sx;
      comp.pos[1] *= sy;
    }
  }

  if (this.bound) {
    this.bound[0][0] *= sx;
    this.bound[0][1] *= sy;
    this.bound[1][0] *= sx;
    this.bound[1][1] *= sy;
  }
};

ReoComponentImplementation.prototype.normalizePositions = function () {
  // normalize coords
  let nearestNodes = 100000;
  let nearestInCm = this.drawNodeSpacing;
  let nds = this.nodes;
  for (let n1 in nds) {
    for (let n2 in nds) {
      if (n1 != n2) {
        let dist = Math.abs(nds[n1][0] - nds[n2][0]) + Math.abs(nds[n1][1] - nds[n2][1]);
        if (nearestNodes > dist && dist > .001) {
          nearestNodes = dist;
        }
      }
    }
  }

  // knowing the nearest pair of nodes, rescale all waypoints
  this.scale(nearestInCm / nearestNodes, -nearestInCm / nearestNodes);
};

/**
 * If position of a component isn't yet known (not in meta), derive it from paths
 */
ReoComponentImplementation.prototype.inferMissingMeta = function () {
  for (let cidx = 0; cidx < this.innerComponents.length; cidx++) {
    let comp = this.innerComponents[cidx];
    if (!comp.pos) {
      if (comp.waypointsToPortIndex.length > 0) {
        let cur = [0, 0];
        for (let aidx = 0; aidx < comp.waypointsToPortIndex.length; aidx++) {
          let realwaypoint = this.waypoints[comp.waypointsToPortIndex[aidx][0]];
          if (!realwaypoint) {
            throw "does not exist " + comp.waypointsToPortIndex[aidx][0];
          }
          cur = [cur[0] + realwaypoint[0], cur[1] + realwaypoint[1]];
        }
        comp.pos = [cur[0] / comp.waypointsToPortIndex.length, cur[1] / comp.waypointsToPortIndex.length];
      } else {
        comp.pos = [0, 0];
      }
    }
    // angle unknown? determine it from two waypoints, otherwise set to 0
    if (!comp.hasOwnProperty('angle')) {
      let angle = 0;
      if (comp.waypointsToPortIndex.length == 2) {
        let wp1 = this.waypoints[comp.waypointsToPortIndex[0][0]];
        let wp2 = this.waypoints[comp.waypointsToPortIndex[1][0]];
        let kpw = [wp1[0] - wp2[0], wp1[1] - wp2[1]];
        angle = (180 - Math.atan2(kpw[1], kpw[0]) * (180 / Math.PI)) % 360;
      }
      comp.angle = angle;
    }
  }
};

ReoComponentImplementation.prototype.inferBound = function () {
  this.bound = [
    [10000, 10000], [-10000, -10000]
  ];
  for (let i in this.nodes) {
    let np = this.nodes[i];
    this.bound[0][0] = Math.min(this.bound[0][0], np[0]);
    this.bound[0][1] = Math.min(this.bound[0][1], np[1]);
    this.bound[1][0] = Math.max(this.bound[1][0], np[0]);
    this.bound[1][1] = Math.max(this.bound[1][1], np[1]);
  }
  let postPush = [[-this.network.cfgBoundInferPush, -this.network.cfgBoundInferPush], [this.network.cfgBoundInferPush, this.network.cfgBoundInferPush]];
  let ioComp = {};
  for (let i in this.nodes) {
    if (this.isIO[i]) {
      // determine where the connected components are relatively
      let connVec = [0, 0];
      let np = this.nodes[i];
      for (let innerComponent of this.innerComponents) {
        let isIOUsedComp = innerComponent.argsIn.indexOf(i) >= 0 || innerComponent.argsOut.indexOf(i) >= 0;
        if (isIOUsedComp) {
          connVec[0] += innerComponent.pos[0] - np[0];
          connVec[1] += innerComponent.pos[1] - np[1];
        }
      }

      // remove push based on angle
      let angle = (Math.atan2(connVec[1], connVec[0]) + Math.PI * 2) % (Math.PI * 2);
      if (angle <= Math.PI * 0.25) postPush[1][0] = 0;
      else if (angle <= Math.PI * 0.75) postPush[0][1] = 0;
      else if (angle <= Math.PI * 1.25) postPush[0][0] = 0;
      else if (angle <= Math.PI * 1.75) postPush[1][1] = 0;
      else postPush[1][0] = 0;
    }
  }

  this.bound[0][0] += postPush[0][0];
  this.bound[0][1] += postPush[0][1];
  this.bound[1][0] += postPush[1][0];
  this.bound[1][1] += postPush[1][1];
};

ReoComponentImplementation.prototype.shift = function (offset) {
  // shift waypoints (incl nodes), used components and the bound
  for (let wp in this.waypoints) {
    this.waypoints[wp][0] += offset[0];
    this.waypoints[wp][1] += offset[1];
  }
  for (let ci in this.innerComponents) {
    this.innerComponents[ci].pos[0] += offset[0];
    this.innerComponents[ci].pos[1] += offset[1];
  }
  this.bound[0][0] += offset[0];
  this.bound[0][1] += offset[1];
  this.bound[1][0] += offset[0];
  this.bound[1][1] += offset[1];
};

ReoComponentImplementation.prototype.define = function (drawstate) {
  let output = '';
  // Define used components
  for (let cidx = 0; cidx < this.innerComponents.length; cidx++) {
    let comp = this.innerComponents[cidx];
    if (!drawstate[comp.typeName]) {
      drawstate[comp.typeName] = true;
      output += comp.define(drawstate);
    }
  }

  // output += '\\def \\reoimpldraw@@ !!{\n'.format(this.implName);
  output += 'function reoimpldraw@@() {\n'.format(this.implName);
  // output += `let ${this.implName} = createComponent(50,50,750,550);\n${this.implName}.label.set({'text': '${this.implName}'});\nid = '0';\n`;

  // Nodes
  // for (let n in this.waypoints) {
  //   if (this.nodes[n]) {
  //     output += ('  \\node[gnode=@@] (@@) at (@@,@@) {};\n'.format(this.labels[n] || '', n, this.waypoints[n][0], this.waypoints[n][1]));
  //   } else {
  //     output += ('  \\coordinate (@@) at (@@,@@) {};\n'.format(n, this.waypoints[n][0], this.waypoints[n][1]));
  //   }
  // }

  // Components
  for (let cidx = 0; cidx < this.innerComponents.length; cidx++) {
    let comp = this.innerComponents[cidx];
    output += comp.draw(this.nodes);
  }

  output += '}\n';
  return output
};

ReoComponentImplementation.prototype.processMeta = async function (s, env) {
  switch (s.key) {
    case 'pos':
      // let wpname = this.genNodeName(s.keyarg, env);
      let wpname = s.keyarg;
      let coord = parseNumberArray(s.value, env);
      this.nodes[wpname] = coord;
      this.waypoints[wpname] = coord;
      break;
    case 'bound':
      this.bound = [parseNumberArray(s.value[0], env), parseNumberArray(s.value[1], env)];
      break;
    case 'spacing':
      this.drawNodeSpacing = s.value;
      break;
    case 'label':
      this.labels[this.genNodeName(s.keyarg, env)] = s.value;
      break;
    default:
      await this.network.processMeta(s, env);
  }
};

ReoComponentImplementation.prototype.parseInnerStr = async function (str, env) {
  // peek for next keyword
  let peekRg = /^\s*(\w+)*/g;
  let peekM = peekRg.exec(str);
  if (!peekM)
    return;

  if (peekM[1] === 'for') {
    let p = /^\s*for\s+(\w+)\s*=\s*(.+?)\s*\.\.\s*(.+?)\s*{(({(({(({.*?}|.)*?)}|.)*?)}|.)*?)}\s*/g;
    let m = p.exec(str);

    if (!m) {
      return;
    }

    let matchStr = m[0];
    let itrSymbol = m[1];
    let boundL = parseNumber(m[2], env);
    let boundR = parseNumber(m[3], env);
    let innerScopeStr = m[4];

    for (let itr = parseInt(boundL); itr <= parseInt(boundR); itr++) {
      let innerEnv = JSON.parse(JSON.stringify(env));
      innerEnv[itrSymbol] = itr;
      await this.parseInnerStr(innerScopeStr, innerEnv);
    }

    let nextStr = str.substring(matchStr.length);
    await this.parseInnerStr(nextStr, env);
  } else {
    // Parse component def
    let p = /^\s*(\w+)(?:<([^>]*)>)?\(([^;\)]*)(;([^;\)]+))?\)\s*/g; // compname<templargs, >(inargs, , , ; outargs opt, , ,)
    let m = p.exec(str);
    if (!m) {
      return;
    }

    // Get all data from the match
    let matchStr = m[0];
    let cName = m[1];
    let argsTempl = (m[2] || '').split(',').map(function (x) {return x.trim()}).filter(function (x) {return x.length > 0});
    let argsIn = m[3].split(',').map(function (x) {return x.trim()}).filter(function (x) {return x.length > 0});
    let argsOut = (m[5] || '').split(',').map(function (x) {return x.trim()}).filter(function (x) {return x.length > 0});

    let self = this;
    argsTempl = argsTempl.map(function (x) {return parseNumber(x, env)});
    let impl = await this.network.getImplementationFor(cName, argsTempl);
    let usedComponent = new impl.impl();

    // prefix args with [cName] to ensure unique names
    // argsIn = argsIn.map(function (x) {return self.genNodeName(x, env)});
    // argsOut = argsOut.map(function (x) {return self.genNodeName(x, env)});

    usedComponent.argsIn = argsIn;
    usedComponent.argsOut = argsOut;
    usedComponent.waypointsToPortIndex = [];
    usedComponent.waypoints = [];

    let allArgs = usedComponent.argsIn.concat(usedComponent.argsOut);
    // insert primary waypoints (may be modified by metadata below)
    for (let i = 0; i < allArgs.length; i++) {
      usedComponent.waypointsToPortIndex.push([allArgs[i]]);
    }


    let nextStr = str.substring(matchStr.length);
    if (nextStr.substring(0, 3) === '/*!') { // Component metadata
      let res = this.network.parseMeta(nextStr);
      nextStr = res[0];
      let mdata = res[1];
      for (let s of mdata) {
        switch (s.key) {
          case 'pathto':
            for (let i = s.value.length - 1; i >= 0; i--) {
              let wpcoord = s.value[i];
              // name is [cName] + waypoint + [unique index]
              let wpname = this.genWaypointName();
              this.generatedWaypointCount += 1;
              this.waypoints[wpname] = wpcoord;
              usedComponent.waypointsToPortIndex[parseInt(s.keyarg) - 1].unshift(wpname);
            }
            break;
          case 'value':
            usedComponent.value = s.value;
            break;
          case 'angle':
            usedComponent.angle = parseNumber(s.value, env);
            break;
          case 'pos':
            if (!s.keyarg) {
              // node pos
              /*let wpname = this.genNodeName(s.keyarg, env);
              let coord = parseNumberArray(s.value, env);
              this.nodes[wpname] = coord;
              this.waypoints[wpname] = coord;*/
              // component pos
              usedComponent.pos = parseNumberArray(s.value, env);
              break;
            }
            // else: fall through
          default:
            await this.processMeta(s, env);
        }
      }
    }

    this.innerComponents.push(usedComponent);

    await this.parseInnerStr(nextStr, env);
  }
};

if (typeof module !== 'undefined') {
  module.exports = ReoComponentImplementation;
}