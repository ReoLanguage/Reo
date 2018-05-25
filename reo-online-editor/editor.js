(function() {
  var c = document.getElementById("c");
  var container = document.getElementById("canvas");

  function resizeCanvas() {
    c.width = container.clientWidth;
    c.height = container.clientHeight;
    // Check if the Fabric.js canvas object has been initialized
    if (canvas) {
      canvas.setWidth(container.clientWidth);
      canvas.setHeight(container.clientHeight);
      canvas.calcOffset();

      // Redraw the main component
      var x1 = 50;
      var y1 = 50;
      var x2 = container.clientWidth - 50;
      var y2 = container.clientHeight - 50;
      main.set({
        'left': x1,
        'top': y1,
        'width': x2 - x1,
        'height': y2 - y1
      });

      // Reset the label position
      main.label.set({left: x1 + (x2 - x1) / 2});
      main.label.set({top: y1 + 15});
      main.label.setCoords();
      canvas.requestRenderAll();
    }
  }
  document.body.onresize = function() {resizeCanvas()};
  resizeCanvas();

  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false, preserveObjectStacking: true });
  fabric.Object.prototype.originX = fabric.Object.prototype.originY = 'center';
  fabric.Object.prototype.objectCaching = false;
  var active, isDown, origX, origY, origLeft, origTop;
  var mode = 'select';
  var id = '0';
  var nodes = [];
  var channels = [];
  var components = [];

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

  function buttonClick(button) {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = button.id;
    button.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component' || obj.class === 'node' || obj.class === 'label') {
        obj.set({'selectable': mode === 'select'});
      }
    });
  }

  document.getElementById("select").onclick =    function() {buttonClick(document.getElementById("select"))}
  document.getElementById("component").onclick = function() {buttonClick(document.getElementById("component"))}
  document.getElementById("sync").onclick =      function() {buttonClick(document.getElementById("sync"))}
  document.getElementById("lossysync").onclick = function() {buttonClick(document.getElementById("lossysync"))}
  document.getElementById("syncdrain").onclick = function() {buttonClick(document.getElementById("syncdrain"))}
  document.getElementById("syncspout").onclick = function() {buttonClick(document.getElementById("syncspout"))}
  document.getElementById("fifo1").onclick =     function() {buttonClick(document.getElementById("fifo1"))}

  document.getElementById("downloadsvg").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.svg";
    a.href = 'data:image/svg+xml;base64,' + window.btoa(canvas.toSVG());
    a.click()
  };

  document.getElementById("downloadpng").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.png";
    a.href = canvas.toDataURL('image/png');
    a.click()
  };

  document.getElementById("downloadCode").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.treo";
    a.href = window.URL.createObjectURL(new Blob([document.getElementById("text").value], {type: 'text/plain'}));
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
            return resolve(this.responseText);
          }
        };
        client.send();
      });
    }
    let text = document.getElementById("text").value;
    let network = new ReoNetwork(sourceLoader);
    await network.includeSource('default.treo');
    await network.parseComponent(text.replace(/\n/g, ''));

    try {
      let output = await network.generateCode();
      // console.log(output);
      clearAll();
      eval(output)
    } catch (e) {
      console.log(e);
      alert(e)
    }
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
        referenceAngle:    this.referenceAngle,
        referenceDistance: this.referenceDistance,
        referencePoint:    this.referencePoint,
        rotate:            this.rotate,
        scale:             this.scale
      });
    };
  })(fabric.Object.prototype.toObject);

  var Node = fabric.util.createClass(fabric.Circle, {
    type: 'node',

    initialize: function(options) {
      options || (options = {});
      this.callSuper('initialize', options);
      this.set({
        'label': options.label || '',
        'channels': options.channels || [], // these are the channels that are connected to this node
        'labelOffsetX': options.labelOffSetX || 20,
        'labelOffsetY': options.labelOffSetY || -20,
        'class': 'node',
        'nodetype': 'undefined',
        'component': options.component || main,
        'id': options.id || generateId()
      });
    },

    toObject: function() {
      return fabric.util.object.extend(this.callSuper('toObject'), {
        label: this.get('label'),
        labelOffsetX: this.get('labelOffsetX'),
        labelOffsetY: this.get('labelOffsetY'),
        class: this.get('class'),
        id: this.get('id')
      });
    },

    _render: function(ctx) {
      this.callSuper('_render', ctx);
    }
  }); // Node

  function createNode(left, top, name) {
    var node = new Node({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      fill: nodeFillColourSource,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      hasControls: false,
      selectable: mode == 'select'
    });

    var label = new fabric.IText(name ? name : node.id, {
      left: left + 20,
      top: top - 20,
      fontSize: 20,
      object: node,
      class: 'label',
      hasControls: false
      //visible: false
    });

    node.set({'label': label, 'labelOffsetX': 20, 'labelOffsetY': -20});
    label.on('editing:exited', function(e) {
      label.object.set({id: label.text})
    });

    nodes.push(node);
    // to be included later but currently throws an error when generating graphics from text
    //updateNode(node);
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
   * @returns {{class: string, components: Array}}
   */
  function createChannel(type, node1, node2) {
    // create a channel...
    var channel = {
      class: 'channel',
      components: []
    }, i;

    var diffX = Math.abs(node1.x - node2.x);
    var diffY = Math.abs(node1.y - node2.y);

    // ...a reference rectangle...
    channel.components[0] = new fabric.Rect({
      width: 5,
      height: 100,
      baseLength: 100,
      left: Math.min(node1.x,node2.x) + diffX / 2,
      top: Math.min(node1.y,node2.y) + diffY / 2,
      angle: 90,
      fill: 'red',
      visible: false,
      evented: false,
      originX: 'center',
      originY: 'center'
    });

    // ...two nodes...
    channel.node1 = createNode(node1.x, node1.y, node1.name);
    channel.node2 = createNode(node2.x, node2.y, node2.name);

    // ...and two anchors
    // TODO
    //channel.anchor1 = createAnchor(133,100);
    //channel.anchor2 = createAnchor(167,100);

    // link the channel to the nodes
    channel.node1.channels.push(channel);
    channel.node2.channels.push(channel);

    // currently loaded from a separate file
    // TODO: replace with a database search
    switch(type) {
      case 'sync':
        createSync(channel,node1.x,node1.y,node2.x,node2.y);
        break;
      case 'lossysync':
        createLossySync(channel,node1.x,node1.y,node2.x,node2.y);
        break;
      case 'syncdrain':
        createSyncDrain(channel,node1.x,node1.y,node2.x,node2.y);
        break;
      case 'syncspout':
        createSyncSpout(channel,node1.x,node1.y,node2.x,node2.y);
        break;
      case 'fifo1':
        createFIFO1(channel,node1.x,node1.y,node2.x,node2.y);
        break;
      default:
        console.log("Invalid channel name");
        return;
    }

    canvas.add(channel.components[0]);

    // calculate the relation matrix between the channel component and the reference rectangle
    // then save it as a channel component property
    for (i = 1; i < channel.components.length; i++) {
      var o = channel.components[i];
      var bossTransform = channel.components[0].calcTransformMatrix();
      var invertedBossTransform = fabric.util.invertTransform(bossTransform);
      var desiredTransform = fabric.util.multiplyTransformMatrices(invertedBossTransform, channel.components[i].calcTransformMatrix());
      o.relationship = desiredTransform;
      canvas.add(channel.components[i]);
    }
    channels.push(channel);

    // Anchors disabled for now
    // canvas.add(channel.node1, channel.node2, channel.node1.label, channel.node2.label, channel.anchor1, channel.anchor2);
    canvas.add(channel.node1, channel.node2, channel.node1.label, channel.node2.label);

    updateChannel(channel);
    //console.log(channel);
    return channel
  } //createChannel

  function loadChannels() {
    if (typeof Storage === "undefined")
      console.log("Please use a browser that supports HTML Web Storage.");
    else {
      if (!localStorage.getItem("channels")) {
        console.log("Storage contains no channel array");
        var xhttp = new XMLHttpRequest();
        xhttp.overrideMimeType("application/json");
        xhttp.onreadystatechange = function() {
          if (this.readyState === 4 && this.status === 200) {
            // Typical action to be performed when the document is ready:
            document.getElementById("text").value = xhttp.responseText;
            localStorage.setItem("channels",xhttp.responseText);
            addChannelsToInterface();
          }
        };
        xhttp.open("GET", "channels/sync1.js", true);
        xhttp.send();
      } else {
        addChannelsToInterface();
      }
    }
  }

  function addChannelsToInterface() {
    var channels = JSON.parse(localStorage.getItem("channels"));
    for (var i = 0; i < channels.length; i++) {
      var img = document.createElement("img");
      img.setAttribute("src","img/sync.svg");
      img.setAttribute("alt",channels[i].name);
      var a = document.createElement("a");
      a.setAttribute("title",channels[i].name);
      a.appendChild(img);
      var span = document.createElement("span");
      span.setAttribute("id",channels[i].name);
      span.setAttribute("class",channels[i].class);
      span.appendChild(a);
      span.appendChild(document.createElement("br"));
      span.appendChild(document.createTextNode(channels[i].name));
      document.getElementById("channels").appendChild(span);
      span.onclick = function() {buttonClick(span)};
    }
  } //addChannelsToInterface

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

  function updateNode(node) {
    var source = false, sink = false, i;

    // set coordinates and component reference
    node.label.setCoords();
    node.set({labelOffsetX: node.label.left - node.left, labelOffsetY: node.label.top - node.top});
    node.set({'component': main});
    for (i = nodes.length - 1; i >= 0; --i) {
      // prevent comparing the node with itself
      if (nodes[i] === node)
        continue;
      // merge nodes that overlap
      if (node.intersectsWithObject(nodes[i])) {
        if(Math.abs(node.left-nodes[i].left) < mergeDistance && Math.abs(node.top-nodes[i].top) < mergeDistance)
          mergeNodes(node, nodes[i]);
      }
    }

    // update the component property of the node
    for (i = components.length - 1; i >= 0; --i) {
      if (node.intersectsWithObject(components[i])) {
        if (components[i].size < node.component.size)
          node.component = components[i];
      }
    }

    // ensure that no channel crosses a component boundary
    for (i = 0; i < node.channels.length; ++i) {
      if (node === node.channels[i].node1) {
        if (node.component.size > node.channels[i].node2.component.size)
          snapOutComponent(node.channels[i].node2,node.channels[i].node2.component,node);
        else {
          if (!isBoundaryNode(node)) {
            node.channels[i].node2.component = node.component;
            snapToComponent(node.channels[i].node2,node.channels[i].node2.component);
          }
        }
      }
      else if (node === node.channels[i].node2) {
        if (node.component.size > node.channels[i].node1.component.size)
          snapOutComponent(node.channels[i].node1,node.channels[i].node1.component,node);
        else {
          if (!isBoundaryNode(node)) {
            node.channels[i].node1.component = node.component;
            snapToComponent(node.channels[i].node1,node.channels[i].node1.component);
          }
        }
      }
      else
        console.log("Broken node reference detected");
    }

    // update nodetype and colouring
    for (i = 0; i < node.channels.length; i++) {
      if (node.channels[i].node1 === node) {
        if (node.channels[i].end1 === 'source')
          source = true;
        else
          sink = true;
      }
      else if (node.channels[i].node2 === node) {
        if (node.channels[i].end2 === 'source')
          source = true;
        else
          sink = true;
      }
      else
        console.log("Error updating nodes")
    }

    if (source) {
      if (sink)
        node.set({'nodetype':'mixed','fill':nodeFillColourMixed});
      else
        node.set({'nodetype':'source','fill':nodeFillColourSource});
    }
    else
      node.set({'nodetype':'sink','fill':nodeFillColourSink})
  } //updateNode

  function updateChannel(channel) {
    var x1 = channel.node1.get('left'),
      y1 = channel.node1.get('top'),
      x2 = channel.node2.get('left'),
      y2 = channel.node2.get('top'),
      diffX = Math.abs(x1-x2),
      diffY = Math.abs(y1-y2),
      i;

    // update the reference rectangle
    channel.components[0].set({'left': Math.min(x1,x2) + diffX / 2});
    channel.components[0].set({'top': Math.min(y1,y2) + diffY / 2});
    channel.components[0].set({'angle': calculateAngle(channel, 90)});

    // convert new size to scaling
    var length = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    var scale = length/channel.components[0].baseLength;
    channel.components[0].set({'scaleX': scale, 'scaleY': scale});

    channel.components[0].setCoords();

    // update all channel components
    for (i = 1; i < channel.components.length; i++) {
      var o = channel.components[i];
      if (o.type === 'line') {
        o.set({'x1': x1, 'y1': y1, 'x2': x2, 'y2': y2});
      }
      else {
        if (!o.relationship) {
          console.log("No relationship found");
          return;
        }
        var relationship = o.relationship;
        var newTransform = fabric.util.multiplyTransformMatrices(channel.components[0].calcTransformMatrix(), relationship);
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
          o.set({'scaleX': 1, 'scaleY': 1});
        if (o.rotate === false)
          o.set({'angle': o.baseAngle});
        if (o.referencePoint === 'node1') {
          o.set({
            'left': o.referenceDistance * Math.cos((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node1.left,
            'top': o.referenceDistance * Math.sin((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node1.top
          });
        }
        if (o.referencePoint === 'node2') {
          o.set({
            'left': o.referenceDistance * Math.cos((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node2.left,
            'top': o.referenceDistance * Math.sin((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node2.top
          });
        }
      }
      o.setCoords();
    }
    canvas.requestRenderAll()
  } //updateChannel

  function isBoundaryNode(node) {
    return node.nodetype !== 'mixed' &&
      (node.left === node.component.left ||
       node.top  === node.component.top  ||
       node.left === node.component.left + node.component.width ||
       node.top  === node.component.top  + node.component.height)
  }

  function updateText() {
    if (main) {
      var s1 = main.label.text + '(', s2 = '';
      var space1 = '', space2 = '', q, obj;

      for (q = 0; q < nodes.length; ++q) {
        obj = nodes[q];
        if (obj.component.id === 'main' && isBoundaryNode(obj)) {
          s1 += space1 + obj.label.text;
          space1 = ',';
        }
      }

      for (q = 0; q < channels.length; ++q) {
        obj = channels[q];
        if (obj.node1.component === main ||
            obj.node2.component === main ||
             (isBoundaryNode(obj.node1) &&
              isBoundaryNode(obj.node2) &&
              obj.node1.component !== obj.node2.component
             )
           )
        {
          let node1 = obj.node1;
          let node2 = obj.node2;
          s2 += space2 + obj.name + '(' + node1.label.text + ',' + node2.label.text + ')';
          s2 += ' /*! pos(' + node1.label.text + '): [' + Math.round(node1.left) + ', ' + Math.round(node1.top) +
            '], pos(' + node2.label.text + '): [' + Math.round(node2.left) + ', ' + Math.round(node2.top) + '] !*/';
          space2 = '\n';
        }
      }

      for (q = 0; q < components.length; ++q) {
        obj = components[q];
        if (obj !== main) {
          var s3 = '\n' + obj.label.text + '(';
          var space3 = '', r, obj2;

          for (r = 0; r < nodes.length; ++r) {
            obj2 = nodes[r];
            if (obj2.component === obj && isBoundaryNode(obj2)) {
              s3 += space3 + obj2.label.text;
              space3 = ',';
            }
          }
          let top = Math.round(obj.top), left = Math.round(obj.left), bottom = top + obj.height, right = left + obj.width;
          space3 = '';
          s3 += ') { ';
          s3 += '/*! pos: [' + left + ', ' + top + ', ' + right + ', ' + bottom + '] !*/\n';
          for (r = 0; r < channels.length; ++r) {
            obj2 = channels[r];
            let node1 = obj2.node1;
            let node2 = obj2.node2;
            if (node1.component === obj && node2.component === obj) {
              s3 += space3 + obj2.name + '(' + node1.label.text + ',' + node2.label.text + ')';
              s3 += ' /*! pos(' + node1.label.text + '): [' + Math.round(node1.left) + ', ' + Math.round(node1.top) +
                '], pos(' + node2.label.text + '): [' + Math.round(node2.left) + ', ' + Math.round(node2.top) + '] !*/';
              space3 = '\n';
            }
          }
          space3 = '';
          s3 += '\n}';
          s2 += space2 + s3;
          space2 = ' ';
        }
      }

      s1 = s1 + ') {\n' + s2 + '\n}';
      document.getElementById('text').value = s1;
    }
  }

  function snapToComponent(node, comp) {
    var right = comp.left + comp.scaleX * comp.width, bottom = comp.top + comp.scaleY * comp.height, i;
    if (node.left > right) // right side
      node.set({'left': right});
    if (node.left < comp.left) // left side
      node.set({'left': comp.left});
    if (node.top > bottom) // bottom side
      node.set({'top': bottom});
    if (node.top < comp.top) // top side
      node.set({'top': comp.top});
    node.setCoords();
    node.label.set({'left': node.left + node.labelOffsetX, 'top': node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.channels.length; ++i)
      updateChannel(node.channels[i])
    updateText()
  }

  function snapOutComponent(node, component, connectedNode) {
    var right = component.left + component.width, bottom = component.top + component.height, i;
    if (connectedNode.left > right) // right side
      node.set({left: right});
    if (connectedNode.left < component.left) // left side
      node.set({left: component.left});
    if (connectedNode.top > bottom) // bottom side
      node.set({top: bottom});
    if (connectedNode.top < component.top) // top side
      node.set({top: component.top});
    node.setCoords();
    node.label.set({'left': node.left + node.labelOffsetX, 'top': node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.channels.length; ++i)
      updateChannel(node.channels[i])
    updateText()
  }

  canvas.on('object:moving', function(e) {
    e.target.setCoords()
  }); //object:moving

  canvas.on('object:added', function(e) {
    updateText()
  }); //object:added

  canvas.on('object:removed', function(e) {
    updateText()
  }); //object:removed

  canvas.on('text:changed', function(e) {
    updateText()
  }); //text:editing:exited

/* Anchors disabled for now
  canvas.on('mouse:over', function(e) {
    if (e.target && e.target.class === "anchor") {
      e.target.set('opacity', '100');
      canvas.requestRenderAll()
    }
  }); //mouse:over

  canvas.on('mouse:out', function(e) {
    if (e.target && e.target.class === "anchor") {
      e.target.set('opacity', '0');
      canvas.requestRenderAll()
    }
  }); //mouse:out
*/

  canvas.on('mouse:down', function(e) {
    isDown = true;
    var pointer = canvas.getPointer(e.e), i;
    origX = pointer.x;
    origY = pointer.y;
    var p = canvas.getActiveObject();
    if (p && mode !== 'select') {
      canvas.discardActiveObject();
    }
    if (mode === 'select') {
      if (p && p.class == 'component') {
        origLeft = p.left;
        origRight = p.left + p.width;
        origTop = p.top;
        origBottom = p.top + p.height;
        p.nodes = [];
        for (i = 0; i < nodes.length; ++i) {
          if (nodes[i].component === p) {
            p.nodes.push(nodes[i]);
            nodes[i].origLeft = nodes[i].left;
            nodes[i].origTop = nodes[i].top;
          }
        }
      }
    }
    else if (mode === 'component') {
      var comp = createComponent(pointer.x, pointer.y, pointer.x, pointer.y);
      canvas.setActiveObject(comp);
    }
    else {
      var channel = createChannel(mode, {x: pointer.x, y: pointer.y}, {x: pointer.x, y: pointer.y});
      snapToComponent(channel.node1,channel.node1.component);

      p = channel.node1;
      // place node on nearby edge of component
      for (i = 0; i < components.length; i++) {
        if (Math.abs(p.left - components[i].left) < mergeDistance)
          p.set({'left': components[i].left});
        if (Math.abs(p.top - components[i].top) < mergeDistance)
          p.set({'top': components[i].top});
        if (Math.abs(p.left - (components[i].left + components[i].width)) < mergeDistance)
          p.set({'left': components[i].left + components[i].width});
        if (Math.abs(p.top - (components[i].top + components[i].height)) < mergeDistance)
          p.set({'top': components[i].top + components[i].height});
        p.setCoords();
      }

      for (i = 0; i < p.channels.length; ++i)
        updateChannel(p.channels[i])
      p.label.set({left: p.left + p.labelOffsetX});
      p.label.set({top: p.top + p.labelOffsetY});
      p.label.setCoords();

      // merge with existing nodes, except node2 of the same channel
      for (i = nodes.length - 1; i >= 0; --i) {
        if (nodes[i] === p || nodes[i] === channel.node2)
          continue;
        if (p.intersectsWithObject(nodes[i])) {
          if(Math.abs(p.left-nodes[i].left) < mergeDistance && Math.abs(p.top-nodes[i].top) < mergeDistance)
            mergeNodes(nodes[i], p);
        }
      }
      canvas.setActiveObject(channel.node2)
    }
  }); //mouse:down

  canvas.on('mouse:move', function(e){
    if (!isDown) return;
    var p = canvas.getActiveObject(), i, j;
    if (!p) return;
    var pointer = canvas.getPointer(e.e);
    if (p.class === 'component') {
      if (p.status === 'drawing') {
        if (origX > pointer.x)
          p.set({left: pointer.x});
        if (origY > pointer.y)
          p.set({top: pointer.y});
        p.set({width: Math.abs(origX - pointer.x)});
        p.set({height: Math.abs(origY - pointer.y)});
        p.setCoords();
        p.header.set({x1: p.left, y1: p.top + headerHeight, x2: p.left + p.width, y2: p.top + headerHeight});
        p.header.setCoords();
        p.label.set({left: p.left + (p.width/2), top: p.top + 15});
        p.label.setCoords();
      }
      else {
        p.setCoords();
        p.header.set({x1: p.left, y1: p.top + headerHeight, x2: p.left + p.scaleX * p.width, y2: p.top + headerHeight});
        p.header.setCoords();
        p.label.set({left: p.left + (p.scaleX * p.width) / 2, top: p.top + 15});
        p.label.setCoords();
        if (p.__corner != 0) {
          for (i = 0; i < p.nodes.length; i++) {
            let node = p.nodes[i];
            if (node.origLeft == origLeft)
              node.set({'left': p.left});
            if (node.origLeft == origRight)
              node.set({'left': p.left + p.scaleX * p.width});
            if (node.origTop == origTop)
              node.set({'top': p.top});
            if (node.origTop == origBottom)
              node.set({'top': p.top + p.scaleY * p.height});
            snapToComponent(node, node.component);
          }
        }
        else {
          p.set({left: origLeft + pointer.x - origX});
          p.set({top: origTop + pointer.y - origY});
          p.setCoords();
          for (i = 0; i < p.nodes.length; i++) {
            let node = p.nodes[i];
            node.set({left: node.origLeft + pointer.x - origX});
            node.set({top: node.origTop + pointer.y - origY});
            node.setCoords();
            node.label.set({left: node.left + node.labelOffsetX});
            node.label.set({top: node.top + node.labelOffsetY});
            node.label.setCoords();
            for (j = 0; j < node.channels.length; j++)
              updateChannel(node.channels[j]);
          }
        }
      }
    }
    if (p.class === 'node') {
      p.set({'left': pointer.x, 'top': pointer.y});
      p.setCoords();
      for (i = 0; i < nodes.length; i++) {
        if (Math.abs(p.left-nodes[i].left) < mergeDistance && Math.abs(p.top-nodes[i].top) < mergeDistance) {
          p.set({'left': nodes[i].left, 'top': nodes[i].top});
          p.setCoords();
        }
      }
      for (i = 0; i < components.length; i++) {
        // Check if the node is near any component boundaries
        let left = false, top = false, right = false, bottom = false;
        if (Math.abs(p.left - components[i].left) < mergeDistance)
          left = true;
        if (Math.abs(p.top - components[i].top) < mergeDistance)
          top = true;
        if (Math.abs(p.left - (components[i].left + components[i].width)) < mergeDistance)
          right = true;
        if (Math.abs(p.top - (components[i].top + components[i].height)) < mergeDistance)
          bottom = true;
        // Check if the node is inside or close to the component
        if (left   && p.top  > components[i].top  - mergeDistance && p.top  < components[i].top  + components[i].height + mergeDistance ||
            top    && p.left > components[i].left - mergeDistance && p.left < components[i].left + components[i].width +  mergeDistance ||
            right  && p.top  > components[i].top  - mergeDistance && p.top  < components[i].top  + components[i].height + mergeDistance ||
            bottom && p.left > components[i].left - mergeDistance && p.left < components[i].left + components[i].width +  mergeDistance)
        {
          // Ensure that mixed nodes are visually separated from component boundaries
          if (p.nodetype === 'mixed') {
            if (left)
              if (p.left < components[i].left)
                p.set({'left': components[i].left - mergeDistance});
              else
                p.set({'left': components[i].left + mergeDistance});
            if (top)
              if (p.top < components[i].top)
                p.set({'top': components[i].top - mergeDistance});
              else
                p.set({'top': components[i].top + mergeDistance});
            if (right)
              if (p.left < components[i].left + components[i].width)
                p.set({'left': components[i].left + components[i].width - mergeDistance});
              else
                p.set({'left': components[i].left + components[i].width + mergeDistance});
            if (bottom)
              if (p.top < components[i].top + components[i].height)
                p.set({'top': components[i].top + components[i].height - mergeDistance});
              else
                p.set({'top': components[i].top + components[i].height + mergeDistance});
            p.setCoords();
          }
          // Put source or sink nodes on the component boundary
          else {
            if (left)
              p.set({'left': components[i].left});
            if (top)
              p.set({'top': components[i].top});
            if (right)
              p.set({'left': components[i].left + components[i].width});
            if (bottom)
              p.set({'top': components[i].top + components[i].height});
            p.setCoords();
          }
        }
      }
      for (i = 0; i < p.channels.length; ++i)
        updateChannel(p.channels[i])
      p.label.set({left: p.left + p.labelOffsetX});
      p.label.set({top: p.top + p.labelOffsetY});
      p.label.setCoords();
    }
    canvas.requestRenderAll()
  }); //mouse:move

  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject(), i;
    if (p) {
      p.setCoords();
      if (p.class === 'node') {
        updateNode(p);
      }
      if (p.class === 'component') {
        p.label.setCoords();
        reorderComponents(p);
        p.set({status: 'design'});
        p.set({'width': p.scaleX * p.width, 'height': p.scaleY * p.height, scaleX: 1, scaleY: 1});
        p.set({selectable: mode == 'select'});
        for (j = 0; j < nodes.length; j++) {
          // update the component property of the node
          for (i = components.length - 1; i >= 0; --i) {
            if (nodes[j].intersectsWithObject(components[i])) {
              if (components[i].size < nodes[j].component.size)
                nodes[j].component = components[i];
            }
          }
          // ensure that no channel crosses a component boundary
          for (i = 0; i < nodes[j].channels.length; ++i) {
            if (p.intersectsWithObject(p)) {
              if (nodes[j] === nodes[j].channels[i].node1)
                snapOutComponent(nodes[j], nodes[j].component, nodes[j].channels[i].node2);
              else if (nodes[j] === nodes[j].channels[i].node2)
                snapOutComponent(nodes[j], nodes[j].component, nodes[j].channels[i].node1);
              else
                console.log("Broken node reference detected");
            }
          }
        }
      }
      if (p.class === 'label') {
        p.setCoords();
        p.object.set({'labelOffsetX': p.left - p.object.left, 'labelOffsetY': p.top - p.object.top});
      }
      if (mode !== 'select')
        canvas.discardActiveObject();
      canvas.requestRenderAll();
      updateText();
    }
  }); //mouse:up

  function mergeNodes(destination, source) {
    for (let j = 0; j < source.channels.length; j++) {
      if (source.channels[j].node1 === source) {
        source.channels[j].node1 = destination;
      }
      else {
        if (source.channels[j].node2 === source)
          source.channels[j].node2 = destination;
        else
          console.log("Error merging nodes");
      }
      destination.channels.push(source.channels[j]);
    }
    for (let k = 0; k < nodes.length; k++)
      if (nodes[k] == source) {
        nodes.splice(k,1);
        break;
      }
    canvas.remove(source.label, source);
    destination.bringToFront();
  }

  /**
   * Reorders the components so that all components are behind the other elements and p is in front of the other components
   */
  function reorderComponents(p) {
    if (p) {
      p.label.sendToBack();
      p.sendToBack();
    }
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component' && obj !== p) {
        obj.label.sendToBack();
        obj.sendToBack()
      }
    })
  }

  function createComponent(x1,y1,x2,y2,name) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;

    var rect = new fabric.Rect({
      left: left,
      top: top,
      width: width,
      height: height,
      fill: 'transparent',
      stroke: '#000',
      strokeWidth: 1,
      hoverCursor: 'default',
      originX: 'left',
      originY: 'top',
      hasRotatingPoint: false,
      selectable: mode == 'select',
      size: width * height,
      class: 'component',
      status: 'drawing',
      nodes: [],
      id: generateId()
    });

    var header = new fabric.Line([x1, y1 + headerHeight, x2, y1 + headerHeight], {
    fill: '#000',
    stroke: '#000',
    strokeWidth: 1,
    evented: false,
  });

    var label = new fabric.IText(name ? name : 'name', {
      left: left + (width / 2),
      top: top + 15,
      fontSize: 24,
      class: 'title',
      object: rect,
      hoverCursor: 'text',
      hasControls: false,
      lockMovementX: true,
      lockMovementY: true,
      selectable: mode == 'select'
    });

    rect.set({'label': label, 'header': header});

    rect.setCoords();
    canvas.add(rect, header, label);
    canvas.requestRenderAll();
    var i = 0;
    while (i < components.length && rect.size < components[i].size)
      i++;
    components.splice(i, 0, rect);
    return rect
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
  //document.getElementById("text").value = JSON.stringify(nodes[0].channels[0]);
})();