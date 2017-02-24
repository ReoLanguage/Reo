package nl.cwi.reo.graphgames;

/**
 * Implements an edge in a {@link nl.cwi.reo.graphgames.GameGraph}.
 */
public class Edge implements Comparable<Edge> {

	/**
	 * Source vertex.
	 */
	public final Vertex source;
	
	/** 
	 * Target vertex.
	 */
	public final Vertex  target;

	/**
	 * Weight of the edge.
	 */
	public final int weight;

	/**
	 * Duration of the edge.
	 */
	public final int time;

	/**
	 * Constructor.
	 * @param a		source vertex
	 * @param b		target vertex
	 * @param w		weight of the edge
	 * @param t		duration of the edge
	 */
	public Edge(Vertex a, Vertex b, int w, int t) {
		this.source = a;
		this.target = b;
		this.weight = w;
		this.time = t;
	}

	public int compareTo(Edge other) {
		return this.toString().compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
		if (!(other instanceof Edge)) return false;
		Edge e = (Edge)other;
		return e.source.equals(this.source) && e.target.equals(this.target);
	}
	
	@Override
	public String toString() { 
		return "(" + this.source.name + ", " + this.target.name + ")";
	}
}


