"use strict";

var Reo = Reo || {};

Reo.End = function(node, channel){
  // Reo channel end object.
  this.node = node;
  this.channel = channel;

  this.color = null;
};

Reo.Node = function(parent, id, coord, type){
  // Reo node object.
  this.id = id;
  this.parent = parent;

  this.coord = coord;
  this.type = type;

  this.sources = [];
  this.sinks = [];
};

Reo.Node.prototype = {
  constructor: Reo.Node.prototype.constructor,
  get ends(){
    return this.sources.concat(this.sinks);
  },
  get colorings(){
    if(this._colorings)
      return this._colorings;

    console.info("Generating colorings for " + this.type
                  + " node " + this.id + ".");
    switch(this.type){
      case "read":
      case "write":
      case "edge":
        // coloring of these nodes depends on I/O availability
        this._colorings = [];
      break;
      case "xrouter":
        this._colorings = this._getColoringsXrouter();
      break;
      default:
        this._colorings = this._getColoringsMerge();
    }

    return this._colorings;
  }
};

Reo.Node.prototype._getColoringsMerge = function(){
  // Returns coloring table for a merge node.
  var table = [];

  // flow from a source to all sink ends
  // use 'dummy source' to run loop even without sources (for component sinks)
  var sources = this.sources.length==0?["dummy-source"]:this.sources;
  for(var i in sources){
    var col = {sources: [], sinks: []};
    // flow from a single source end
    this.sources.forEach(function(n, j){
      col.sources.push(j==i?"-":">");
    });

    // to all sink ends
    this.sinks.forEach(function(){
      col.sinks.push("-");
    });

    table.push(col);
  }

  // flow is blocked by one of the sink ends
  // use 'dummy sink' to run loop even without sinks (for component sources)
  var sinks = this.sinks.length==0?["dummy-sink"]:this.sinks;
  for(var i in sinks){
    var col = {sources: [], sinks: []};
    // all source ends are blocked
    this.sources.forEach(function(){
      col.sources.push(">");
    });

    // one sink end is blocking
    this.sinks.forEach(function(n, j){
      col.sinks.push(i==j?">":"<");
    });

    table.push(col);
  }

  // flow is blocked by lack of sources and all sink ends
  // XXX: ideally we'd generate all combinations of blocking/flowing sinks
  if(this.sinks.length > 1){
    var col = {sources: [], sinks: []};
    // all source ends are blocked
    this.sources.forEach(function(){
      col.sources.push("<");
    });

    // all sink ends are blocking
    this.sinks.forEach(function(){
      col.sinks.push(">");
    });

    table.push(col);
  }

  // flow is blocked by lack of sources
  {
    var col = {sources: [], sinks: []};
    // all source ends are blocking
    this.sources.forEach(function(){
      col.sources.push("<");
    });

    // all sink ends are blocked
    this.sinks.forEach(function(){
      col.sinks.push("<");
    });

    table.push(col);
  }

  // annotate with node identifier
  return table.map(function(n){
    var m = {};
    m[this.id] = n;
    return m;
  }, this);
};

Reo.Node.prototype._getColoringsXrouter = function(){
  // Returns coloring table for a exclusive router node.
  var table = [];

  // flow from a source to one sink ends
  for(var i in this.sources){
    for(var j in this.sinks){
      var col = {sources: [], sinks: []};
      // flow from a single source end
      this.sources.forEach(function(n, k){
        col.sources.push(k==i?"-":">");
      });

      // to a single sink ends
      this.sinks.forEach(function(n, k){
        col.sinks.push(k==j?"-":"<");
      });

      table.push(col);
    }
  }

  // flow is blocked by all sink ends
  {
    var col = {sources: [], sinks: []};
    // all source ends are blocked
    this.sources.forEach(function(){
      col.sources.push(">");
    });

    // all sink end is blocking
    this.sinks.forEach(function(){
      col.sinks.push(">");
    });

    table.push(col);
  }

  // flow is blocked by lack of sources
  {
    var col = {sources: [], sinks: []};
    // all source ends are blocked
    this.sources.forEach(function(){
      col.sources.push("<");
    });

    // all sink ends are blocked
    this.sinks.forEach(function(){
      col.sinks.push("<");
    });

    table.push(col);
  }

  // annotate with node identifier
  return table.map(function(n){
    var m = {};
    m[this.id] = n;
    return m;
  }, this);
};

