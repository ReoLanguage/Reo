package nl.cwi.reo.graphgames;

import java.util.Objects;

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
	public final Vertex target;

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
	 * 
	 * @param a
	 *            source vertex
	 * @param b
	 *            target vertex
	 * @param w
	 *            weight of the edge
	 * @param t
	 *            duration of the edge
	 */
	public Edge(Vertex a, Vertex b, int w, int t) {
		if (a == null || b == null)
			throw new NullPointerException();
		this.source = a;
		this.target = b;
		this.weight = w;
		this.time = t;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Edge other) {
		return other.source.equals(this.source) ? other.source.compareTo(this.source)
				: other.target.compareTo(this.target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Edge))
			return false;
		Edge e = (Edge) other;
		return Objects.equals(this.source, e.source) && Objects.equals(this.target, e.target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" + this.source.name + ", " + this.target.name + ")";
	}
}
