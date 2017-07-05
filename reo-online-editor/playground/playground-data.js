"use strict";

var playgroundData = {};

// Channel definitions
// XXX: some of these tables include 'flip' entries
playgroundData.channels = {
  "sync": {
    id: "sync",
    colorings: [{sources: ["-"], sinks: ["-"]},
                {sources: ["<"], sinks: ["<"]},
                {sources: [">"], sinks: [">"]}],
    draw: function(canvas, from, to){
      canvas.drawArrow(from, to);
    }
  },
  "lossySync": {
    id: "lossySync",
    colorings: [{sources: ["-"], sinks: ["-"]},
                {sources: ["-"], sinks: [">"]},
                {sources: ["<"], sinks: ["<"]},
                {sources: ["<"], sinks: [">"]}],
    draw: function(canvas, from, to){
      canvas.drawArrow(from, to, {dashed: true});
    }
  },
  "syncDrain": {
    id: "syncDrain",
    colorings: [{sources: ["-", "-"]},
                {sources: ["<", ">"]},
                {sources: [">", "<"]},
                {sources: ["<", "<"]}],
    draw: function(canvas, from, to, mid){
      canvas.drawArrow(from, mid[0]);
      canvas.drawArrow(to, mid[2]);
      canvas.drawLine(mid[0], mid[2]);
    }
  },
  "asyncDrain": {
    id: "asyncDrain",
    colorings: [{sources: ["-", ">"]},
                {sources: ["-", "<"]},
                {sources: [">", "-"]},
                {sources: ["<", "-"]},
                {sources: ["<", "<"]}],
    draw: function(canvas, from, to, mid, ang){
      canvas.drawArrow(from, mid[0]);
      canvas.drawArrow(to, mid[2]);
      canvas.drawLine(mid[0], mid[2]);

      // middle dashes
      var xo = Math.sin(ang) * 3,
          yo = Math.cos(ang) * 3;

      canvas.drawPolygon([mid[1][0] + xo, mid[1][1] + yo],
                         {n: 2, radius: 8, angle: -ang});
      canvas.drawPolygon([mid[1][0] - xo, mid[1][1] - yo],
                         {n: 2, radius: 8, angle: -ang});
    }
  },
  "syncSpout": {
    id: "syncSpout",
    colorings: [{sinks: ["-", "-"]},
                {sinks: ["<", ">"]},
                {sinks: [">", "<"]},
                {sinks: [">", ">"]}],
    origin: true,
    draw: function(canvas, from, to, mid){
      canvas.drawArrow(mid[0], from);
      canvas.drawArrow(mid[0], to);
    }
  },
  "asyncSpout": {
    id: "asyncSpout",
    colorings: [{sinks: ["-", "<"]},
                {sinks: ["-", ">"]},
                {sinks: ["<", "-"]},
                {sinks: [">", "-"]},
                {sinks: [">", ">"]}],
    origin: true,
    draw: function(canvas, from, to, mid, ang){
      canvas.drawArrow(mid[0], from);
      canvas.drawArrow(mid[0], to);

      // middle dashes
      var xo = Math.sin(ang) * 3,
          yo = Math.cos(ang) * 3;

      canvas.drawPolygon([mid[1][0] + xo, mid[1][1] + yo],
                         {n: 2, radius: 8, angle: -ang});
      canvas.drawPolygon([mid[1][0] - xo, mid[1][1] - yo],
                         {n: 2, radius: 8, angle: -ang});
    }
  },
  "filter": {
    id: "filter",
    colorings: [{sources: ["-"], sinks: ["-"]}],
    draw: function(canvas, from, to, mid, ang){
      var top = Math.PI / 2 + ang;
      var points = [from,
                    [mid[1][0] + 27 * Math.sin(top + (3/6) * Math.PI),
                     mid[1][1] + 27 * Math.cos(top + (3/6) * Math.PI)],
                    [mid[1][0] + 25 * Math.sin(top + (2/6) * Math.PI),
                     mid[1][1] + 25 * Math.cos(top + (2/6) * Math.PI)],
                    [mid[1][0] + 20 * Math.sin(top + (5/6) * Math.PI),
                     mid[1][1] + 20 * Math.cos(top + (5/6) * Math.PI)],
                    [mid[1][0] + 13 * Math.sin(top),
                     mid[1][1] + 13 * Math.cos(top)],
                    [mid[1][0] + 20 * Math.sin(top - (5/6) * Math.PI),
                     mid[1][1] + 20 * Math.cos(top - (5/6) * Math.PI)],
                    [mid[1][0] + 25 * Math.sin(top - (2/6) * Math.PI),
                     mid[1][1] + 25 * Math.cos(top - (2/6) * Math.PI)],
                    [mid[1][0] + 27 * Math.sin(top - (3/6) * Math.PI),
                     mid[1][1] + 27 * Math.cos(top - (3/6) * Math.PI)],
                    to];
      canvas.drawPath(points);
      canvas.drawArrow(mid[2], to);
    }
  }
};

