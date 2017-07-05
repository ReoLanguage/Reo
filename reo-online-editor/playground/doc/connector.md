# Connectors
A Reo connector consists of nodes and channels, and optionally components. All
nodes and channels have a string identifier, are connected through ends.

An end connects a single node with a single channel; from the perspective of a
node or a channel, an end is either a source or a sink end. A node's source
ends are a channel's sink ends and vice versa.

A channel has a user-defined type (see the section "Channel types") and has
exactly 2 ends.  These may be either both a source and a sink end, two source
ends, or two sink ends.

A node has a coordinate, and a type, which is one of: `read`, `write`, `merge`,
`xrouter`, or `edge`.

A component is a connector and a map of nodes from outside the connector to
inside the connector.

## Channel types
In Reo, all channels are user-defined. A channel consists of at least an
identifier, a draw function, and a coloring table.  An example of a channel
type is given below.

    {
      id: "sync",
      colorings: [{sources: ["-"], sinks: ["-"]},
                  {sources: ["<"], sinks: ["<"]},
                  {sources: [">"], sinks: [">"]}],
      origin: false,
      draw: function(canvas, from, to, mid, angle){
        canvas.drawArrow(from, to);
      }
    }

The draw function is called when the channel needs to be drawn. The parameters
give the canvas to draw on, the coordinates where the channel starts (`from`)
and where the channel ends (`to`). For convenience, an array of some useful
points (in fourths) along that line (`mid`), and the angle between `from` and
`to` is given. These can be calculated from the two given coordinates, but
since they are often needed they are passed to the draw function as well.

The coloring table is an array of objects, each object containing an array for
`sources` and/or an array for `sinks`. Since a channel must have exactly two
ends, if both `sources` and `sinks` are present, each array has only one entry;
if only one is present, that array must have two entries.

Some channels can be the 'origin' of data items, such as spouts or FIFOs. This
means a channel can emit a data item without it being written to. The property
`origin` must be set to `true` if the channel is an origin.

Furthermore, channels that can be in multiple 'configurations' or 'states' are
implemented as separate channels altogether. The progression between different
configurations is expressed with the `next`-properties of colorings, described
in the section "Coloring".

## Components
A connector may contain several components, which are opaque subconnectors. A
component consists of a connector and a map of nodes. The node map is an object
that maps each internal read or write node to a node in the parent connector.

A component-internal node may be mapped to any parent node, but for performance
reasons, it is recommended that nodes of type `edge` are used. These nodes are
ignored when determining the cached colorings of the parent connector.

## Coloring
A coloring (see also [1]) is a map from ends to colors. A coloring is
represented as an object with a child for each node in the connector, each
child containing an array of colors for each sink and source end of that node.

A color is represented as a string, which is one of: `-` (flow), `<` or `>`
(no-flow, pointing towards blame). The direction of blame is defined as such:

- For a node or channel's source ends, the node or channel (self) is on the
  right side and what it connects to is (other) on the left side. The color `>`
  blames the self, and the color `<` blames the other.
- For a node or channel's sink ends, the self is on the left side and the other
  is on the right side. The color `<` blames the self, and the color `>` blames
  the other.

For channels that can be in multiple configurations (see "Channel types"), a
coloring may optionally have a `next`-property. This property determines which
type the channel becomes if this coloring is applied to it. For example, an
'empty FIFO'-type channel that has its source end flow-colored has a `next`
pointing to a 'full FIFO'-type channel.

Furthermore, during animation, a channel may be in an intermediate state;
consider a FIFO of capacity 2 containing a single data item (`FIFO2/1`), that
simultaneously originates and receives a data item. After this happens, the
channel unchanged: there is still only 1 data item inside. However, during
animation, there is a moment when the outgoing data item has left the channel
but the incoming has not arrived yet.

For these intermediate states, a coloring may also have an `in` and an `out`
property. These determine what type the channel becomes when a data item enters
or leaves the channel (respectively).

A coloring table for a node or channel type is the set of all possible valid
colorings for that node or channel type. The coloring tables for channels are
defined beforehand (see the section "Channel types"). The coloring tables for
nodes are generated based on the number of sources and sinks they have, and
then cached for efficiency. Read and write nodes are not merged into the
coloring table yet, because their coloring depends on the pending I/O.
 
The colorings for all nodes and channels are merged to generate a connector
coloring. Connector colorings are partially cached: all nodes and channels
without a `next`-property are merged into an (possibly incomplete) coloring,
which is cached. To generate the complete connector coloring, the coloring
tables for all channels *with* a `next`-property are merged into the cached
coloring.
