(function() {
  var canvas = this.__canvas = new fabric.Canvas('c', { selection: false });
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

  buttonBorderOff      = '2px solid white';
  buttonBorderOn       = '2px solid black';

  document.getElementById("select").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'select';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': true});
      }
    });
  };

  document.getElementById("component").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'component';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };

  document.getElementById("sync").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'sync';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };

  document.getElementById("lossysync").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'lossysync';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };

  document.getElementById("syncdrain").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'syncdrain';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };

  document.getElementById("syncspout").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'syncspout';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };

  document.getElementById("fifo1").onclick = function() {
    document.getElementById(mode).style.border = buttonBorderOff;
    mode = 'fifo1';
    this.style.border = buttonBorderOn;
    canvas.forEachObject(function(obj) {
      if (obj.class === 'component') {
        obj.set({'selectable': false});
      }
    });
  };

  document.getElementById("downloadsvg").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.svg";
    a.href = 'data:image/svg+xml;base64,' + window.btoa(canvas.toSVG());
    a.click();
  };

  document.getElementById("downloadpng").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.png";
    a.href = canvas.toDataURL('image/png');
    a.click();
  };

  document.getElementById("downloadCode").onclick = function () {
    var a = document.getElementById("download");
    a.download = "reo.treo";
    a.href = window.URL.createObjectURL(new Blob([document.getElementById("text").value], {type: 'text/plain'}));
    a.click();
  };

  document.getElementById("submit").onclick = async function () {
    async function sourceLoader(fname) {
      return new Promise(function (resolve, reject) {
        var client = new XMLHttpRequest();
        client.open('GET', fname);
        client.onreadystatechange = function () {
          if (client.readyState === 4) {
            if (this.status !== 200) {
              return reject(this.status);
            }
            return resolve(client.responseText);
          }
        };
        client.send();
      });
    }
    let text = document.getElementById("text").value;
    let network = new ReoNetwork(sourceLoader);
    await network.includeSource('reo2tikz/core.treo');
    await network.parseComponent(text.replace(/\n/g, ''));

    try {
      console.log(await network.generateCode())
    } catch (e) {
      alert(e)
    }
  };

  // generate a new object ID
  // ID will only contain letters, i.e. z is followed by aa
  function generateId() {
    id = ((parseInt(id, 36)+1).toString(36)).replace(/[0-9]/g,'a');
    return id;
  }

  function createNode(left, top) {
    var node = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      fill: nodeFillColourSource,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      hasControls: false,
      class: 'node',
      component: main,
      id: generateId()
    });

    // these are the channels that are connected to this node
    node.channels = [];

    var label = new fabric.IText(node.id, {
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
      label.object.set({id: label.text});
    });

    nodes.push(node);
    return node;
  } //createNode

  function createAnchor(left, top) {
    var anchor = new fabric.Circle({
      left: left,
      top: top,
      strokeWidth: lineStrokeWidth,
      radius: nodeFactor * lineStrokeWidth,
      stroke: lineStrokeColour,
      hasControls: false,
      class: 'anchor',
      opacity: 0
    });
    return anchor;

  } //createAnchor

  function createChannel(name, x1, y1, x2, y2) {
    // create a channel...
    var channel = {
      class: 'channel',
      components: []
    };

    var diffX = Math.abs(x1-x2);
    var diffY = Math.abs(y1-y2);

    // ...a reference rectangle...
    channel.components[0] = new fabric.Rect({
      width: 5,
      height: 100,
      baseLength: 100,
      left: Math.min(x1,x2) + diffX / 2,
      top: Math.min(y1,y2) + diffY / 2,
      angle: 90,
      fill: 'red',
      visible: false,
      selectable: false,
      originX: 'center',
      originY: 'center',
    });

    // ...two nodes...
    channel.node1 = createNode(x1,y1);
    channel.node2 = createNode(x2,y2);

    // ...and two anchors
    // TODO
    channel.anchor1 = createAnchor(133,100);
    channel.anchor2 = createAnchor(167,100);

    // link the channel to the nodes
    channel.node1.channels.push(channel);
    channel.node2.channels.push(channel);

    // currently loaded from a separate file
    // TODO: replace with a database search
    switch(name) {
      case 'sync':
        createSync(channel,x1,y1,x2,y2);
        break;
      case 'lossysync':
        createLossySync(channel,x1,y1,x2,y2);
        break;
      case 'syncdrain':
        createSyncDrain(channel,x1,y1,x2,y2);
        break;
      case 'syncspout':
        createSyncSpout(channel,x1,y1,x2,y2);
        break;
      case 'fifo1':
        createFIFO1(channel,x1,y1,x2,y2);
        break;
      default:
        console.log("Invalid channel name");
        return;
        break;
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

    canvas.add(channel.node1, channel.node2, channel.node1.label, channel.node2.label, channel.anchor1, channel.anchor2);

    updateChannel(channel);
    return channel;
  } //createChannel

  function calculateAngle(channel, baseAngle) {
    var angle = 0;
    var x = (channel.node2.get('left') - channel.node1.get('left'));
    var y = (channel.node2.get('top')  - channel.node1.get('top'));

    if (x === 0) {
      angle = (y === 0) ? 0 : (y > 0) ? Math.PI / 2 : Math.PI * 3 / 2;
    } else if (y === 0) {
      angle = (x > 0) ? 0 : Math.PI;
    } else {
      angle = (x < 0) ? Math.atan(y / x) + Math.PI : (y < 0) ? Math.atan(y / x) + (2 * Math.PI) : Math.atan(y / x);
    }

    return ((angle * 180 / Math.PI) + baseAngle) % 360;
  } //calculateAngle

  function updateNode(node) {
    var source = false;
    var sink = false;
    for (i = 0; i < node.channels.length; i++) {
      if (node.channels[i].node1 == node) {
        if (node.channels[i].end1 == 'source')
          source = true;
        else
          sink = true;
      }
      else if (node.channels[i].node2 == node) {
        if (node.channels[i].end2 == 'source')
          source = true;
        else
          sink = true;
      }
      else
        console.log("Error updating nodes");
    }

    if (source) {
      if (sink)
        node.set({'nodetype':'mixed','fill':nodeFillColourMixed});
      else
        node.set({'nodetype':'source','fill':nodeFillColourSource});
    }
    else
      node.set({'nodetype':'sink','fill':nodeFillColourSink});
  }

  function updateChannel(channel) {
    var x1 = channel.node1.get('left');
    var y1 = channel.node1.get('top');
    var x2 = channel.node2.get('left');
    var y2 = channel.node2.get('top');
    var diffX = Math.abs(x1-x2);
    var diffY = Math.abs(y1-y2);

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
    for (k = 1; k < channel.components.length; k++) {
      var o = channel.components[k];
      if (o.type == 'line') {
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
        if (o.scale == false)
          o.set({'scaleX': 1, 'scaleY': 1});
        if (o.rotate == false)
          o.set({'angle': o.baseAngle});
        if (o.referencePoint == 'node1') {
          o.set({
            'left': o.referenceDistance * Math.cos((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node1.left,
            'top': o.referenceDistance * Math.sin((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node1.top
          });
        }
        if (o.referencePoint == 'node2') {
          o.set({
            'left': o.referenceDistance * Math.cos((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node2.left,
            'top': o.referenceDistance * Math.sin((channel.components[0].angle + o.referenceAngle + 180) * Math.PI / 180) + channel.node2.top
          });
        }
      }
      o.setCoords();
    }
    canvas.requestRenderAll();
  } //updateChannel

  function isBoundaryNode (node, component) {
    return node.left === component.left ||
      node.top === component.top ||
      node.left === component.left + component.width ||
      node.top === component.top + component.height;
  }

  function updateText() {
    if (main) {
      var s1 = main.label.text + '(', s2 = '';
      var space1 = '', space2 = '', q, obj;

      for (q = 0; q < nodes.length; ++q) {
        obj = nodes[q];
        if (obj.component.id === 'main' && isBoundaryNode(obj, obj.component)) {
          s1 += space1 + obj.label.text;
          space1 = ',';
        }
      }

      for (q = 0; q < channels.length; ++q) {
        obj = channels[q];
        if (obj.node1.component === main ||
            obj.node2.component === main ||
             (isBoundaryNode(obj.node1,obj.node1.component) &&
              isBoundaryNode(obj.node2,obj.node2.component) &&
              obj.node1.component !== obj.node2.component
             )
           )
        {
          let node1 = obj.node1;
          let node2 = obj.node2;
          s2 += space2 + obj.name + '(' + node1.label.text + ',' + node2.label.text + ')';
          s2 += ' /*! ' + 'pos(' + node1.label.text + '): [' + Math.round(node1.top) + ', ' + Math.round(node1.left) + '], ' +
            'pos(' + node2.label.text + '): [' + Math.round(node2.top) + ', ' + Math.round(node2.left) + ']' + ' !*/';
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
            if (obj2.component === obj && isBoundaryNode(obj2, obj2.component)) {
              s3 += space3 + obj2.label.text;
              space3 = ',';
            }
          }
          space3 = '';
          s3 += ') {\n  ';
          for (r = 0; r < channels.length; ++r) {
            obj2 = channels[r];
            if (obj2.node1.component === obj && obj2.node2.component === obj) {
              s3 += space3 + obj2.name + '(' + obj2.node1.label.text + ',' + obj2.node2.label.text + ')';
              space3 = ',';
            }
          }
          space3 = '';
          s3 += '\n}';
          s2 += space2 + s3;
          space2 = ' ';
        }
      }

      s1 = s1 + ') {\n' + s2 + '\n}';
      document.getElementById('text').innerHTML = s1;
    }
  }

  function snapToComponent(node, comp) {
    var right = comp.left + comp.width;
    var bottom = comp.top + comp.height;
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
    for (i = 0; i < node.channels.length; i++) {
      updateChannel(node.channels[i]);
    }
    updateText();
  }

  function snapOutComponent(node, comp, connectednode) {
    var right = comp.left + comp.width;
    var bottom = comp.top + comp.height;
    if (connectednode.left > right) // right side
      node.set({left: right});
    if (connectednode.left < comp.left) // left side
      node.set({left: comp.left});
    if (connectednode.top > bottom) // bottom side
      node.set({top: bottom});
    if (connectednode.top < comp.top) // top side
      node.set({top: comp.top});
    node.setCoords();
    node.label.set({'left': node.left + node.labelOffsetX, 'top': node.top + node.labelOffsetY});
    node.label.setCoords();
    for (i = 0; i < node.channels.length; i++) {
      updateChannel(node.channels[i]);
    }
    updateText();
  }

  canvas.on('object:moving', function(e) {
    e.target.setCoords();
  }); //object:moving

  canvas.on('object:added', function(e) {
    updateText();
  }); //object:added

  canvas.on('object:removed', function(e) {
    updateText();
  }); //object:removed

  canvas.on('text:changed', function(e) {
    updateText();
  }); //text:editing:exited

  function copy(obj) {
    var obj2 = obj.clone();
    if (obj.class == 'component') {
      obj2.set({
        'size': obj.size,
        'class': obj.class,
        'status': obj.status,
        'id': obj.id,
        'label': obj.label
      });
      obj2.label.object = obj2;
    }
    if (obj.class == 'node') {
      obj2.set({
        'class': obj.class,
        'component': obj.component,
        'id': obj.id,
        'label': obj.label,
        'channels': obj.channels
      });
      obj2.label.object = obj2;
    }
    if (obj.class == 'channel') {
      obj2.set({
        'class': obj.class,
        'name': obj.name,
        'components': obj.components,
        'node1': obj.node1,
        'node2': obj.node2,
        'anchor1': obj.anchor1,
        'anchor2': obj.anchor2
      });
      for (i = 0; i < obj.node1.channels.length; i++) {
        if (obj.node1.channels[i] == obj)
          obj.node1.channels[i] = obj2;
      }
      for (j = 0; j < obj.node2.channels.length; j++) {
        if (obj.node2.channels[j] == obj)
          obj.node2.channels[j] = obj2;
      }
    }
    if (obj.class == 'label') {
      obj2.set({
        'class': obj.class,
        'object': obj.object
      });
      obj.object.label = obj2;
    }
    return obj2;
  }

  canvas.on('mouse:over', function(e) {
    if (e.target && e.target.class == "anchor")
    {
      e.target.set('opacity', '100');
      canvas.requestRenderAll();
    }
  }); //mouse:over

  canvas.on('mouse:out', function(e) {
    if (e.target && e.target.class == "anchor")
    {
      e.target.set('opacity', '0');
      canvas.requestRenderAll();
    }
  }); //mouse:out

  canvas.on('mouse:down', function(e) {
    isDown = true;
    var pointer = canvas.getPointer(e.e);
    origX = pointer.x;
    origY = pointer.y;
    var p = canvas.getActiveObject();
    if (p && mode != 'select') {
      origLeft = p.left;
      origTop = p.top;
      return;
    }
    if (mode == 'select') {
      //console.log('Mode is select');
      if (p && p.class == 'component') {
        //console.log('p is ' + p.type + ' ' + p.id);
        //console.log('Creating a group');
        var p2 = copy(p);
        var group = new fabric.Group([ p2 ], {
          left: p.left,
          top: p.top,
          label: p.label,
          labelOffsetX: p.labelOffsetX,
          labelOffsetY: p.labelOffsetY,
          originX: 'left',
          originY: 'top',
          class: 'group'
        });
        p.label.set({'object': group});
        // Temporarily disabled because it is buggy
        /*canvas.forEachObject(function(obj) {
          //console.log(obj.id);
          console.log('Checking object type ' + obj.type + ' with class ' + obj.class + ' and id ' + obj.id);
          if(obj.type == 'i-text')
            console.log(' references ' + obj.object.id);
          if ((obj.class != 'component' && obj.component == p) || (obj.class == 'label' && obj.object.component == p))
          {
            var obj2 = copy(obj);
            group.addWithUpdate(obj2);
            canvas.remove(obj);
          }
        });*/
        canvas.remove(p);
        canvas.requestRenderAll();
        canvas.add(group);
        canvas.setActiveObject(group);
        reorderComponents(group);
        origLeft = group.left;
        origTop = group.top;
      }
    }
    if (mode == 'component') {
      canvas.discardActiveObject();
      var comp = drawComponent(pointer.x,pointer.y,pointer.x,pointer.y);
      canvas.setActiveObject(comp);
    }
    if (mode == 'sync' || mode == 'lossysync' || mode == 'syncdrain' || mode == 'syncspout' || mode == 'fifo1') {
      canvas.discardActiveObject();
      var channel = createChannel(mode,pointer.x,pointer.y,pointer.x,pointer.y);
      snapToComponent(channel.node1,main);
      canvas.setActiveObject(channel.node2);
    }
  }); //mouse:down

  canvas.on('mouse:move', function(e){
    if (!isDown)
      return;
    var p = canvas.getActiveObject();
    if (!p)
      return;
    var pointer = canvas.getPointer(e.e);
    if (p.class == 'component') {
      if (p.status == 'drawing') {
        if (origX > pointer.x)
          p.set({left:pointer.x});
        if (origY > pointer.y)
          p.set({top:pointer.y});
        p.set({width:Math.abs(origX - pointer.x)});
        p.set({height:Math.abs(origY - pointer.y)});
        p.setCoords();
        p.label.set({left: p.left + (p.width/2), top: p.top - 15});
        p.label.setCoords();
      }
    }
    if (p.class == 'node') {
      p.set({'left': pointer.x, 'top': pointer.y});
      p.setCoords();
      canvas.forEachObject(function(obj) {
        if (obj !== p && p.intersectsWithObject(obj)) {
          if (obj.class === 'node') {
            if (Math.abs(p.left-obj.left) < 10 && Math.abs(p.top-obj.top) < 10) {
              p.set({'left': obj.left, 'top': obj.top});
              p.setCoords();
            }
          }
          else if (obj.class === 'component') {
            if (Math.abs(p.left - obj.left) < 10)
              p.set({'left': obj.left});
            if (Math.abs(p.top - obj.top) < 10)
              p.set({'top': obj.top});
            if (Math.abs(p.left - (obj.left + obj.width)) < 10)
              p.set({'left': obj.left + obj.width});
            if (Math.abs(p.top - (obj.top + obj.height)) < 10)
              p.set({'top': obj.top + obj.height});
            p.setCoords();
          }
        }
      });
      for (i = 0; i < p.channels.length; i++)
        updateChannel(p.channels[i]);
      p.label.set({left: p.left + p.labelOffsetX});
      p.label.set({top: p.top + p.labelOffsetY});
      p.label.setCoords();
    }
    if (p.class == 'group') {
      p.set({left: origLeft + pointer.x - origX});
      p.set({top: origTop + pointer.y - origY});
      p.setCoords();
      p.label.set({left: p.left + p.labelOffsetX});
      p.label.set({top: p.top + p.labelOffsetY});
      p.label.setCoords();
    }
    canvas.requestRenderAll();
  }); //mouse:move

  canvas.on('mouse:up', function(e){
    isDown = false;
    var p = canvas.getActiveObject();
    if (p) {
      p.setCoords();
      if (p.class == 'node') {
        p.label.setCoords();
        p.set({labelOffsetX: p.label.left - p.left, labelOffsetY: p.label.top - p.top});
        p.set({'component': main});

        for (i = nodes.length - 1; i >= 0; i--) {
          // prevent comparing the node with itself
          if (nodes[i].id == p.id)
            continue;

          // merge nodes that overlap
          if (p.intersectsWithObject(nodes[i])) {
            if(Math.abs(p.left-nodes[i].left) < 10 && Math.abs(p.top-nodes[i].top) < 10) {
              for (j = 0; j < nodes[i].channels.length; j++) {
                if (nodes[i].channels[j].node1.id == nodes[i].id) {
                  nodes[i].channels[j].node1 = p;
                }
                else {
                  if (nodes[i].channels[j].node2.id == nodes[i].id)
                    nodes[i].channels[j].node2 = p;
                  else
                    console.log("Error merging nodes");
                }
                p.channels.push(nodes[i].channels[j]);
              }
              canvas.remove(nodes[i].label, nodes[i]);
              nodes.splice(i,1);
              updateNode(p);
              p.bringToFront();
            }
          }
        }

        // update the component property of the node
        canvas.forEachObject(function(obj) {
          if (p.intersectsWithObject(obj)) {
            if (obj.get('class') == 'component') {
              if (obj.size < p.component.size) {
                p.component = obj;
              }
            }
          }
        });

        // ensure that no channel crosses a component boundary
        for (m = 0; m < p.channels.length; m++) {
          if (p.channels[m].node1 == p) {
            if (p.channels[m].node2.component.size < p.component.size)
              snapOutComponent(p.channels[m].node2,p.channels[m].node2.component,p);
            else {
              p.channels[m].node2.component = p.component;
              snapToComponent(p.channels[m].node2,p.component);
            }
          }
          else if (p.channels[m].node2 == p) {
            if (p.channels[m].node1.component.size < p.component.size)
              snapOutComponent(p.channels[m].node1,p.channels[m].node1.component,p);
            else {
              p.channels[m].node1.component = p.component;
              snapToComponent(p.channels[m].node1,p.component);
            }
          }
          else
            console.log("Broken node reference detected");
        }

      }
      if (p.class == 'component') {
        p.label.setCoords();
        reorderComponents(p);
        p.set({'labelOffsetX': p.label.left - p.left, 'labelOffsetY': p.label.top - p.top, status: 'design'});
        if (mode != 'select')
          p.set({selectable: false});
      }
      if (p.class == 'label') {
        p.setCoords();
        p.object.set({'labelOffsetX': p.left - p.object.left, 'labelOffsetY': p.top - p.object.top});
      }
      else {
        canvas.discardActiveObject();
      }
      if (p.class == 'group') {
        var items = p._objects;
        p._restoreObjectsState();
        canvas.remove(p);
        var comp = items[0];
        comp.set({'labelOffsetX': p.labelOffsetX, 'labelOffsetY': p.labelOffsetY});
        comp.label.set({'object': comp});
        canvas.add(comp);
        for (var i = 1; i < items.length; i++) {
          items[i].set({'component': comp});
          canvas.add(items[i]);
        }
        canvas.requestRenderAll();
      }
      reorderComponents();
      canvas.requestRenderAll();
    }
  }); //mouse:up

  /* Reorders the components so that all components are behind the other elements and p is in front of the other components */
  function reorderComponents(p) {
    if (p) {
      p.label.sendToBack();
      p.sendToBack();
    }
    canvas.forEachObject(function(obj) {
      if (obj.class == 'component' && obj != p) {
        obj.label.sendToBack();
        obj.sendToBack();
      }
    });
  }

  function drawComponent(x1,y1,x2,y2) {
    var width = (x2 - x1);
    var height = (y2 - y1);
    var left = x1;
    var top = y1;

    var rect = new fabric.Rect({
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
      //hasBorders: false,
      //hasControls: false,
      size: width * height,
      class: 'component',
      status: 'drawing',
      id: generateId()
    });

    var label = new fabric.IText('name', {
      left: left + (width / 2),
      top: top - 15,
      fontSize: 32,
      class: 'label',
      object: rect,
      hasControls: false
    });

    rect.set({'label': label, 'labelOffsetX': left + (width / 2), 'labelOffsetY': -15});

    rect.setCoords();
    canvas.add(rect,label);
    canvas.requestRenderAll();
    components.push(rect);
    return rect;
  }

  var main = drawComponent(50,50,750,550);
  main.set({id: 'main', fill: 'transparent', hasBorders: false, hasControls: false, evented: false});
  main.label.set({'text': 'main'});
  id = '0';
  document.getElementById("select").click();
  createChannel('sync',100,100,200,100);
  createChannel('lossysync',100,200,200,200);
  createChannel('syncdrain',100,300,200,300);
  createChannel('syncspout',100,400,200,400);
  createChannel('fifo1',100,500,200,500);
})();