playgroundData._generateFIFO = function(K){
  // Generate channel data for a FIFO with capacity [K].
  var fifo = [];

  for(var I = 0; I <= K; I++){
    var chan = {};

    chan.id = "fifo" + K + "/" + I;
    chan.draw = (function(K, I){
      return function(canvas, from, to, mid, ang){
        playgroundData._drawFIFO(canvas, from, to, mid, ang, K, I);
      };
    })(K, I);

    // data items can originate from filled FIFOs
    chan.origin = (I > 0);

    // XXX: some of these tables include 'flip' entries
    if(I==0)
      chan.colorings = [["-", "<"], ["<", "<"],
                        ["-", ">"], ["<", ">"]];
    else if(I==K)
      chan.colorings = [[">", "-"], [">", ">"],
                        ["<", "-"], ["<", ">"]];
    else
      chan.colorings = [["-", "-"], ["-", "<"], [">", "-"], ["<", ">"],
                        ["<", "-"]];

    chan.colorings.forEach(function(n, i){
      this[i] = {sources: [n[0]], sinks: [n[1]]};

      // determine how many items after this coloring
      if(n[0]=="-" && n[1]!="-")
        this[i].next = I + 1;
      else if(n[0]!="-" && n[1]=="-")
        this[i].next = I - 1;

    }, chan.colorings);

    chan.hasNexts = true;
    fifo.push(chan);
  }

  // convert amount of items to channel reference
  for(var i in fifo){
    fifo[i].colorings.forEach(function(n, i){
      n.next = fifo[n.next];
    });

    fifo[i].in = fifo[Number(i) + 1];
    fifo[i].out = fifo[i - 1];
  }

  return fifo;
};

playgroundData._drawFIFO = function(canvas, from, to, mid, ang, K, I){
  // Draw a generic FIFO with capacity [K] and filled with [I] items.
  if(I > K)
    throw new Error("FIFO cannot exceed capacity");

  canvas.drawArrow(from, to);
  canvas.drawRect(mid[1], 20, 46,
                  {angle: -ang, center: true}, {stroke: "#000", fill: "#fff"});

  // FIFO capacity
  if(K > 1){
    var capacity = [mid[1][0] + (Math.cos(ang) * 20),
                    mid[1][1] - (Math.sin(ang) * 20)];
    canvas.drawRect(capacity, 20, 20,
                    {angle: -ang, center: true}, {fill: "#000"});
    canvas.drawText(capacity, K,
                    {align: "center", baseline: "middle"},
                    {fill: "#fff"});
  }

  // amount of items
  if(I > 0)
    canvas.drawPolygon(mid[1], {n: 5, radius: 8, angle: -Math.PI / 2},
                       {fill: "#fc0"});
  if(I > 1)
    canvas.drawText(mid[1], I, {align: "center", baseline: "middle"});
};

(function(channels, K){
  // Add FIFO channel definitions up to capacity [K].
  for(var i = 1; i <= K; i++){
    playgroundData._generateFIFO(i).forEach(function(n){
      channels[n.id] = n;
    });
  }
})(playgroundData.channels, 3);

