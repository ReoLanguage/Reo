# Animation
A colored connector can be animated; this shows the flow of data items for that
specific coloring. In the past[2], Reo connector animation has been implemented
by writing for each channel and coloring an 'animation specification'. The Reo
Playground takes a significantly simpler approach.

The Reo.Animation object manages the drawing of the connector and the animation
of a single coloring.

## Animation steps
An animation of a single coloring consists of several steps. The most important
steps are:

- `origin`
- `propagateIn`
- `propagateOut`
- `propagateNext`

In each step, ends that have data items are either one of two lists: the
`pending` list, which contains all ends that can dispense a data item
immediately, and the `blocked` list, which contains all ends that must wait for
another data item to arrive first (such as sink ends on a drain). Whenever a
data item is placed in the `pending` list, it is also marked so that it is not
visited again.

When the `pending` list is empty, the animation is finished. A callback
function can be attached to the Reo.Animation object to be notified of this.

### `origin`
In the `origin` step, data items originating from channels (see also the
section "Channel types" in Connectors) are drawn. Simultaneously for each
originating channel, on all flow-colored sink ends, a data item is moved from
the middle of the channel to that sink end.

After this, all sink ends of nodes at these channel sink ends are pending.
When the data items reach the sink nodes, or if there are no originating
channels, this step finishes and the `propagateIn` step starts.

### `propagateIn`
In the `propagateIn` step, simultaneously for all pending ends, a data item is
moved from the node at that end into the channel that the end is connected to.
When the data items reach the middle of their channels, this step finishes and
the `propagateOut` step starts.

### `propagateOut`
In the `propagateOut` step, simultaneously for all pending ends, a data item is
moved from the channel that the end is connected to the node at the *other*
channel end.  When the data items reach the node at the other channel end, this
step finishes and the `propagateNext` step starts.

### `propagateNext`
In the `propagateNext` step, no animation actually takes place. Instead, the
next set of pending and blocked nodes is determined. After this, if there are
pending data items, the `propagateIn` step starts.

## Channel types during animation
Animations are performed over single colorings. This means that during a single
animation, a connector remains unchanged. However, as mentioned in the section
"Channel types", channels with multiple configurations are implemented as
multiple channel types; during an animation, data items moving around may leave
a channel in an intermediate state (see also the section "Coloring").

During animation, a channel can have an 'animation type' applied to it to
handle these intermediate states. When a channel is drawn, the animation type
(if there is one) always takes precedence over the actual type. Note that the
animation type of a channel is purely visual: none of its semantic information
is used.
