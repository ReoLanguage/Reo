package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;

public class SynchrGraph implements Iterable<SynchrEdge> {
	private final Set<SynchrEdge> edges = new HashSet<>();
	private final PortFactory portFactory;

	public SynchrGraph(PortFactory portFactory, Set<SynchrEdge> edges) {
		if (portFactory == null)
			throw new NullPointerException();
		if (edges == null)
			throw new NullPointerException();
		if (edges.contains(null))
			throw new NullPointerException();

		this.edges.addAll(edges);
		this.portFactory = portFactory;
	}

	public void combineEdges() {
		Map<SynchrEdge, Set<SynchrEdge>> uncombinables = new HashMap<>();

		while (true) {
			SynchrEdge addEdge = null;
			SynchrEdge removeEdge1 = null;
			SynchrEdge removeEdge2 = null;

			loops: {
				for (SynchrEdge edg1 : edges)
					for (SynchrEdge edg2 : edges)
						if (edg1 != edg2
								&& ((uncombinables.containsKey(edg1) && !uncombinables
										.get(edg1).contains(edg2))
										|| (uncombinables.containsKey(edg2) && !uncombinables
												.get(edg2).contains(edg1)) || (!uncombinables
										.containsKey(edg1) && !uncombinables
										.containsKey(edg2)))) {

							if (edg1.getSize() != edg2.getSize()) {
								if (!uncombinables.containsKey(edg1))
									uncombinables.put(edg1,
											new HashSet<SynchrEdge>());

								uncombinables.get(edg1).add(edg2);
								continue;
							}

							Set<SynchrVertex> vertices = new HashSet<>();
							Set<SynchrVertex> vertices1 = new HashSet<>();
							Set<SynchrVertex> vertices2 = new HashSet<>();
							edg1.addVerticesToSet(vertices1);
							edg2.addVerticesToSet(vertices2);

							int distance = 0;
							for (SynchrVertex v : edg1) {
								if (edg2.contains(v)) {
									vertices.add(v);
									vertices1.remove(v);
									vertices2.remove(v);
								}

								else
									distance++;

								if (distance > 1)
									break;
							}
							

							if (distance == 1) {
								PortSet ports = portFactory.newSet();
								vertices1.iterator().next()
										.addPortsToSet(ports);
								vertices2.iterator().next()
										.addPortsToSet(ports);
								vertices.add(new SynchrVertex(portFactory,
										ports));

								addEdge = new SynchrEdge(portFactory, vertices);

								removeEdge1 = edg1;
								removeEdge2 = edg2;
								break loops;
							}

							else {
								if (!uncombinables.containsKey(edg1))
									uncombinables.put(edg1,
											new HashSet<SynchrEdge>());

								uncombinables.get(edg1).add(edg2);
							}
						}
			}

			if (addEdge == null)
				return;

			edges.remove(removeEdge1);
			edges.remove(removeEdge2);
			edges.add(addEdge);
		}
	}

	public Collection<PortSet> divide(PortSet ports) {
		if (ports == null)
			throw new NullPointerException();
		if (ports.getFactory() != portFactory)
			throw new IllegalStateException();

		Map<Set<SynchrEdge>, PortSet> remaindersToPorts = new HashMap<>();
		for (Port p : ports) {
			Set<SynchrEdge> remainders = getEdgeRemainders(p);
			if (remaindersToPorts.containsKey(remainders))
				remaindersToPorts.put(remainders, portFactory.newSet());

			remaindersToPorts.get(remainders).add(p);
		}

		return remaindersToPorts.values();
	}

	public Set<SynchrEdge> getEdgeRemainders(Port port) {
		if (port.getFactory() != portFactory)
			throw new IllegalStateException();

		Set<SynchrEdge> remainders = new HashSet<>();
		for (SynchrEdge edg : edges) {
			if (edg.contains(port))
				remainders.add(edg);
		}

		return remainders;
	}

	@Override
	public Iterator<SynchrEdge> iterator() {
		return edges.iterator();
	}

	@Override
	public String toString() {
		List<String> strings = new ArrayList<>();
		for (SynchrEdge edg : edges)
			strings.add(edg.toString());

		return strings.toString();
	}
}