// Component definitions
playgroundData.components = {
  "inhibitor": {
    id: "inhibitor",
    nodes: {
      "A": {coord: [160, 140], type: "write"},
      "B": {coord: [480, 140], type: "read"},
      "c": {coord: [320, 140], type: "merge"},

      "I": {coord: [160, 240], type: "write"},

      "d": {coord: [320, 240], type: "merge"},
      "e": {coord: [240, 240], type: "merge"},
      "f": {coord: [320, 340], type: "merge"},
      "g": {coord: [400, 240], type: "merge"}
    },
    channels: {
      "Ac": {sources: ["A"], sinks: ["c"],
             type: playgroundData.channels["sync"]},
      "cB": {sources: ["c"], sinks: ["B"],
             type: playgroundData.channels["sync"]},

      "cd": {sources: ["c", "d"],
             type: playgroundData.channels["syncDrain"]},

      "de": {sources: ["d"], sinks: ["e"],
             type: playgroundData.channels["sync"]},
      "ef": {sources: ["e"], sinks: ["f"],
             type: playgroundData.channels["fifo1/0"]},
      "fg": {sources: ["f"], sinks: ["g"],
             type: playgroundData.channels["fifo1/1"]},
      "gd": {sources: ["g"], sinks: ["d"],
             type: playgroundData.channels["sync"]},

      "Ie": {sources: ["I"], sinks: ["e"],
             type: playgroundData.channels["sync"]}
    }
  },
  "lossyChooser": {
    id: "lossyChooser",
    nodes: {
      "A": {coord: [120, 140], type: "write"},
      "B": {coord: [120, 340], type: "write"},
      "d": {coord: [240, 140], type: "merge"},
      "e": {coord: [240, 340], type: "merge"},
      "f": {coord: [400, 240], type: "merge"},
      "C": {coord: [520, 240], type: "read"}
    },
    channels: {
      "Ad": {sources: ["A"], sinks: ["d"],
             type: playgroundData.channels["sync"]},
      "Be": {sources: ["B"], sinks: ["e"],
             type: playgroundData.channels["sync"]},
      "de": {sources: ["d", "e"],
             type: playgroundData.channels["syncDrain"]},
      "df": {sources: ["d"], sinks: ["f"],
             type: playgroundData.channels["sync"]},
      "ef": {sources: ["e"], sinks: ["f"],
             type: playgroundData.channels["lossySync"]},
      "fC": {sources: ["f"], sinks: ["C"],
             type: playgroundData.channels["sync"]}
    }
  }
};