Reo.Channel = function(parent, id, sources, sinks, type){
  // Reo channel object.
  this.id = id;
  this.parent = parent;

  this.sources = [];
  this.sinks = [];

  this.type = type;
  this.typeInit = type;

  if(sources.length + sinks.length > 2)
    throw new Error("Channel can not have more than 2 ends");

  if(("sources" in type.colorings[0])!=(sources.length > 0)
      || ("sinks" in type.colorings[0])!=(sinks.length > 0))
    throw new Error("Channel end mismatch");


  // add connected source/sinks to every node
  for(var i in sources){
    var node = sources[i],
        end = new Reo.End(node, this);
    this.sources.push(end);
    node.sinks.push(end);
  }

  for(var i in sinks){
    var node = sinks[i],
        end = new Reo.End(node, this);
    this.sinks.push(end);
    node.sources.push(end);
  }
};

Reo.Channel.prototype = {
  constructor: Reo.Channel.prototype.constructor,
  get ends(){
    return this.sources.concat(this.sinks);
  },
  get colorings(){
    return this._colorings || (this._colorings = this._getColorings());
  },
  get type(){
    return this._type;
  },
  set type(v){
    // delete cached colorings and invalidate connector coloring
    this._colorings = null;
    this.parent.coloring = null;
    return (this._type = v);
  },
  get next(){
    var connectorCol = this.parent.coloring;
    if(!connectorCol || !this.type.hasNexts)
      return undefined;

    // find out which channel coloring corresponds with connector coloring
    for(var idx in this.colorings){
      var col = this.colorings[idx],
          ok = false;

      // check if all ends match
      for(var j in col){
        ok = col[j].sources.every(function(n, i){
          return n==connectorCol[j].sources[i];
        }) && col[j].sinks.every(function(n, i){
          return n==connectorCol[j].sinks[i];
        });

        if(!ok)
          break;
      }

      // found a matching coloring
      if(ok)
        return this.type.colorings[idx].next;
    }

    throw new Error("No possible next");
  }
};

Reo.Channel.prototype._getColorings = function(){
  // Returns coloring table for this channel.
  var table = [];
  console.info("Generating colorings for channel " + this.id + ".");

  // generate all possible colorings for this channel
  for(var c in this.type.colorings){
    var coloring = this.type.colorings[c];
    var col = {};

    // add all sources
    for(var i in this.sources){
      var source = this.sources[i],
          idx = source.node.sinks.indexOf(source);

      var node = {sources: [], sinks: []};
      node.sinks.length = source.node.sinks.length;
      node.sinks[idx] = coloring.sources[i];
      col[source.node.id] = node;
    }

    // add all sinks
    for(var i in this.sinks){
      var sink = this.sinks[i],
          idx = sink.node.sources.indexOf(sink);

      var node = {sources: [], sinks: []};
      node.sources.length = sink.node.sources.length;
      node.sources[idx] = coloring.sinks[i];
      col[sink.node.id] = node;
    }

    table.push(col);
  }

  return table;
};

Reo.Component = function(parent, id, coord, size, map, connector){
  // Reo (opaque) component object.
  this.id = id;
  this.parent = parent;

  this.coord = coord;
  this.size = size;

  this.map = map;
  this.connector = connector;
};

