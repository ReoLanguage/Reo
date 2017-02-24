package nl.cwi.reo.graphgames;

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
	 * @param name		vertex name
	 * @param owner		owner=0,1
	 */
	public Vertex(String name, int owner) {
		this.name = name;
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
		if (!(other instanceof Vertex)) return false;
		Vertex v = (Vertex)other;
		return v.name.equals(this.name);
	}

	public int compareTo(Vertex other) {
		return name.compareTo(other.name);
	}
	
	@Override
	public String toString() { 
		return name;
	}
}
