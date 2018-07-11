require.config({paths: {'vs': 'monaco-editor/min/vs'}});
require(['vs/editor/editor.main', "vs/language/reo/reo"], function(mainModule, reoIMonarchLanguage) {
  monaco.languages.register({id: 'reo'});
  monaco.languages.setMonarchTokensProvider('reo', reoIMonarchLanguage.language);
  monaco.languages.setLanguageConfiguration('reo', reoIMonarchLanguage.conf);
  var codeEditor = monaco.editor.create(document.getElementById('text'), {language: 'reo'});

  var c = document.getElementById("c"), container = document.getElementById("canvas");

  function resizeCanvas() {
    c.width = container.clientWidth;
    c.height = container.clientHeight;
    // Check if the Fabric.js canvas object has been initialized
    if (canvas) {
      canvas.setWidth(container.clientWidth);
      canvas.setHeight(container.clientHeight);
      canvas.calcOffset();

      // Redraw the main component
      var x1 = 50, y1 = 50, x2 = container.clientWidth - 50, y2 = container.clientHeight - 50;
      main.set({
        left: x1,
        top: y1,
        width: x2 - x1,
        height: y2 - y1
      });

      // Reset the label position
      main.label.set({left: x1 + (x2 - x1) / 2, top: y1 + 15});
      main.label.setCoords();
      canvas.requestRenderAll()
    }
  }
  document.body.onresize = function() {resizeCanvas()};
  resizeCanvas();

  var canvas = this.__canvas = new fabric.Canvas('c', {selection: false, preserveObjectStacking: true});
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  fabric.Object.prototype.objectCaching = false;
  var active, isDown, origX, origY, origLeft, origTop, origRight, origBottom;
  var mode = 'select';
  var id = '0';
  var nodes = [], channels = [], components = [];

  loadChannels();

  // drawing parameters

  nodeFillColourSource = '#fff';
  nodeFillColourSink   = '#fff';
  nodeFillColourMixed  = '#000';
  nodeFactor           =      4;

  lineFillColour       = '#000';
  lineStrokeColour     = '#000';
  lineStrokeWidth      =      1;

  arrowFactor          =      8;
  arrowOffsetOut       = lineStrokeWidth * nodeFactor + 4;
  arrowOffsetIn        = arrowOffsetOut + arrowFactor;

  fifoHeight           =     30;
  fifoWidth            =     10;
  fifoFillColour       = '#fff';

  buttonBorderOff      = '0.5vmin solid white';
  buttonBorderOn       = '0.5vmin solid black';

  mergeDistance        =     20;
  headerHeight         =     30;
  loopRadius           =     25;

  function buttonClick(button) {
    canvas.discardActiveObject();
    canvas.requestRenderAll();
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = button.id;
    button.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component' || obj.class === 'node' || obj.class === 'label') {
        obj.set('selectable', mode === 'select')
      }
    })
  }

  document.getElementById("select").onclick    = function() {buttonClick(document.getElementById("select"))};
  document.getElementById("component").onclick = function() {buttonClick(document.getElementById("component"))};

  document.getElementById("downloadsvg").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.svg";
    a.href = "data:image/svg+xml;base64," + window.btoa(canvas.toSVG());
    a.click()
  };

  document.getElementById("downloadpng").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.png";
    a.href = canvas.toDataURL("image/png");
    a.click()
  };

  document.getElementById("downloadCode").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.treo";
    a.href = window.URL.createObjectURL(new Blob([document.getElementById("text").value], {type: "text/plain"}));
    a.click()
  };

  document.getElementById("submit").onclick = async function () {
    async function sourceLoader(fname) {
      return new Promise(function (resolve, reject) {
        let client = new XMLHttpRequest();
        client.open('GET', fname);
        client.onreadystatechange = function () {
          if (this.readyState === 4) {
            if (this.status !== 200)
              return reject(this.status);
            return resolve(this.responseText)
          }
        };
        client.send()
      })
    }
    let text = codeEditor.getValue();
    let network = new ReoNetwork(sourceLoader);
    await network.includeSource("default.treo");
    await network.parseComponent(text.replace(/\n/g, ''));

    try {
      // TODO generate positions if there were no geometry metadata comments
      let output = await network.generateCode();
      // console.log(output);
      clearAll();
      eval(output)
    } catch (e) {
      console.log(e);
      alert(e)
    }
  };

  document.getElementById("commentSwitch").onclick = function () {
    updateText()
  };

  // generate a new object ID
  // ID will only contain letters, i.e. z is followed by aa
  function generateId() {
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g, 'a');
    return id
  }

  // Extend the default Fabric.js object type to include additional positional parameters
  fabric.Object.prototype.toObject = (function (toObject) {
    return function () {
      return fabric.util.object.extend(toObject.call(this), {
        baseAngle:         this.baseAngle,
        referenceAngle:    this.referenceAngle,
        referenceDistance: this.referenceDistance,
        referencePoint:    this.referencePoint,
        rotate:            this.rotate,
        scale:             this.scale
      })
    }
  })(fabric.Object.prototype.toObject);

  var Node = fabric.util.createClass(fabric.Circle, {
    type: 'node',

    initialize: function(options) {
      options || (options = {});
      this.callSuper('initialize', options);
      this.set({
        label: options.label || '',
        channels: options.channels || [], // these are the channels that are connected to this node
        labelOffsetX: options.labelOffsetX || 10,
        labelOffsetY: options.labelOffsetY || -20,
        class: 'node',
        nodetype: 'undefined',
        parent: main,
        id: options.id || generateId()
      })
    },

    toObject: function() {
      return fabric.util.object.extend(this.callSuper('toObject'), {
        label: this.get('label'),
        labelOffsetX: this.get('labelOffsetX'),
        labelOffsetY: this.get('labelOffsetY'),
        class: this.get('class'),
        id: this.get('id')
      })
    },

    _render: function(ctx) {
      this.callSuper('_render', ctx)
    },

    positionMetadata: function () {
      return `pos(${this.label.text}): [${Math.round(this.left)}, ${Math.round(this.top)}]`
    }
  }); // Node

  function createNode(left, top, name, manual) {
    var node = new Node({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      fill: nodeFillColourSource,
      padding: nodeFactor * lineStrokeWidth,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      class: 'node',
      hasControls: false,
      selectable: mode === 'select'
    });

    var label = new fabric.IText(name ? name : node.id, {
      left: left + 10,
      top: top - 20,
      fontSize: 20,
      object: node,
      class: 'label',
      hasControls: false
    });

    node.set({label: label, labelOffsetX: 10, labelOffsetY: -20});
    label.on('editing:exited', function() {
      label.object.set('id', label.text)
    });

    nodes.push(node);
    setParent(node);
    canvas.add(node, node.label);
    if (!manual)
      updateNode(node);
    return node
  } //createNode

  function createAnchor(left, top) {
    return new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      hasControls: false,
      class: 'anchor',
      opacity: 0
    })
  } //createAnchor

  /**
   *
   * @param {string} type - type of channel to be created
   * @param {Object} node1
   * @param {string} [node1.name] - optional name of the first node
   * @param {number} node1.x
   * @param {number} node1.y
   * @param {Object} node2
   * @param {string} [node2.name] - optional name of the second node
   * @param {number} node2.x
   * @param {number} node2.y
   * @param {boolean} [manual] - optional flag to indicate the function was initiated by a user action
   * @returns {{class: string, parts: Array}}
   */
  function createChannel(type, node1, node2, manual) {
    // create a channel...
    var channel, i, validChannel = false;

    for (i = 0; i < channeltypes.length; ++i)
      if (channeltypes[i].name === type) {
        channel = JSON.parse(JSON.stringify(channeltypes[i]));
        validChannel = true
      }
    if (!validChannel)
      throw new Error("Channel type " + type + " is invalid");

    fabric.util.enlivenObjects(channel.parts, function(objects) {
      channel.parts = objects;
      completeChannelCreation(channel, node1, node2, manual)
    });
  } //createChannel

  function completeChannelCreation(channel, node1, node2, manual) {
    var diffX = Math.abs(node1.x - node2.x), diffY = Math.abs(node1.y - node2.y), i, p;

    // ...a reference rectangle...
    channel.parts[0].set({left: Math.min(node1.x,node2.x) + diffX / 2, top: Math.min(node1.y,node2.y) + diffY / 2});
    canvas.add(channel.parts[0]);

    // ...two nodes...
    channel.node1 = createNode(node1.x, node1.y, node1.name, manual);
    channel.node2 = createNode(node2.x, node2.y, node2.name, manual);

    // ...and two anchors
    // TODO Anchors
    //channel.anchor1 = createAnchor(133,100);
    //channel.anchor2 = createAnchor(167,100);

    // link the channel to the nodes
    channel.node1.channels.push(channel);
    channel.node2.channels.push(channel);

    // code generation functions
    channel.positionMetadata = function() {
      return ` /*! ${this.node1.positionMetadata()}, ${this.node2.positionMetadata()} !*/`
    };

    channel.generateCode = function (withComment) {
      var code = `${this.name}(${this.node1.label.text},${this.node2.label.text})`;
      if (withComment)
        code += this.positionMetadata();
      return code
    };

    // calculate the relation matrix between the channel component and the reference rectangle
    // then save it as a channel component property
    var bossTransform = channel.parts[0].calcTransformMatrix();
    for (i = 1; i < channel.parts.length; i++) {
      var invertedBossTransform = fabric.util.invertTransform(bossTransform);
      var desiredTransform = fabric.util.multiplyTransformMatrices(invertedBossTransform, channel.parts[i].calcTransformMatrix());
      channel.parts[i].relationship = desiredTransform;
      canvas.add(channel.parts[i])
    }
    channels.push(channel);
    channel.node1.bringToFront();
    channel.node2.bringToFront();

    // TODO Anchors
    // canvas.add(channel.anchor1, channel.anchor2);

    setParent(channel);
    updateChannel(channel);
    snapToComponent(channel.node1,channel.node1.parent);

    p = channel.node1;
    // place node on nearby edge of component
    for (i = 0; i < components.length; i++) {
      if (Math.abs(p.left - components[i].left) < mergeDistance)
        p.set('left', components[i].left);
      if (Math.abs(p.top - components[i].top) < mergeDistance)
        p.set('top', components[i].top);
      if (Math.abs(p.left - (components[i].left + components[i].width)) < mergeDistance)
        p.set('left', components[i].left + components[i].width);
      if (Math.abs(p.top - (components[i].top + components[i].height)) < mergeDistance)
        p.set('top', components[i].top + components[i].height);
      p.setCoords()
    }

    for (i = 0; i < p.channels.length; ++i)
      updateChannel(p.channels[i])
    p.label.set({left: p.left + p.labelOffsetX, top: p.top + p.labelOffsetY});
    p.label.setCoords();

    // merge with existing nodes, except node2 of the same channel
    for (i = nodes.length - 1; i >= 0; --i) {
      if (nodes[i] === p || nodes[i] === channel.node2)
        continue;
      if (p.intersectsWithObject(nodes[i]))
        if(Math.abs(p.left-nodes[i].left) < mergeDistance && Math.abs(p.top-nodes[i].top) < mergeDistance)
          mergeNodes(nodes[i], p)
    }
    canvas.setActiveObject(channel.node2)
  }

  function createLink(node) {
    var clone = createNode(node.left, node.top, node.label.text);
    var link = new fabric.Line([node.left, node.top, clone.left, clone.top], {
      fill: 'silver',
      stroke: 'silver',
      strokeWidth: 1,
      hasBorders: false,
      hasControls: false,
      evented: false,
      hoverCursor: 'default',
      originX: 'center',
      originY: 'center',
      nodes: [node, clone]
    });
    node.link = link;
    clone.link = link;
    canvas.discardActiveObject();
    canvas.add(link, clone, clone.label);
    node.bringToFront();
    canvas.setActiveObject(clone);
    canvas.requestRenderAll();
  }

  function loadChannels() {
    if (typeof Storage !== "undefined" && localStorage.getItem("channels"))
      channeltypes = JSON.parse(localStorage.getItem("channels"));

    for (var i = 0; i < channeltypes.length; ++i) {
      var img = document.createElement("img");
      img.setAttribute("src","img/" + channeltypes[i].name + ".svg");
      img.setAttribute("alt",channeltypes[i].name);
      var a = document.createElement("a");
      a.setAttribute("title",channeltypes[i].name);
      a.appendChild(img);
      var span = document.createElement("span");
      span.setAttribute("id",channeltypes[i].name);
      span.setAttribute("class",channeltypes[i].class);
      span.appendChild(a);
      span.appendChild(document.createElement("br"));
      span.appendChild(document.createTextNode(channeltypes[i].name));
      document.getElementById("channels").appendChild(span);
      span.onclick = function() {buttonClick(this)}
    }
  } //loadChannels

  function calculateAngle(channel, baseAngle) {
    var angle = 0;
    var x = (channel.node2.get('left') - channel.node1.get('left'));
    var y = (channel.node2.get('top')  - channel.node1.get('top'));

    if (x === 0)
      angle = (y === 0) ? 0 : (y > 0) ? Math.PI / 2 : Math.PI * 3 / 2;
    else if (y === 0)
      angle = (x > 0) ? 0 : Math.PI;
    else
      angle = (x < 0) ? Math.atan(y / x) + Math.PI : (y < 0) ? Math.atan(y / x) + (2 * Math.PI) : Math.atan(y / x);

    return ((angle * 180 / Math.PI) + baseAngle) % 360
  } //calculateAngle

  // set the parent property of object p
  // p is either a node, a channel or a component
  // if p is a component, parent should be defined
  function setParent(p, parent) {
    var parentarray, i, j;
    if (p === main)
      return;
    // if a parent is set, remove the reference to p from the parent
    if (p.parent) {
      switch(p.class) {
        case 'node':
          parentarray = p.parent.nodes;
          break;
        case 'channel':
          parentarray = p.parent.channels;
          break;
        case 'component':
          parentarray = p.parent.components
      }
      for (i = 0; i < parentarray.length; ++i) {
        if (parentarray[i] === p) {
          parentarray.splice(i,1);
          break
        }
      }
    }

    switch (p.class) {
      case 'node':
        for (i = 0; i < components.length; ++i) {
          if (p.intersectsWithObject(components[i])) {
            p.parent = components[i];
            components[i].nodes.push(p);
          }
        }
        break;
      case 'channel':
        if (p.node1.parent === p.node2.parent) {
          p.parent = p.node1.parent
        } else {
          if (p.node1.parent.size > p.node2.parent.size)
            p.parent = p.node1.parent;
          else
            p.parent = p.node2.parent
        }
        p.parent.channels.push(p);
        break;
      case 'component':
        if (!parent)
          throw new Error("Trying to set undefined parent for component");
        p.parent = parent;
        parent.components.push(p)
    }
  }

  function updateNode(node) {
    var i,j,k;

    // set node coordinates
    node.label.setCoords();
    node.set({labelOffsetX: node.label.left - node.left, labelOffsetY: node.label.top - node.top});
    for (i = nodes.length - 1; i >= 0; --i) {
      // prevent comparing the node with itself
      if (nodes[i] === node)
        continue;
      // merge nodes that overlap
      if (node.intersectsWithObject(nodes[i]))
        if(Math.abs(node.left-nodes[i].left) < mergeDistance && Math.abs(node.top-nodes[i].top) < mergeDistance)
          mergeNodes(node, nodes[i])
    }
    setParent(node);

    // ensure that no channel crosses a component boundary
    for (i = 0; i < node.channels.length; ++i) {
      var otherNode;
      if (node === node.channels[i].node1)
        otherNode = node.channels[i].node2;
      else if (node === node.channels[i].node2)
        otherNode = node.channels[i].node1;
      else
        throw new Error("Broken node reference detected");

      for (j = 0; j < components.length; ++j)
        if (components[j] === node.parent)
          break;
      for (k = 0; k < components.length; ++k)
        if (components[k] === otherNode.parent)
          break;
      if (j < k)
        snapOutComponent(otherNode, otherNode.parent, node);
      else {
        if (!isBoundaryNode(node)) {
          otherNode.parent = node.parent;
          snapToComponent(otherNode, otherNode.parent)
        }
      }
      setParent(node.channels[i]);
    }
    updateNodeColouring(node)
  } //updateNode

  function updateNodeColouring(node) {
    let source = false, sink = false, i;
    for (i = 0; i < node.channels.length; ++i) {
      if (node.channels[i].node1 === node) {
        if (node.channels[i].end1 === 'source')
          source = true;
        else
          sink = true
      } else if (node.channels[i].node2 === node) {
        if (node.channels[i].end2 === 'source')
          source = true;
        else
          sink = true
      } else throw new Error("Error updating nodes")
    }

    if (source) {
      if (sink)
        node.set({nodetype: 'mixed', fill: nodeFillColourMixed});
      else
        node.set({nodetype: 'source', fill: nodeFillColourSource})
    } else node.set({nodetype: 'sink', fill: nodeFillColourSink})
  }

  function updateChannel(channel) {
    var x1 = channel.node1.get('left'),
      y1 = channel.node1.get('top'),
      x2 = channel.node2.get('left'),
      y2 = channel.node2.get('top'),
      diffX = Math.abs(x1-x2),
      diffY = Math.abs(y1-y2),
      i;

    // update the reference rectangle
    if (channel.parts[0].type === 'rect') {
      channel.parts[0].set({
        left: Math.min(x1,x2) + diffX / 2,
        top: Math.min(y1,y2) + diffY / 2,
        angle: calculateAngle(channel, 90)
      });

      // convert new size to scaling
      var length = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
      var scale = length/channel.parts[0].baseLength;
      channel.parts[0].set({scaleX: scale, scaleY: scale});
      channel.parts[0].setCoords()
    } else if (channel.parts[0].type === 'circle') {
      channel.parts[0].set({left: x1, top: y1 - loopRadius});
      channel.parts[0].setCoords()
    }

    // update all channel components
    for (i = 1; i < channel.parts.length; i++) {
      var o = channel.parts[i];
      if (o.type === 'line')
        o.set({x1: x1, y1: y1, x2: x2, y2: y2});
      else {
        if (!o.relationship) {
          throw new Error("No relationship found");
          return
        }
        var relationship = o.relationship;
        var newTransform = fabric.util.multiplyTransformMatrices(channel.parts[0].calcTransformMatrix(), relationship);
        let opt = fabric.util.qrDecompose(newTransform);
        o.set({
          flipX: false,
          flipY: false,
        });
        o.setPositionByOrigin(
          {x: opt.translateX, y: opt.translateY},
          'center',
          'center',
        );
        o.set(opt);
        if (o.scale === false)
          o.set({scaleX: 1, scaleY: 1});
        if (o.rotate === false)
          o.set('angle', o.baseAngle);
        let reference;
        switch (o.referencePoint) {
          case 'node1':
            reference = channel.node1;
            break;
          case 'node2':
            reference = channel.node2;
            break;
          default:
            if (channel.parts[0].type === 'rect')
              reference = channel.parts[0];
            else
              reference = {
                left: loopRadius * Math.cos((channel.parts[0].angle - 90) * Math.PI / 180) + channel.parts[0].left,
                top:  loopRadius * Math.sin((channel.parts[0].angle - 90) * Math.PI / 180) + channel.parts[0].top
              }
        }
        o.set({
          left: o.referenceDistance * Math.cos((channel.parts[0].angle + o.referenceAngle + 180) * Math.PI / 180) + reference.left,
          top:  o.referenceDistance * Math.sin((channel.parts[0].angle + o.referenceAngle + 180) * Math.PI / 180) + reference.top
        })
      }
      o.setCoords()
    }
    canvas.requestRenderAll()
  } //updateChannel

  function isBoundaryNode(node) {
    return node.left === node.parent.left ||
           node.top  === node.parent.top  ||
           node.left === node.parent.left + node.parent.width ||
           node.top  === node.parent.top  + node.parent.height
  }

  function updateText() {
    let commentSwitch = document.getElementById('commentSwitch').checked;
    if (main) {
      var s1 = main.label.text + '(', s2 = '';
      var space1 = '', space2 = '    ', q, obj;

      for (q = 0; q < nodes.length; ++q) {
        obj = nodes[q];
        if (obj.parent.id === 'main' && isBoundaryNode(obj)) {
          s1 += space1 + obj.label.text;
          space1 = ','
        }
      }

      for (q = 0; q < channels.length; ++q) {
        obj = channels[q];
        let node1 = obj.node1, node2 = obj.node2;
        if (node1.parent === main ||
            node2.parent === main ||
             (isBoundaryNode(node1) &&
              isBoundaryNode(node2) &&
              node1.parent !== node2.parent
             )
        ) {
          s2 += space2 + obj.generateCode(commentSwitch);
          space2 = '\n    '
        }
      }

      for (q = 0; q < components.length; ++q) {
        obj = components[q];
        if (obj !== main) {
          var s3 = '\n' + obj.label.text + '(';
          var space3 = '', r, obj2;

          for (r = 0; r < nodes.length; ++r) {
            obj2 = nodes[r];
            if (obj2.parent === obj && isBoundaryNode(obj2)) {
              s3 += space3 + obj2.label.text;
              space3 = ','
            }
          }
          space3 = '\n';
          s3 += ') {';
          if (commentSwitch)
            s3 += obj.positionMetadata();
          for (r = 0; r < channels.length; ++r) {
            obj2 = channels[r];
            if (obj2.node1.parent === obj && obj2.node2.parent === obj)
              s3 += space3 + obj2.generateCode(commentSwitch)
          }
          space3 = '';
          s3 += '\n}';
          s2 += space2 + s3;
          space2 = ' '
        }
      }

      s1 = s1 + ') {\n' + s2 + '\n}';
      codeEditor.setValue(s1);
    }
  }

  function snapToComponent(node, comp) {
    var right = comp.left + comp.scaleX * comp.width, bottom = comp.top + comp.scaleY * comp.height, i;
    if (node.left > right) // right side
      node.set('left', right);
    if (node.left < comp.left) // left side
      node.set('left', comp.left);
    if (node.top > bottom) // bottom side
      node.set('top', bottom);
    if (node.top < comp.top) // top side
      node.set('top', comp.top);
    node.setCoords();
    node.label.set({left: node.left + node.labelOffsetX, top: node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.channels.length; ++i)
      updateChannel(node.channels[i]);
    updateText()
  }

  function snapOutComponent(node, component, connectedNode) {
    var right = component.left + component.width, bottom = component.top + component.height, i;
    if (connectedNode.left > right) // right side
      node.set('left', right);
    if (connectedNode.left < component.left) // left side
      node.set('left', component.left);
    if (connectedNode.top > bottom) // bottom side
      node.set('top', bottom);
    if (connectedNode.top < component.top) // top side
      node.set('top', component.top);
    node.setCoords();
    node.label.set({left: node.left + node.labelOffsetX, top: node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.channels.length; ++i)
      updateChannel(node.channels[i])
    updateText()
  }

  canvas.on('object:moving', function(e) {
    e.target.setCoords()
  }); //object:moving

  canvas.on('object:added', function() {
    updateText()
  }); //object:added

  canvas.on('object:removed', function() {
    updateText()
  }); //object:removed

  canvas.on('text:changed', function() {
    updateText()
  }); //text:editing:exited

  /*canvas.on('mouse:over', function(e) {
    if (e.target) {
      switch (e.target.class) {
        case "anchor":
          // TODO Anchors
          e.target.set('opacity', 100);
          break;
        case 'options':
          e.target.parent.balloon.animate('opacity', 100, {
            onChange: canvas.renderAll.bind(canvas),
            duration: 1000
          });
          break;
        case 'balloon':
          e.target.set({opacity: 100, isHover: true})
      }
      canvas.requestRenderAll()
    }
  }); //mouse:over

  canvas.on('mouse:out', function(e) {
    if (e.target) {
      switch (e.target.class) {
        case "anchor":
          // TODO Anchors
          e.target.set('opacity', 0);
          break;
        case 'options':
          e.target.parent.balloon.animate('opacity', 0, {
            onChange: canvas.renderAll.bind(canvas),
            duration: 1000,
            onComplete: (e) => {
              let balloon = e.target.parent.balloon;
              if (balloon.isHover)
                balloon.set('opacity', 100)
            }
          });
          break;
        case 'balloon':
          e.target.set({opacity: 0, isHover: false})
      }
      canvas.requestRenderAll()
    }
  }); //mouse:out*/

  canvas.on('mouse:down', function(e) {
    isDown = true;
    var pointer = canvas.getPointer(e.e), i;
    origX = pointer.x;
    origY = pointer.y;
    var p = canvas.getActiveObject();
    if (p && mode !== 'select')
      canvas.discardActiveObject();
    switch (mode) {
      case 'select':
        if (p) {
          if (p.class === 'node') {
            bringNodeToFront(p);
          } else if (p.class === 'component') {
            bringComponentToFront(p);
            origLeft = p.left;
            origRight = p.left + p.width;
            origTop = p.top;
            origBottom = p.top + p.height;
            p.nodes = [];
            for (i = 0; i < nodes.length; ++i) {
              if (nodes[i].parent === p) {
                p.nodes.push(nodes[i]);
                nodes[i].origLeft = nodes[i].left;
                nodes[i].origTop = nodes[i].top
              }
            }
          } else if (p.class === 'delete') {
            deleteComponent(p.component);
          }
        }
        break;
      case 'component':
        var comp = createComponent(pointer.x, pointer.y, pointer.x, pointer.y);
        canvas.setActiveObject(comp);
        break;
      case 'compactSwitch':
        // TODO change component to compact mode
        break;
      default:
        createChannel(mode, {x: pointer.x, y: pointer.y}, {x: pointer.x, y: pointer.y}, true);
    }
  }); //mouse:down

  canvas.on('mouse:move', function(e) {
    if (!isDown) return;
    var p = canvas.getActiveObject(), i, j;
    if (!p) return;
    var pointer = canvas.getPointer(e.e);
    if (p.class === 'component') {
      if (p.status === 'drawing') {
        if (origX > pointer.x)
          p.set('left', pointer.x);
        if (origY > pointer.y)
          p.set('top', pointer.y);
        p.set({width: Math.abs(origX - pointer.x), height: Math.abs(origY - pointer.y)});
        p.setCoords();
      } else {
        // p.options.set({left: p.left + 15, top: p.top + 15});
        // p.options.setCoords();
        // p.balloon.set({left: p.left - 40, top: p.top - 40});
        // p.balloon.setCoords();
        if (p.__corner !== 0) {
          for (i = 0; i < p.nodes.length; ++i) {
            let node = p.nodes[i];
            if (node.origLeft === origLeft)
              node.set('left', p.left);
            if (node.origLeft === origRight)
              node.set('left', p.left + p.scaleX * p.width);
            if (node.origTop === origTop)
              node.set('top', p.top);
            if (node.origTop === origBottom)
              node.set('top', p.top + p.scaleY * p.height);
            snapToComponent(node, node.parent)
          }
        } else {
          p.set({left: origLeft + pointer.x - origX, top: origTop + pointer.y - origY});
          p.setCoords();
          for (i = 0; i < p.nodes.length; i++) {
            let node = p.nodes[i];
            node.set({left: node.origLeft + pointer.x - origX, top: node.origTop + pointer.y - origY});
            node.setCoords();
            node.label.set({left: node.left + node.labelOffsetX, top: node.top + node.labelOffsetY});
            node.label.setCoords();
            for (j = 0; j < node.channels.length; ++j)
              updateChannel(node.channels[j])
          }
        }
      }
      p.label.set({left: p.left + (p.scaleX * p.width) / 2, top: p.top + 15});
      p.header.set({x1: p.left, y1: p.top + headerHeight, x2: p.left + p.scaleX * p.width, y2: p.top + headerHeight});
      p.header.setCoords();
      p.delete.set({left: p.left + 15, top: p.top + 15});
      p.delete.setCoords();
      p.compactSwitch.set({left: p.left + 20 + nodeFactor * 4, top: p.top + 15});
      p.compactSwitch.setCoords();
    } else if (p.class === 'node') {
      p.set({left: pointer.x, top: pointer.y});
      p.setCoords();
      if (p.link) {
        p.link.set({x1: p.link.nodes[0].left, y1: p.link.nodes[0].top, x2: p.link.nodes[1].left, y2: p.link.nodes[1].top});
        p.link.setCoords();
      }
      for (i = 0; i < nodes.length; i++) {
        if (Math.abs(p.left-nodes[i].left) < mergeDistance && Math.abs(p.top-nodes[i].top) < mergeDistance) {
          p.set({left: nodes[i].left, top: nodes[i].top});
          p.setCoords()
        }
      }
      for (i = 0; i < components.length; i++) {
        // Check if the node is near any component boundaries
        let changingPosition, position, size = 0;
        if (Math.abs(p.left - components[i].left) < mergeDistance) { // near left boundary
          changingPosition = 'left';
          position = components[i].left
        } else if (Math.abs(p.top - components[i].top) < mergeDistance) { // near top boundary
          changingPosition = 'top';
          position = components[i].top
        } else if (Math.abs(p.left - (components[i].left + components[i].width)) < mergeDistance) { // near right boundary
          changingPosition = 'left';
          position = components[i].left;
          size = components[i].width
        } else if (Math.abs(p.top - (components[i].top + components[i].height)) < mergeDistance) { // near bottom boundary
          changingPosition = 'top';
          position = components[i].top;
          size = components[i].height
        }
        // Check if the node is inside or close to the component
        if (changingPosition === 'left' && p.top  > components[i].top  - mergeDistance && p.top  < components[i].top  + components[i].height + mergeDistance ||
            changingPosition === 'top'  && p.left > components[i].left - mergeDistance && p.left < components[i].left + components[i].width  + mergeDistance) {
          var newPosition = {};
          // Ensure that mixed nodes are visually separated from component boundaries
          if (p.nodetype === 'mixed') {
            if (p[changingPosition] < components[i][changingPosition] + size)
              newPosition[changingPosition] = components[i][changingPosition] + size - mergeDistance;
            else
              newPosition[changingPosition] = components[i][changingPosition] + size + mergeDistance
          }
          // Put source or sink nodes on the component boundary
          else
            newPosition[changingPosition] = components[i][changingPosition] + size;
          p.set(newPosition);
          p.setCoords()
        }
      }
      for (i = 0; i < p.channels.length; ++i)
        updateChannel(p.channels[i])
      p.label.set({left: p.left + p.labelOffsetX, top: p.top + p.labelOffsetY})
    }
    p.label.setCoords();
    canvas.requestRenderAll()
  }); //mouse:move

  canvas.on('mouse:up', function(){
    isDown = false;
    var p = canvas.getActiveObject(), i, j;
    if (p) {
      p.setCoords();
      if (p.class === 'node')
        updateNode(p);
      else if (p.class === 'component') {
        p.set({width: p.scaleX * p.width, height: p.scaleY * p.height, scaleX: 1, scaleY: 1});
        p.setCoords();
        if (p.status === 'drawing') {
          p.set('status', 'design');
          p.setCoords();
          p.header.set({x1: p.left, y1: p.top + headerHeight, x2: p.left + p.width, y2: p.top + headerHeight});
          p.header.setCoords();
          p.label.set({left: p.left + (p.width/2), top: p.top + 15});
          p.label.setCoords()
        }
        bringComponentToFront(p);

        p.set('selectable', mode === 'select');
        // ensure that no channel crosses a component boundary
        for (j = 0; j < nodes.length; j++) {
          for (i = 0; i < nodes[j].channels.length; ++i) {
            if (p.intersectsWithObject(p)) {
              if (nodes[j] === nodes[j].channels[i].node1)
                snapOutComponent(nodes[j], nodes[j].parent, nodes[j].channels[i].node2);
              else if (nodes[j] === nodes[j].channels[i].node2)
                snapOutComponent(nodes[j], nodes[j].parent, nodes[j].channels[i].node1);
              else
                throw new Error("Broken node reference detected")
            }
          }
        }
        if (mode !== 'select')
          document.getElementById("select").click()
      } else if (p.class === 'label') {
        p.setCoords();
        p.object.set({labelOffsetX: p.left - p.object.left, labelOffsetY: p.top - p.object.top})
      }
      if (mode !== 'select')
        canvas.discardActiveObject();
      canvas.requestRenderAll();
      updateText()
    }
  }); //mouse:up

  function mergeNodes(destination, source) {
    var j, i;
    for (j = 0; j < source.channels.length; ++j) {
      let loop = false;
      if (source.channels[j].node1 === source) {
        source.channels[j].node1 = destination;
        if (source.channels[j].node2 === destination)
          loop = true
      } else {
        if (source.channels[j].node2 === source) {
          source.channels[j].node2 = destination;
          if (source.channels[j].node1 === destination)
            loop = true
        }
        else
          throw new Error("Error merging nodes")
      }
      if (loop) {
        // if the source node is equal to the destination node, create a loop and update the position of all channel parts
        var channel = source.channels[j];
        var rect = channel.parts[0];
        var line = channel.parts[1];

        // create a circle to replace the channel line
        var curve = new fabric.Circle({
          left: line.x1,
          top: line.y1 - loopRadius,
          angle: 0,
          strokeWidth: lineStrokeWidth,
          strokeDashArray: line.strokeDashArray,
          fill: 'transparent',
          radius: loopRadius,
          stroke: lineStrokeColour,
          hasBorders: false,
          hasControls: false,
          evented: false,
          hoverCursor: 'default'
        });
        canvas.add(curve);

        // create a new position for all parts based on their original position
        for (i = 2; i < channel.parts.length; ++i) {
          var o = channel.parts[i];
          if (o.referencePoint === 'node1' || o.referencePoint === 'node2' || o.referencePoint === 'middle') {
            // calculate the distance along the straight line
            let length = o.referenceDistance * Math.cos((rect.angle + o.referenceAngle + 180) * Math.PI / 180);
            // calculate the offset from the straight line
            let offset = o.referenceDistance * Math.sin((rect.angle + o.referenceAngle + 180) * Math.PI / 180);
            let circumference = 2 * Math.PI * loopRadius;
            // determine where on the circumference the object should be placed
            let angleA = (-length / circumference) * 360;
            if (o.referencePoint === 'middle')
              angleA += 180;
            // adjust the object's own angle
            o.angle = o.angle + angleA;
            // reposition the object
            o.set({
              left: (loopRadius + offset) * Math.cos((angleA + 90) * Math.PI / 180) + curve.left,
              top: (loopRadius + offset) * Math.sin((angleA + 90) * Math.PI / 180) + curve.top
            });
            let diffX, diffY;
            if (o.referencePoint === 'middle') {
              diffX = o.left - curve.left;
              diffY = o.top - (curve.top - loopRadius)
            } else {
              diffX = o.left - line.x1;
              diffY = o.top - line.y1
            }
            // save the new referenceDistance and referenceAngle
            o.set({
              referenceDistance: Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2)),
              referenceAngle: Math.atan2(diffY, diffX) * 180 / Math.PI + 180
            })
          }
          var bossTransform = curve.calcTransformMatrix();
          var invertedBossTransform = fabric.util.invertTransform(bossTransform);
          var desiredTransform = fabric.util.multiplyTransformMatrices(invertedBossTransform, channel.parts[i].calcTransformMatrix());
          channel.parts[i].relationship = desiredTransform;
          canvas.remove(o);
          canvas.add(o)
        }
        channel.parts[0] = curve;
        channel.parts.splice(1,1);
        canvas.remove(line)
      }
      else
        destination.channels.push(source.channels[j])
    }
    for (i = 0; i < nodes.length; ++i)
      if (nodes[i] === source) {
        nodes.splice(i,1);
        break
      }
    for (i = 0; i < source.parent.nodes.length; ++i)
      if (source.parent.nodes[i] === source) {
        source.parent.nodes.splice(i,1);
        break
      }
    canvas.remove(source.label, source);
    destination.bringToFront();
    updateNodeColouring(destination)
  }

  /**
   * Moves component p and all its objects to the top layer
   */
  function bringComponentToFront(p) {
    var i, j;
    if (!p || p.class !== 'component')
      return;
    /*for (i = 0; i < components.length; ++i)
      if (components[i] === p) {
        components.push(components.splice(i,1));
        break;
      }*/
    p.bringToFront();
    p.header.bringToFront();
    if (p !== main) {
      p.compactSwitch.bringToFront();
      p.delete.bringToFront()
    }
    p.label.bringToFront();
    for (i = 0; i < p.channels.length; ++i) {
      for (j = 1; j < p.channels[i].parts.length; ++j)
        p.channels[i].parts[j].bringToFront();
    }
    for (i = 0; i < p.nodes.length; ++i) {
      p.nodes[i].bringToFront();
      p.nodes[i].label.bringToFront()
    }
    for (i = 0; i < p.components.length; ++i) {
      bringComponentToFront(p.components[i])
    }
  }

  /**
   * Moves node p and all its connected channels to the top layer
   */
  function bringNodeToFront(p) {
    var i, j;
    if (!p || p.class !== 'node')
      return;
    for (i = 0; i < p.channels.length; ++i) {
      for (j = 1; j < p.channels[i].parts.length; ++j)
        p.channels[i].parts[j].bringToFront();
      p.channels[i].node1.bringToFront();
      p.channels[i].node2.bringToFront()
      console.log("Bringing node " + p.channels[i].node1.id + " to the front");
      console.log("Bringing node " + p.channels[i].node2.id + " to the front");
    }
    p.label.bringToFront();
  }

  function deleteNode(node) {
    var i;
    // delete the connecting channels
    for (i = node.channels.length - 1; i >= 0; --i)
      deleteChannel(node.channels[i]);
    // remove the node from the global nodes array
    for (i = 0; i < nodes.length; ++i)
      if (nodes[i] === node) {
        nodes.splice(i,1);
        break
      }
    // remove the node from the parent nodes array
    for (i = 0; i < node.parent.nodes.length; ++i)
      if (node.parent.nodes[i] === node) {
        node.parent.nodes.splice(i,1);
        break
      }
    canvas.remove(node, node.label);
  }

  function deleteChannel(channel) {
    var j;
    // delete the channel reference from the connecting nodes
    for (j = 0; j < channel.node1.channels.length; ++j)
      if (channel.node1.channels[j] === channel) {
        channel.node1.channels.splice(j,1);
        if (channel.node1.channels.length === 0)
          deleteNode(channel.node1);
        else
          updateNodeColouring(channel.node1);
        break
      }
    for (j = 0; j < channel.node2.channels.length; ++j)
      if (channel.node2.channels[j] === channel) {
        channel.node2.channels.splice(j,1);
        if (channel.node2.channels.length === 0)
          deleteNode(channel.node2);
        else
          updateNodeColouring(channel.node2);
        break
      }
    // remove the channel from the global channels array
    for (j = 0; j < channels.length; ++j)
      if (channels[j] === channel) {
        channels.splice(j,1);
        break
      }
    // remove the channel from the parent channels array
    for (j = 0; j < channel.parent.channels.length; ++j)
      if (channel.parent.channels[j] === channel) {
        channel.parent.channels.splice(j,1);
        break
      }
    // remove the channel and all its parts
    for (j = 0; j < channel.parts.length; ++j)
      canvas.remove(channel.parts[j])
  }

  /**
   * Deletes component and all underlying objects, including other components
   * recursive is for internal use only
   */
  function deleteComponent(component, recursive) {
    var k;
    // delete underlying components
    for (k = 0; k < component.components.length; ++k)
      deleteComponent(component.components[k], true);
    // delete all nodes that are in this component
    for (k = 0; k < component.nodes.length; ++k)
      deleteNode(component.nodes[k]);
    // remove the component from the global components array
    for (k = 0; k < components.length; ++k)
      if (components[k] === component) {
        components.splice(k,1);
        break
      }
    // remove the component from the parent components array
    if (component.parent && !recursive)
      for (k = 0; k < component.parent.components.length; ++k)
        if (component.parent.components[k] === component) {
          component.parent.components.splice(k,1);
          break;
        }
    if (component !== main)
      canvas.remove(component.delete, component.compactSwitch);
    canvas.remove(component, component.header, component.label)
  }

  document.addEventListener("keydown", function(e) {
    var p = canvas.getActiveObject();
    if (e.code === "Delete" && p)
      switch (p.class) {
        case 'node':
          deleteNode(p);
          break;
        case 'channel':
          deleteChannel(p);
          break;
        case 'component':
          deleteComponent(p)
      }
  });

  function createComponent(x1,y1,x2,y2,name) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;

    var component = new fabric.Rect({
      left: left,
      top: top,
      width: width,
      height: height,
      fill: '#eee',
      stroke: '#000',
      strokeWidth: 1,
      hoverCursor: 'default',
      originX: 'left',
      originY: 'top',
      hasRotatingPoint: false,
      selectable: mode === 'select',
      size: width * height,
      class: 'component',
      status: 'drawing',
      nodes: [],
      channels: [],
      components: [],
      id: name ? name : generateId()
    });

    var header = new fabric.Line([x1, y1 + headerHeight, x2, y1 + headerHeight], {
      fill: '#000',
      stroke: '#000',
      strokeWidth: 1,
      evented: false
    });

    var label = new fabric.IText(component.id, {
      left: left + (width / 2),
      top: top + 15,
      fontSize: 24,
      class: 'title',
      object: component,
      hoverCursor: 'text',
      hasControls: false,
      lockMovementX: true,
      lockMovementY: true,
      selectable: mode === 'select'
    });

    component.set({label: label, header: header});
    canvas.add(component, header, label);

    if (name !== 'main') {
      var compactSwitch = new fabric.Circle({
        left: left + 20 + nodeFactor * 4,
        top: top + 15,
        radius: nodeFactor * 2,
        hasControls: false,
        selectable: false,
        parent: component,
        class: 'compactSwitch'
      });
      component.set('compactSwitch', compactSwitch);
      canvas.add(compactSwitch);

      fabric.Image.fromURL('img/delete.svg', function(img) {
        var scale = (nodeFactor * 4) / img.height;
        var img1 = img.scale(scale).set({left: component.left + 15, top: component.top + 15, class: 'delete', component: component});
        component.set('delete', img1);
        canvas.add(img1)
      });
    }

    /*var options = new fabric.Circle({
      left: left + 15,
      top: top + 15,
      radius: nodeFactor * 2,
      hasControls: false,
      selectable: false,
      parent: rect,
      class: 'options'
    });

    var balloon = new fabric.Rect({
      left: left - 40,
      top: top - 40,
      width: 50,
      height: headerHeight,
      fill: '#FFF',
      stroke: '#000',
      strokeWidth: 1,
      originX: 'left',
      originY: 'top',
      selectable: false,
      class: 'balloon',
      isHover: false,
      opacity: 0
    });

    rect.set({options: options, balloon: balloon});
    canvas.add(options, balloon);*/

    component.positionMetadata = function() {
      let top = Math.round(this.top), left = Math.round(this.left);
      return ' /*! pos: [' + left + ', ' + top + ', ' + Math.round(left + this.width) + ', ' + Math.round(top + this.height) + '] !*/'
    };

    components.push(component);
    return component
  }

  function clearAll() {
    canvas.clear();
    id = '0';
    nodes = [];
    channels = [];
    components = [];
    main = undefined
  }

  var main = createComponent(50,50,container.clientWidth-50,container.clientHeight-50,'main');
  main.set({id: 'main', fill: 'transparent', hasBorders: false, hasControls: false, evented: false});
  id = '0';
  createChannel('sync',{x: 100, y: 150},{x: 200, y: 150});
  createChannel('lossysync',{x: 100, y: 250},{x: 200, y: 250});
  createChannel('syncdrain',{x: 100, y: 350},{x: 200, y: 350});
  createChannel('syncspout',{x: 100, y: 450},{x: 200, y: 450});
  createChannel('fifo1',{x: 100, y: 550},{x: 200, y: 550});
  document.getElementById("select").click();
});