Reo.Component.prototype = {
  constructor: Reo.Component.prototype.constructor,
  get colorings(){
    // get colorings and map ports to the outside world
    var cols = this.connector.colorings,
        mappedCols = [];
    for(var i in cols){
      var col = cols[i],
          mappedCol = {};
      for(var j in col){
        // rename internal colorings to not interfere
        mappedCol[this.id + "/" + j] = col[j];

        // for the outside world, our sources are sinks and vice versa
        if(j in this.map)
          mappedCol[this.map[j]] = {sources: col[j].sinks,
                                    sinks: col[j].sources};
      }

      mappedCols.push(mappedCol);
    }

    return Reo._dedupColoringTable(mappedCols, false).all;
  }
};

Reo.Connector = function(nodes, channels, components){
  // Reo connector object.
  if(!(this instanceof Reo.Connector))
    return;

  this.nodes = {};
  // create nodes
  for(var i in nodes){
    var node = nodes[i];
    this.nodes[i] = new Reo.Node(this, i, node.coord, node.type);
  }

  this.channels = {};
  // create channels
  for(var i in channels){
    var chan = channels[i];

    // get array of sources (if any)
    var sources = chan.sources?chan.sources.map(function(n){
      return this.nodes[n];
    }, this):[];

    // get array of sinks (if any)
    var sinks = chan.sinks?chan.sinks.map(function(n){
      return this.nodes[n];
    }, this):[];

    this.channels[i] = new Reo.Channel(this, i, sources, sinks, chan.type);
  }

  this.components = {};
  this.map = {};
  // create components
  for(var i in components){
    var comp = components[i];

    // determine coord and size
    var min = [Infinity, Infinity],
        max = [0, 0];
    for(var j in comp.map){
      var node = nodes[comp.map[j]].coord;
      min[0] = Math.min(min[0], node[0]);
      min[1] = Math.min(min[1], node[1]);

      max[0] = Math.max(max[0], node[0]);
      max[1] = Math.max(max[1], node[1]);
    }
    var coord = [(min[0] + max[0]) / 2,
                 (min[1] + max[1]) / 2],
        size = [max[0] - min[0],
                max[1] - min[1]];

    // create actual connector for each component
    var connector = new Reo.Connector(comp.type.nodes, comp.type.channels);
    connector.id = comp.type.id;

    this.components[i] = new Reo.Component(this, i, coord, size, comp.map,
                                           connector);

    // create inverse map
    for(var j in comp.map){
      if(comp.map[j] in this.map)
        throw new Error("Duplicate component mapping");
      this.map[comp.map[j]] = {component: this.components[i], id: j};
    }
  }
};

Reo.Connector.prototype = {
  constructor: Reo.Connector.prototype.constructor,
  get colorings(){
    // don't cache colorings
    return this._getColorings();
  },
  get coloring(){
    return this._coloring;
  },
  set coloring(v){
    this._coloring = v;
    this._setColoring(v);
  },
  get state(){
    return this._getState();
  },
  set state(v){
    this._setState(v);
  }
};

Reo.Connector.prototype._getColorings = function(){
  // Returns coloring table for this connector.
  if(!this._colorings){
    console.time("Connector._getColorings");
    console.log("Generating cacheable colorings.");
    var table = [];

    this._peak = 0;
    var queue = [];
    // collect I/O nodes
    for(var i in this.nodes){
      var node = this.nodes[i];
      if(node.type!="read" && node.type!="write")
        continue;

      queue.push(node);
    }

    // trace through connector and join tables
    for(var node; node = queue.shift();){
      if(node.visited)
        continue;
      node.visited = true;

      for(var i in node.ends){
        var here = node.ends[i],
            chan = here.channel,
            there = chan.ends[1 - chan.ends.indexOf(here)].node;
        if(chan.visited)
          continue;
        chan.visited = true;

        if(!there.visited){
          table = Reo._joinColoringTables(table, there.colorings);
          this._peak = Math.max(this._peak, table.length);
          queue.push(there);
        }


        // these channels don't change type, thus can be cached
        if(!chan.type.hasNexts){
          table = Reo._joinColoringTables(table, chan.colorings);
          this._peak = Math.max(this._peak, table.length);
        }
      }
    }

    this._colorings = table;
    console.timeEnd("Connector._getColorings");
  }

  table = this._colorings;
  // these channels may change type, so re-join tables every time
  for(var i in this.channels){
    var chan = this.channels[i];
    if(!chan.type.hasNexts)
      continue;
    table = Reo._joinColoringTables(table, chan.colorings);
  }

  // components may change (but are cached internally)
  for(var i in this.components){
    var comp = this.components[i];
    table = Reo._joinColoringTables(table, comp.colorings);
  }

  return Reo._dedupColoringTable(table, false).all;
};