// Connector definitions
playgroundData.connectors = {
  "": {
    desc: "<h1>Welcome to the Reo Playground!</h1>" +
          "<p><a href=\"http://reo.project.cwi.nl/\">Reo</a> is channel-" +
          "based exogenous coordination model in which complex " +
          "coordinators, called <i>connectors</i>, are compositionally " +
          "built out of simpler ones. The simplest connectors in Reo are " +
          "a set of user-defined <i>channels</i>." +
          "<p>This application visualizes various Reo connectors under " +
          "given I/O conditions. Use the timeline below the animation to " +
          "control when I/O arrives.</p>" +
          "<ul><li><strong>To add an event</strong>, click on the " +
          "timeline.</li>" +
          "<li><strong>To change the timing of an event</strong>, drag it " +
          "to the desired position.</li>" +
          "<li><strong>To remove an event</strong>, drag it off the " +
          "timeline.</li></ul>" +
          "<p><small>Previous version: " +
          "<a href=\"http://reo.project.cwi.nl/webreo/\">WebReo</a>" +
          "</small></p>",
    nodes: {
      "b": {coord: [160, 160], type: "merge"},
      "c": {coord: [320, 240], type: "merge"},
      "d": {coord: [160, 320], type: "merge"},
      "R": {coord: [480, 240], type: "read"},
    },
    channels: {
      "bc": {sources: ["b"], sinks: ["c"],
             type: playgroundData.channels["fifo1/1"]},
      "cd": {sources: ["c"], sinks: ["d"],
             type: playgroundData.channels["fifo1/0"]},
      "db": {sources: ["d"], sinks: ["b"],
             type: playgroundData.channels["sync"]},
      "cR": {sources: ["c"], sinks: ["R"],
             type: playgroundData.channels["sync"]}
    }
  },
  "sync": {
    name: "Sync",
    desc: "A <b>synchronous channel</b> accepts a data item from a writer " +
          "at the source end if it can be atomically transferred to a " +
          "reader at the sink end.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sources: ["A"], sinks: ["B"],
             type: playgroundData.channels["sync"]}
    }
  },
  "lossySync": {
    name: "Lossy sync",
    desc: "A <b>lossy synchronous channel</b> always accepts a data item " +
          "from the source end. If there simultaneously is a reader at the " +
          "sink end, the data is atomically transferred, otherwise it is " +
          "lost.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sources: ["A"], sinks: ["B"],
             type: playgroundData.channels["lossySync"]}
    }
  },
  "syncDrain": {
    name: "Sync drain",
    desc: "A <b>synchronous drain</b> accepts a data item from both of its " +
          "source ends simultaneously; if there is a write pending to only " +
          "one of the two source ends, it will block. The data written to " +
          "a drain is lost.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "write"}
    },
    channels: {
      "AB": {sources: ["A", "B"],
             type: playgroundData.channels["syncDrain"]}
    }
  },
  "asyncDrain": {
    name: "Async drain",
    desc: "An <b>asynchronous drain</b> accepts a data item from one of its " +
          "source ends and loses it. If there are writes pending at both " +
          "source ends, they will never happen simultaneously: the write " +
          "that succeeds is nondeterministically chosen.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "write"}
    },
    channels: {
      "AB": {sources: ["A", "B"],
             type: playgroundData.channels["asyncDrain"]}
    }
  },
  "syncSpout": {
    name: "Sync spout",
    desc: "A <b>synchronous spout</b> dispenses an arbitrary data item from " +
          "both of its sink ends simultaneously; if there is a read pending " +
          "from only one of the two sink ends, it will block.",
    nodes: {
      "A": {coord: [200, 240], type: "read"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sinks: ["A", "B"],
             type: playgroundData.channels["syncSpout"]}
    }
  },
  "asyncSpout": {
    name: "Async spout",
    desc: "An <b>asynchronous spout</b> dispenses an arbitrary data item " +
          "from one of its sink ends. If there are writes pending at both " +
          "sink ends, they will never happen simultaneously: the read that " +
          "succeeds is nondeterministically chosen.",
    nodes: {
      "A": {coord: [200, 240], type: "read"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sinks: ["A", "B"],
             type: playgroundData.channels["asyncSpout"]}
    }
  },
  "fifo1": {
    name: "FIFO-1",
    desc: "A <b>FIFO-1 channel</b> is a queue with a buffer of capacity 1. " +
          "A read at its sink end only succeeds if the buffer is full. " +
          "Similarly, a write at the source end only succeeds if the buffer " +
          "is empty.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sources: ["A"], sinks: ["B"],
             type: playgroundData.channels["fifo1/0"]}
    }
  },
  "fifok": {
    name: "FIFO-k",
    desc: "A <b>FIFO-<i>k</i> channel</b> is a queue with a buffer of " +
          "capacity <i>k</i>. In this example, <i>k</i>=3.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sources: ["A"], sinks: ["B"],
             type: playgroundData.channels["fifo3/0"]}
    }
  },
  "filter": {
    name: "Filter",
    desc: "A <b>filter channel</b> transfers data items that match a filter " +
          "pattern. Data items that match the pattern will be transferred " +
          "like a <a href=\"#sync\">synchronous channel</a>. Data items " +
          "that do not match the pattern are accepted, but are immediately " +
          "lost.",
    nodes: {
      "A": {coord: [200, 240], type: "write"},
      "B": {coord: [440, 240], type: "read"}
    },
    channels: {
      "AB": {sources: ["A"], sinks: ["B"],
             type: playgroundData.channels["filter"]}
    }
  },
  "xrouter": {
    name: "Exclusive router",
    desc: "An <b>exclusive router</b> is a connector that transfers " +
          "incoming data items at A to either sink end B or C, but not " +
          "both. If there is only one read pending, it will receive the " +
          "incoming data item. If there are multiple reads pending, one " +
          "will be nondeterministically chosen to receive the incoming data " +
          "item.",
    nodes: {
      "A": {coord: [120, 240], type: "write"},
      "d": {coord: [240, 240], type: "merge"},
      "e": {coord: [400, 140], type: "merge"},
      "f": {coord: [400, 340], type: "merge"},
      "g": {coord: [400, 240], type: "merge"},
      "B": {coord: [520, 140], type: "read"},
      "C": {coord: [520, 340], type: "read"}
    },
    channels: {
      "Ad": {sources: ["A"], sinks: ["d"],
             type: playgroundData.channels["sync"]},
      "de": {sources: ["d"], sinks: ["e"],
             type: playgroundData.channels["lossySync"]},
      "df": {sources: ["d"], sinks: ["f"],
             type: playgroundData.channels["lossySync"]},
      "dg": {sources: ["d", "g"],
             type: playgroundData.channels["syncDrain"]},
      "eg": {sources: ["e"], sinks: ["g"],
             type: playgroundData.channels["sync"]},
      "fg": {sources: ["f"], sinks: ["g"],
             type: playgroundData.channels["sync"]},
      "eB": {sources: ["e"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "fC": {sources: ["f"], sinks: ["C"],
             type: playgroundData.channels["sync"]}
    }
  },
  "xrouterNode": {
    name: "Exclusive router node",
    desc: "The <b>exclusive router node</b> is a shorthand notation for the " +
          "<a href=\"#xrouter\">exclusive router</a>. It can also be " +
          "generalized to have more than 2 sink ends.",
    nodes: {
      "A": {coord: [120, 240], type: "write"},
      "d": {coord: [320, 240], type: "xrouter"},
      "B": {coord: [520, 140], type: "read"},
      "C": {coord: [520, 240], type: "read"},
      "D": {coord: [520, 340], type: "read"}
    },
    channels: {
      "Ad": {sources: ["A"], sinks: ["d"],
             type: playgroundData.channels["sync"]},
      "dB": {sources: ["d"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "dC": {sources: ["d"], sinks: ["C"],
             type: playgroundData.channels["sync"]},
      "dD": {sources: ["d"], sinks: ["D"],
             type: playgroundData.channels["sync"]}
    }
  },
  "takeRegulator": {
    name: "Take-cue regulator",
    desc: "<p>The <b>take-cue</b> regulator allows data flow from A to B " +
          "only when there is a read at R.</p>" +
          "<p>Using this connector, the flow between A and B is regulated " +
          "by R &mdash; even though none of the actors were designed to " +
          "exhibit such behavior! The coordination is completely determined " +
          "by the connector.</p>",
    nodes: {
      "A": {coord: [160, 190], type: "write"},
      "B": {coord: [480, 190], type: "read"},
      "c": {coord: [320, 190], type: "merge"},
      "R": {coord: [320, 290], type: "read"}
    },
    channels: {
      "Ac": {sources: ["A"], sinks: ["c"],
             type: playgroundData.channels["sync"]},
      "cB": {sources: ["c"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "cR": {sources: ["c"], sinks: ["R"],
             type: playgroundData.channels["sync"]}
    }
  },
  "writeRegulator": {
    name: "Write-cue regulator",
    desc: "The <b>write-cue</b> regulator allows data flow from A to B only " +
          "when there is a write at R.",
    nodes: {
      "A": {coord: [160, 190], type: "write"},
      "B": {coord: [480, 190], type: "read"},
      "c": {coord: [320, 190], type: "merge"},
      "R": {coord: [320, 290], type: "write"}
    },
    channels: {
      "Ac": {sources: ["A"], sinks: ["c"],
             type: playgroundData.channels["sync"]},
      "cB": {sources: ["c"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "cR": {sources: ["c", "R"],
             type: playgroundData.channels["syncDrain"]}
    }
  },
  "barrierSync": {
    name: "Barrier synchronizer",
    desc: "<p>The <b>barrier synchronizer</b> lets data flow from A to C " +
          "only when it is also possible for data to flow from B to D. The " +
          "reads C and D succeed simultaneously, regardless of when writes " +
          "at A and B arrive.</p>" +
          "<p>This connector can be seen as an extension of the " +
          "<a href=\"#writeRegulator\">write-cue regulator</a>.</p>",
    nodes: {
      "A": {coord: [160, 190], type: "write"},
      "B": {coord: [480, 190], type: "read"},
      "e": {coord: [320, 190], type: "merge"},
      "f": {coord: [320, 290], type: "merge"},
      "C": {coord: [160, 290], type: "write"},
      "D": {coord: [480, 290], type: "read"}
    },
    channels: {
      "Ae": {sources: ["A"], sinks: ["e"],
             type: playgroundData.channels["sync"]},
      "eB": {sources: ["e"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "ef": {sources: ["e", "f"],
             type: playgroundData.channels["syncDrain"]},
      "Cf": {sources: ["C"], sinks: ["f"],
             type: playgroundData.channels["sync"]},
      "fD": {sources: ["f"], sinks: ["D"],
             type: playgroundData.channels["sync"]}
    }
  },
  "feedbackLoop": {
    name: "Feedback loop",
    desc: "A <b>feedback loop</b> provides a data item on demand. Whenever " +
          "a read appears on A, it receives a predetermined data item.",
    nodes: {
      "b": {coord: [160, 160], type: "merge"},
      "c": {coord: [320, 240], type: "merge"},
      "d": {coord: [160, 320], type: "merge"},
      "A": {coord: [480, 240], type: "read"}
    },
    channels: {
      "bc": {sources: ["b"], sinks: ["c"],
             type: playgroundData.channels["sync"]},
      "cd": {sources: ["c"], sinks: ["d"],
             type: playgroundData.channels["sync"]},
      "db": {sources: ["d"], sinks: ["b"],
             type: playgroundData.channels["fifo2/1"]},
      "cA": {sources: ["c"], sinks: ["A"],
             type: playgroundData.channels["sync"]}
    }
  },
  "sequencer": {
    name: "Sequencer",
    desc: "A <b>sequencer</b> only permits reads to succeed in order. Any " +
          "read will block until all preceding reads have succeeded.",
    nodes: {
      "A": {coord: [250, 90], type: "read"},
      "B": {coord: [390, 90], type: "read"},
      "C": {coord: [520, 90], type: "read"},

      "d": {coord: [120, 290], type: "merge"},
      "e": {coord: [250, 240], type: "merge"},
      "f": {coord: [390, 240], type: "merge"},
      "g": {coord: [520, 290], type: "merge"},

      "h": {coord: [320, 390], type: "merge"}
    },
    channels: {
      "eA": {sources: ["e"], sinks: ["A"],
             type: playgroundData.channels["sync"]},
      "fB": {sources: ["f"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "gC": {sources: ["g"], sinks: ["C"],
             type: playgroundData.channels["sync"]},

      "de": {sources: ["d"], sinks: ["e"],
             type: playgroundData.channels["fifo1/1"]},
      "ef": {sources: ["e"], sinks: ["f"],
             type: playgroundData.channels["fifo1/0"]},
      "fg": {sources: ["f"], sinks: ["g"],
             type: playgroundData.channels["fifo1/0"]},

      "gh": {sources: ["g"], sinks: ["h"],
             type: playgroundData.channels["sync"]},
      "hd": {sources: ["h"], sinks: ["d"],
             type: playgroundData.channels["sync"]}
    }
  },
  "inhibitor": {
    name: "Inhibitor",
    desc: "An <b>inhibitor</b> allows the connection between A and B to be " +
          "'closed' with a write on I, prohibiting any further communication.",
    nodes: playgroundData.components["inhibitor"].nodes,
    channels: playgroundData.components["inhibitor"].channels,
  },
  "orSelector": {
    name: "Or-selector",
    desc: "An <b>or-selector</b> allows only one of the two writers to " +
          "succeed and will <a href=\"#inhibitor\">inhibit</a> any further " +
          "writes from the other writer. When two writers appear " +
          "simultaneously, one is nondeterministically chosen.",
    nodes: {
      // I/O
      "A": {coord: [100, 150], type: "write"},
      "a": {coord: [160, 150], type: "merge"},
      "B": {coord: [100, 330], type: "write"},
      "b": {coord: [160, 330], type: "merge"},
      "C": {coord: [540, 240], type: "read"},
      "c": {coord: [480, 240], type: "merge"},

      // Inhibitor 1
      "i1A": {coord: [250, 120], type: "edge"},
      "i1I": {coord: [250, 180], type: "edge"},
      "i1B": {coord: [390, 150], type: "edge"},

      // Inhibitor 2
      "i2A": {coord: [250, 360], type: "edge"},
      "i2I": {coord: [250, 300], type: "edge"},
      "i2B": {coord: [390, 330], type: "edge"}
    },
    channels: {
      "Aa": {sources: ["A"], sinks: ["a"],
             type: playgroundData.channels["sync"]},
      "Bb": {sources: ["B"], sinks: ["b"],
             type: playgroundData.channels["sync"]},
      "cC": {sources: ["c"], sinks: ["C"],
             type: playgroundData.channels["sync"]},

      "aiA": {sources: ["a"], sinks: ["i1A"],
              type: playgroundData.channels["sync"]},
      "aiI": {sources: ["a"], sinks: ["i2I"],
              type: playgroundData.channels["lossySync"]},

      "biA": {sources: ["b"], sinks: ["i2A"],
              type: playgroundData.channels["sync"]},
      "biI": {sources: ["b"], sinks: ["i1I"],
              type: playgroundData.channels["lossySync"]},

      "i1c": {sources: ["i1B"], sinks: ["c"],
              type: playgroundData.channels["sync"]},
      "i2c": {sources: ["i2B"], sinks: ["c"],
              type: playgroundData.channels["sync"]},
    },
    components: {
      "i1": {map: {"A": "i1A", "I": "i1I", "B": "i1B"},
             type: playgroundData.components["inhibitor"]},
      "i2": {map: {"A": "i2A", "I": "i2I", "B": "i2B"},
             type: playgroundData.components["inhibitor"]}
    }
  },
  "valve": {
    name: "Valve",
    desc: "A <b>valve</b> is like an <a href=\"#inhibitor\">inhibitor</a>, " +
          "in that it allows 'closing' the communication between A and B, " +
          "with a write on I, but unlike an inhibitor, the valve allows the " +
          "communication to be 'opened' again with another write on I.",
    nodes: {
      "A": {coord: [100, 140], type: "write"},
      "B": {coord: [540, 140], type: "read"},
      "c": {coord: [400, 140], type: "merge"},

      "I": {coord: [100, 240], type: "write"},

      "d": {coord: [400, 240], type: "merge"},
      "e": {coord: [320, 240], type: "merge"},
      "f": {coord: [400, 360], type: "xrouter"},
      "g": {coord: [480, 240], type: "merge"},

      "h": {coord: [240, 190], type: "merge"},
      "i": {coord: [240, 290], type: "merge"},
      "j": {coord: [160, 240], type: "xrouter"},
      "k": {coord: [160, 360], type: "merge"},
    },
    channels: {
      "Ac": {sources: ["A"], sinks: ["c"],
             type: playgroundData.channels["sync"]},
      "cB": {sources: ["c"], sinks: ["B"],
             type: playgroundData.channels["sync"]},

      "cd": {sources: ["c", "d"],
             type: playgroundData.channels["syncDrain"]},

      "de": {sources: ["d"], sinks: ["e"],
             type: playgroundData.channels["sync"]},
      "ef": {sources: ["e"], sinks: ["f"],
             type: playgroundData.channels["fifo1/0"]},
      "fg": {sources: ["f"], sinks: ["g"],
             type: playgroundData.channels["fifo1/1"]},
      "gd": {sources: ["g"], sinks: ["d"],
             type: playgroundData.channels["sync"]},

      "fk": {sources: ["f", "k"],
             type: playgroundData.channels["syncDrain"]},

      "Ij": {sources: ["I"], sinks: ["j"],
             type: playgroundData.channels["sync"]},
      "jk": {sources: ["j"], sinks: ["k"],
             type: playgroundData.channels["sync"]},
      "jh": {sources: ["j"], sinks: ["h"],
             type: playgroundData.channels["sync"]},
      "ki": {sources: ["k"], sinks: ["i"],
             type: playgroundData.channels["fifo1/1"]},

      "ih": {sources: ["i", "h"],
             type: playgroundData.channels["syncDrain"]},
      "he": {sources: ["h"], sinks: ["e"],
             type: playgroundData.channels["sync"]}
    }
  },
  "discriminator": {
    name: "Discriminator",
    desc: "The <b>discriminator</b> takes the first of its inputs and " +
          "transfers it to the output. Before the next output can be sent, " +
          "all other writers must arrive; their data will be discarded.",
    nodes: {
      "A": {coord: [160, 100], type: "write"},
      "a": {coord: [250, 100], type: "merge"},
      "c": {coord: [480, 100], type: "merge"},

      "B": {coord: [160, 180], type: "write"},
      "b": {coord: [250, 180], type: "merge"},
      "d": {coord: [480, 180], type: "merge"},

      "f": {coord: [320, 300], type: "merge"},
      "e": {coord: [480, 300], type: "merge"},

      "g": {coord: [250, 380], type: "merge"},
      "h": {coord: [480, 380], type: "merge"},
      "I": {coord: [160, 380], type: "read"}
    },
    channels: {
      "Aa": {sources: ["A"], sinks: ["a"],
             type: playgroundData.channels["sync"]},
      "Bb": {sources: ["B"], sinks: ["b"],
             type: playgroundData.channels["sync"]},

      "af": {sources: ["a"], sinks: ["f"],
             type: playgroundData.channels["lossySync"]},
      "bf": {sources: ["b"], sinks: ["f"],
             type: playgroundData.channels["lossySync"]},

      "ac": {sources: ["a"], sinks: ["c"],
             type: playgroundData.channels["fifo1/0"]},
      "bd": {sources: ["b"], sinks: ["d"],
             type: playgroundData.channels["fifo1/0"]},

      "fe": {sources: ["f"], sinks: ["e"],
             type: playgroundData.channels["fifo1/0"]},
      "fg": {sources: ["f"], sinks: ["g"],
             type: playgroundData.channels["fifo1/0"]},
      "gh": {sources: ["g"], sinks: ["h"],
             type: playgroundData.channels["fifo1/0"]},

      "cd": {sources: ["c", "d"],
             type: playgroundData.channels["syncDrain"]},
      "de": {sources: ["d", "e"],
             type: playgroundData.channels["syncDrain"]},
      "eh": {sources: ["e", "h"],
             type: playgroundData.channels["syncDrain"]},

      "gI": {sources: ["g"], sinks: ["I"],
             type: playgroundData.channels["sync"]}
    }
  },
  "shiftFifo1": {
    name: "Shift-lossy FIFO-1",
    desc: "A <b>shift-lossy FIFO-1</b> acts like a " +
          "<a href=\"#fifo1\">FIFO-1</a>, except that when it is full, " +
          "writes still succeed: the old contents are lost to make space " +
          "for new incoming data items.",
    nodes: {
      "A": {coord: [100, 190], type: "write"},
      "c": {coord: [200, 190], type: "merge"},
      "d": {coord: [440, 190], type: "xrouter"},
      "e": {coord: [200, 290], type: "merge"},
      "f": {coord: [440, 290], type: "merge"},
      "B": {coord: [540, 290], type: "read"}
    },
    channels: {
      "Ac": {sources: ["A"], sinks: ["c"],
             type: playgroundData.channels["sync"]},
      "cd": {sources: ["c"], sinks: ["d"],
             type: playgroundData.channels["fifo2/0"]},
      "de": {sources: ["d"], sinks: ["e"],
             type: playgroundData.channels["sync"]},
      "fe": {sources: ["f"], sinks: ["e"],
             type: playgroundData.channels["fifo1/1"]},
      "ec": {sources: ["e", "c"],
             type: playgroundData.channels["syncDrain"]},
      "df": {sources: ["d"], sinks: ["f"],
             type: playgroundData.channels["sync"]},
      "fB": {sources: ["f"], sinks: ["B"],
             type: playgroundData.channels["sync"]}
    }
  },
  // debug connectors
  "debug-read": {
    name: "2 writers at read",
    desc: "This is a <b>debug connector</b>, used for testing the simulation.",
    nodes: {
      "A": {coord: [100, 150], type: "write"},
      "B": {coord: [100, 330], type: "write"},
      "C": {coord: [540, 240], type: "read"}
    },
    channels: {
      "AC": {sources: ["A"], sinks: ["C"],
             type: playgroundData.channels["sync"]},
      "BC": {sources: ["B"], sinks: ["C"],
             type: playgroundData.channels["sync"]}
    }
  },
  "debug-write": {
    name: "2 readers at write",
    desc: "This is a <b>debug connector</b>, used for testing the simulation.",
    nodes: {
      "A": {coord: [100, 240], type: "write"},
      "B": {coord: [540, 140], type: "read"},
      "C": {coord: [540, 330], type: "read"}
    },
    channels: {
      "AB": {sources: ["A"], sinks: ["B"],
             type: playgroundData.channels["sync"]},
      "AC": {sources: ["A"], sinks: ["C"],
             type: playgroundData.channels["sync"]}
    }
  },
  "debug-lossyChooser": {
    name: "Lossy chooser",
    desc: "This is a <b>debug connector</b>, used for testing the simulation.",
    nodes: playgroundData.components["lossyChooser"].nodes,
    channels: playgroundData.components["lossyChooser"].channels,
  },
  "debug-component": {
    name: "Flawed animation",
    desc: "This is a <b>debug connector</b>, used for testing the simulation.",
    nodes: {
      "A": {coord: [100, 150], type: "write"},
      "B": {coord: [100, 330], type: "write"},
      "C": {coord: [540, 240], type: "read"},
      "d": {coord: [175, 180], type: "merge"},

      "iA": {coord: [250, 210], type: "edge"},
      "iB": {coord: [250, 270], type: "edge"},
      "iC": {coord: [390, 240], type: "edge"}
    },
    channels: {
      "Ad": {sources: ["A"], sinks: ["d"],
             type: playgroundData.channels["sync"]},
      "diA": {sources: ["d"], sinks: ["iA"],
              type: playgroundData.channels["sync"]},
      "BiB": {sources: ["B"], sinks: ["iB"],
              type: playgroundData.channels["sync"]},
      "CiC": {sources: ["iC"], sinks: ["C"],
              type: playgroundData.channels["sync"]}
    },
    components: {
      "i": {map: {"A": "iA", "B": "iB", "C": "iC"},
            type: playgroundData.components["lossyChooser"]}
    }
  }
};

// Playground index
playgroundData.index = {
  "Single channels": ["sync", "lossySync", "syncDrain", "asyncDrain",
                      "syncSpout", "asyncSpout", "fifo1", "fifok"],
  "Connectors": ["takeRegulator", "writeRegulator", "barrierSync",
                 "feedbackLoop", "shiftFifo1", "xrouter", "xrouterNode",
                 "sequencer", "inhibitor", "orSelector", "valve",
                 "discriminator"]
};