class SynchrEdge implements Iterable<SynchrVertex> {
	private final Set<SynchrVertex> vertices = new HashSet<>();
	private final PortFactory portFactory;
	private final PortSet ports;
	private final int hashCode;

	SynchrEdge(PortFactory portFactory, PortSet ports) {
		if (portFactory == null)
			throw new NullPointerException();
		if (ports == null)
			throw new NullPointerException();
		if (portFactory != ports.getFactory())
			throw new IllegalArgumentException();

		this.portFactory = portFactory;
		this.ports = portFactory.newSet();
		for (Port p : ports)
			vertices.add(new SynchrVertex(portFactory, p));

		this.hashCode = vertices.hashCode();
	}

	SynchrEdge(PortFactory portFactory, Set<SynchrVertex> vertices) {
		if (portFactory == null)
			throw new NullPointerException();
		if (vertices == null)
			throw new NullPointerException();
		if (vertices.contains(null))
			throw new NullPointerException();

		this.portFactory = portFactory;
		this.ports = portFactory.newSet();
		this.vertices.addAll(vertices);
		this.hashCode = vertices.hashCode();
	}

	public void addVerticesToSet(Set<SynchrVertex> set) {
		if (set == null)
			throw new NullPointerException();
		if (set.contains(null))
			throw new NullPointerException();

		set.addAll(vertices);
	}

	public boolean contains(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (port.getFactory() != portFactory)
			throw new IllegalStateException();

		if (ports.isEmpty())
			for (SynchrVertex v : vertices)
				v.addPortsToSet(ports);

		return ports.contains(port);
	}

	public boolean contains(SynchrVertex vertex) {
		if (vertex == null)
			throw new NullPointerException();

		return vertices.contains(vertex);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();

		return obj instanceof SynchrEdge && equals((SynchrEdge) obj);
	}

	public boolean equals(SynchrEdge edge) {
		if (edge == null)
			throw new NullPointerException();

		return vertices.equals(edge.vertices);
	}

	public SynchrEdge getRemainder(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (port.getFactory() != portFactory)
			throw new IllegalStateException();

		Set<SynchrVertex> remainderVertices = new HashSet<>();
		for (SynchrVertex v : vertices)
			if (!v.contains(port))
				remainderVertices.add(v);

		return new SynchrEdge(portFactory, remainderVertices);
	}

	public int getSize() {
		return vertices.size();
	}

	public int hashCode() {
		return hashCode;
	}

	@Override
	public Iterator<SynchrVertex> iterator() {
		return vertices.iterator();
	}

	@Override
	public String toString() {
		List<String> strings = new ArrayList<>();
		for (SynchrVertex v : vertices)
			strings.add(v.toString());

		return strings.toString();
	}
}

class SynchrVertex implements Iterable<Port> {
	private final PortSet ports;
	private final int hashCode;

	SynchrVertex(PortFactory portFactory, Port port) {
		if (portFactory == null)
			throw new NullPointerException();
		if (port == null)
			throw new NullPointerException();
		if (portFactory != port.getFactory())
			throw new IllegalArgumentException();

		this.ports = portFactory.newSet();
		this.ports.add(port);
		this.hashCode = ports.hashCode();
	}

	SynchrVertex(PortFactory portFactory, PortSet ports) {
		if (portFactory == null)
			throw new NullPointerException();
		if (ports == null)
			throw new NullPointerException();
		if (portFactory != ports.getFactory())
			throw new IllegalArgumentException();

		this.ports = portFactory.newSet();
		this.ports.addAll(ports);
		this.hashCode = ports.hashCode();
	}

	public void addPortsToSet(PortSet set) {
		if (set == null)
			throw new NullPointerException();

		set.addAll(ports);
	}

	public boolean contains(Port port) {
		if (port == null)
			throw new NullPointerException();

		return ports.contains(port);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();

		return obj instanceof SynchrVertex && equals((SynchrVertex) obj);
	}

	public boolean equals(SynchrVertex vertex) {
		if (vertex == null)
			throw new NullPointerException();

		return ports.equals(vertex.ports);
	}

	public int getSize() {
		return ports.count();
	}
	
	public int hashCode() {
		return hashCode;
	}

	@Override
	public Iterator<Port> iterator() {
		return ports.iterator();
	}
	
	@Override
	public String toString() {
		return ports.getSorted().toString();
	}
}