Reo.Connector.prototype._setColoring = function(col){
  // Set the current coloring for this connector.
  for(var i in this.nodes){
    var node = this.nodes[i];
    if(col && (!(i in col) || col[i].sources.length!=node.sources.length))
      throw new Error("Incomplete coloring");

    for(var j in node.sources)
      node.sources[j].color = col && col[i].sources[j];

    for(var j in node.sinks)
      node.sinks[j].color = col && col[i].sinks[j];
  }

  // set coloring of components
  for(var i in this.components){
    var comp = this.components[i];

    // filter out component-internal colorings
    var connCol = {};
    for(var j in col){
      var node = j.split("/");
      if(node[0]==comp.id)
        connCol[node[1]] = col[j];
    }

    comp.connector.coloring = col && connCol;
  }
};

Reo.Connector.prototype.validColorings = function(io){
  // Return all colorings that are valid with current pending I/O or a given
  //  [io].
  // XXX: this assumes read and write nodes have only one end
  var table = this.colorings;

  for(var i in this.nodes){
    var node = this.nodes[i];
    if(node.type!="read" && node.type!="write")
      continue;

    var nodeCols = [];
    switch(node.type){
      case "read":
        if(io.indexOf(node.sources[0]) >= 0){
          // flow from us
          var col = {sources: ["-"], sinks: []},
              tmp = {};
          tmp[node.id] = col;
          nodeCols.push(tmp);
        }else{
          // flow is blocked by us
          var col = {sources: [">"], sinks: []},
              tmp = {};
          tmp[node.id] = col;
          nodeCols.push(tmp);
        }

        // flow is blocked by connector
        {
          var col = {sources: ["<"], sinks: []},
              tmp = {};
          tmp[node.id] = col;
          nodeCols.push(tmp);
        }
      break;
      case "write":
        if(io.indexOf(node.sinks[0]) >= 0){
          // flow to us
          var col = {sources: [], sinks: ["-"]},
              tmp = {};
          tmp[node.id] = col;
          nodeCols.push(tmp);
        }else{
          // flow is blocked by us
          var col = {sources: [], sinks: ["<"]},
              tmp = {};
          tmp[node.id] = col;
          nodeCols.push(tmp);
        }

        // flow is blocked by connector
        {
          var col = {sources: [], sinks: [">"]},
              tmp = {};
          tmp[node.id] = col;
          nodeCols.push(tmp);
        }
      break;
    }

    table = Reo._joinColoringTables(table, nodeCols);
  }

  return Reo._dedupColoringTable(table, true);
};

Reo.Connector.prototype._getState = function(){
  // Get the state of a connector; that is, the current type of all nextables.
  var state = {};
  for(var i in this.channels){
    var chan = this.channels[i];
    if(!chan.type.hasNexts)
      continue;
    state[i] = chan.type;
  }

  for(var i in this.components){
    var comp = this.components[i];
    state[i] = comp.connector.state;
  }

  return state;
};

Reo.Connector.prototype._setState = function(state){
  // Set the state of a connector.
  if(state==null){
    // reset all channel types
    for(var i in this.channels){
      var chan = this.channels[i];
      chan.type = chan.typeInit;
    }
    for(var i in this.components)
      this.components[i].connector.state = null;

    return;
  }

  for(var i in state){
    if(i in this.components)
      this.components[i].connector.state = state[i];
    else if(i in this.channels)
      this.channels[i].type = state[i];
  }
};

