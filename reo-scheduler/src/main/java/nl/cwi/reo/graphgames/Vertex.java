package nl.cwi.reo.graphgames;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

// TODO: Auto-generated Javadoc
/**
 * Implements a vertex in a {@link nl.cwi.reo.graphgames.GameGraph}.
 */
public class Vertex implements Comparable<Vertex> {

	/**
	 * Vertex name.
	 */
	public final String name;

	/**
	 * Owner of this vertex, which is either Player 0 or Player 1.
	 */
	public final int owner;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            vertex name
	 * @param owner
	 *            owner=0,1
	 */
	public Vertex(String name, int owner) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.owner = owner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Vertex))
			return false;
		Vertex v = (Vertex) other;
		return Objects.equals(this.name, v.name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Vertex other) {
		return this.name.compareTo(other.name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
}
