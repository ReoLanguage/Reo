package nl.cwi.reo.graphgames;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nl.cwi.reo.semantics.Port;

/**
 * Implements a {@link nl.cwi.reo.graphgames.Vertex} used in the scheduling game. 
 */
public class SGVertex extends Vertex {

	/**
	 * Progress of jobs.
	 */
	public final Map<String, Integer> p; 

	/**
	 * State of the WorkAutomaton.
	 */
	public final String q; 

	/**
	 * Synchronization constraint of last action
	 */
	public final Set<Port> N; 

	/**
	 * Progress of jobs during last action
	 */
	public final Map<String, Integer> d;

	/**
	 * Scheduled jobs.
	 */
	public final Set<String> s; 

	/**
	 * Constructs a SGVertex owned by the scheduler.
	 * @param p		progress of jobs
	 * @param q		state of the work automaton
	 * @param N		synchronization constraint of last action
	 * @param d		progress of jobs during last action
	 */
	public SGVertex(Map<String, Integer> p, String q, Set<Port> N, Map<String, Integer> d) {
		super(name(p, q, N, d, new TreeSet<String>(), 0), 0);
		this.p = p;
		this.q = q;
		this.N = N;
		this.d = d;
		this.s = new TreeSet<String>();
	}

	/**
	 * Constructs a SGVertex owned by the application.
	 * @param p		progress of jobs
	 * @param q		state of the work automaton
	 * @param s		scheduled jobs
	 */
	public SGVertex(Map<String, Integer> p, String q, Set<String> s) {
		super(name(p, q, new TreeSet<Port>(), new TreeMap<String, Integer>(), s, 1), 1);
		this.p = p;
		this.q = q;
		this.N = new TreeSet<Port>();
		this.d = new TreeMap<String, Integer>();
		this.s = s;
	}	

	/**
	 * String representation of a SGVertex.
	 * @param p		progress of jobs
	 * @param q		state of the work automaton
	 * @param N		synchronization constraint of last action
	 * @param d		progress of jobs during last action
	 * @param s		scheduled jobs
	 */
	private static String name(Map<String, Integer> p, String q, Set<Port> N, Map<String, Integer> d, Set<String> s, int owner) { 
		String nm = "";
		for(Map.Entry<String, Integer> entry : p.entrySet()) 
			nm += entry.getValue();
		nm += q.toString();
		if (owner == 0) {
			for (Map.Entry<String, Integer> entry : d.entrySet()) 
				nm += entry.getValue();
			for (Port x : N) 
				nm += x;
		} else {
			for (Map.Entry<String, Integer> entry : p.entrySet()) {
				nm += s.contains(entry.getKey()) ? entry.getKey() : "-";
			}
		}
		return nm;
	}
}