Reo.Connector.prototype.nextTick = function(){
  // Set all channels in this connector to the 'next' state.

  // setting type immediately invalidates connector coloring,
  // so collect new types first and set them all afterward
  var newType = {};

  for(var i in this.channels){
    var chan = this.channels[i];
    if(chan.next)
      newType[chan.id] = chan.next;
    chan.animationType = null;
  }

  for(var i in newType){
    this.channels[i].type = newType[i];
  }

  for(var i in this.components){
    var comp = this.components[i];
    comp.connector.nextTick();
  }
};

Reo._joinColoringTables = function(tabA, tabB){
  // Returns the join of two coloring tables [tabA] and [tabB].
  var tabC = [];
  if(tabA.length==0)
    return tabB;
  if(tabB.length==0)
    return tabA;

  // add all in A
  for(var i in tabA){
    for(var j in tabB){
      var joined = Reo._joinColorings(tabA[i], tabB[j]);
      if(joined)
        tabC.push(joined);
    }
  }

  return tabC;
};

Reo._joinColorings = function(colA, colB){
  // Returns the join of two colorings [colA] and [colB].
  var colC = {};

  // for each node in A
  for(var i in colA){
    // add nodes only in A
    if(!(i in colB)){
      colC[i] = colA[i];
      continue;
    }

    // check compatible colors
    var joinable = true;
    var nodeA = colA[i],
        nodeB = colB[i],
        nodeC = {};

    // on both source and sink ends
    for(var end in nodeA){
      var endB = nodeB[end];
      nodeC[end] = nodeA[end].map(function(n, i){
        // either color is undefined, or the colors match
        if(!n || !endB[i] || n==endB[i])
          return n || endB[i];

        // color mismatch
        joinable = false;
      });

      if(!joinable)
        return null;
    }
    colC[i] = nodeC;
  }

  // add all nodes only in B
  for(var i in colB){
    colC[i] = colC[i] || colB[i];
  }

  return colC;
};

Reo._dedupColoringTable = function(tab, ignoreBlame){
  // Remove duplicate entries from a coloring table [tab], and sort by the
  //  amount of flow-colored ends. Ignores blame direction if [ignoreBlame] is
  //  true.
  // Returns an object of arrays of colorings separated by flow or no-flow, and
  //  an array of all colorings.
  var ret = [],
      uniq = {};

  // determine unique entries
  for(var i in tab){
    var col = tab[i];

    var flow = 0,
        mark = [];
    for(var j in col){
      var node = col[j],
          ends = node.sources.concat(node.sinks);

      // count all flow-marks in this color
      flow += ends.reduce(function(ret, n){
        return ret + (n=="-"?1:0);
      }, 0);

      var cols = ends.join(",");
      if(ignoreBlame)
        cols = cols.replace(/[<>]/g, "x");
      mark.push(j + ":" + cols);
    }
    var marks = mark.sort().join(";");
    if(uniq[marks])
      continue;

    uniq[marks] = true;
    ret.push({flow: flow, coloring: col});
  }

  // sort by amount of flow
  ret.sort(function(a, b){
    return b.flow - a.flow;
  });

  // return categorized colorings
  var cols = {flow: [], noFlow: [], all: []};
  for(var i in ret){
    var col = ret[i];
    if(col.flow > 0)
      cols.flow.push(col.coloring);
    else
      cols.noFlow.push(col.coloring);
    cols.all.push(col.coloring);
  }
  return cols;
};

Reo._debug = {};
Reo._debug.clear = function(){
};
Reo._debug.log = function(str){
  console.log(str);
};

Reo._debug.dumpColorings = function(tab){
  // Dump coloring table [tab] to console.
  if(!(tab instanceof Array))
    tab = [tab];

  for(var i in tab){
    var node = tab[i];
    console.group(i);
    try{
      for(var j in node){
        console.log(j + ": [" + node[j]["sources"] + "]"
                     + "[" + node[j]["sinks"] + "]");
      }
    }finally{
      console.groupEnd();
    }
  }
};
