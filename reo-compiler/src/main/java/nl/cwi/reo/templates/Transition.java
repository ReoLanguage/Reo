package nl.cwi.reo.templates;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.prba.Distribution;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;

// TODO: Auto-generated Javadoc
/**
 * The Class Transition.
 */
public final class Transition {

	/** Guard. */
	private final Formula guard;

	/** Set of input ports. */
	private final Set<Port> input;

	/** Output update. */
	private final Map<PortVariable, Term> output;

	/** Memory update. */
	private final Map<MemoryVariable, Term> memory;

	/** Maude variable. */
	static int instancecounter = 0;

	/** The map M. */
	private Map<MemoryVariable, Integer> mapM = new HashMap<>();

	/** The map in M. */
	private Map<String, String> mapInM = new HashMap<>();

	/** The map in P. */
	private Map<String, String> mapInP = new HashMap<>();

	/** The map out M. */
	private Map<String, String> mapOutM = new HashMap<>();

	/** The map out P. */
	private Map<String, String> mapOutP = new HashMap<>();

	/** The nb. */
	private int nb;

	/**
	 * Constructs a new transition.
	 * 
	 * @param guard
	 *            guard
	 * @param output
	 *            output provided at output ports
	 * @param memory
	 *            update of the memory cells
	 */
	public Transition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory) {
		if (guard == null)
			throw new IllegalArgumentException("No guard specified.");
		if (output == null)
			throw new IllegalArgumentException("No output values specified.");
		if (memory == null)
			throw new IllegalArgumentException("No memory update specified.");
		this.guard = guard;
		this.output = Collections.unmodifiableMap(output);
		this.memory = Collections.unmodifiableMap(memory);
		Set<Port> I = new HashSet<Port>();
		// find all *used* ports in the formula and the terms.
		this.input = I;
		instancecounter++;
		nb = instancecounter;
	}

	/**
	 * Instantiates a new transition.
	 *
	 * @param guard
	 *            the guard
	 * @param output
	 *            the output
	 * @param memory
	 *            the memory
	 * @param input
	 *            the input
	 */
	public Transition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory,
			Set<Port> input) {
		if (guard == null)
			throw new IllegalArgumentException("No guard specified.");
		if (output == null)
			throw new IllegalArgumentException("No output values specified.");
		if (memory == null)
			throw new IllegalArgumentException("No memory update specified.");
		this.guard = guard;
		this.output = Collections.unmodifiableMap(output);
		this.memory = Collections.unmodifiableMap(memory);
		this.input = Collections.unmodifiableSet(input);
		instancecounter++;
		nb = instancecounter;
	}

	/**
	 * Gets the set of guards.
	 * 
	 * @return guard
	 */
	public Formula getGuard() {
		return this.guard;
	}

	/**
	 * Gets the set of input ports that participate in this transition.
	 * 
	 * @return set of input ports
	 */
	public Set<Port> getInput() {
		return this.input;
	}

	/**
	 * Gets the values assigned to the output ports.
	 * 
	 * @return assignment of terms to output ports.
	 */
	public Map<PortVariable, Term> getOutput() {
		return this.output;
	}

	/**
	 * Retrieves the values assigned to memory cells.
	 * 
	 * @return assignment of terms to memory cells.
	 */
	public Map<MemoryVariable, Term> getMemory() {
		return this.memory;
	}

	/**
	 * Computes
	 * 
	 * @return
	 */
	public Map<Map<MemoryVariable, Term>, Term> getPRISMUpdate() {

		// TODO
		Map<Map<MemoryVariable, Term>, Term> update = new HashMap<>();

		// Flatten the distribution terms in the memory update
		Map<MemoryVariable, Term> mem2 = new HashMap<>();
		for (Map.Entry<MemoryVariable, Term> entry : memory.entrySet())
			mem2.put(entry.getKey(), Transition.flattenDistribution(entry.getValue()));

		return null;
	}

	// private static Map<Map<MemoryVariable, Term>, Term>
	// invert(Map<MemoryVariable, Term> memory) {
	// Iterator<Map.Entry<Term, Term>> iter = memory.entrySet().iterator();
	// Term m =
	// return null;
	// }

	private static Term flattenDistribution(Term t) {
		if (t instanceof Distribution) {
			Map<Term, Term> newDistr = new HashMap<>();
			Distribution d = (Distribution) t;
			for (Map.Entry<Term, Term> entry : d.getDistribution().entrySet()) {
				Term f = flattenDistribution(entry.getKey());
				if (f instanceof Distribution) {
					for (Map.Entry<Term, Term> ef : ((Distribution) f).getDistribution().entrySet())
						newDistr.put(ef.getKey(), new Function("*", null,
								Arrays.asList(entry.getValue(), ef.getValue()), false, ef.getValue().getTypeTag()));
				} else {
					newDistr.put(f, entry.getValue());
				}
			}
			return new Distribution(newDistr);
		}
		return t;
	}

	/**
	 * Gets the map M.
	 *
	 * @return the map M
	 */
	public Map<MemoryVariable, Integer> getMapM() {
		Set<MemoryVariable> s = this.memory.keySet();
		Map<MemoryVariable, Integer> map = new HashMap<>();
		for (MemoryVariable m : s) {
			map.put(m, Integer.parseInt(m.getName().substring(1)));
		}
		this.mapM = map;
		return this.mapM;
	}

	/**
	 * Gets the map in M.
	 *
	 * @return the map in M
	 */
	public Map<String, String> getMapInM() {
		Set<Term> s = new HashSet<>(this.output.values());
		s.addAll(new HashSet<>(memory.values()));
		Map<String, String> map = new HashMap<>();
		for (Term n : s) {
			if (n instanceof MemoryVariable)
				map.put(((MemoryVariable) n).getName().substring(1), "d_" + ((MemoryVariable) n).getName());
		}
		for (MemoryVariable m : memory.keySet()) {
			if (!map.containsKey(((MemoryVariable) m).getName().substring(1))) {
				map.put(((MemoryVariable) m).getName().substring(1), "*");
			}
		}
		this.mapInM = map;
		return this.mapInM;
	}

	/**
	 * Gets the map in P.
	 *
	 * @return the map in P
	 */
	public Map<String, String> getMapInP() {
		Set<Term> s = new HashSet<>(this.output.values());
		s.addAll(new HashSet<>(memory.values()));
		Map<String, String> map = new HashMap<>();
		for (Term n : s) {
			if (n instanceof PortVariable)
				map.put(((PortVariable) n).getName().substring(1), "d" + ((PortVariable) n).getName());
		}
		for (PortVariable m : output.keySet()) {
			if (!map.containsKey(((PortVariable) m).getName().substring(1))) {
				map.put(((PortVariable) m).getName().substring(1), "*");
			}
		}
		this.mapInP = map;
		return this.mapInP;
	}

	/**
	 * Gets the map out P.
	 *
	 * @return the map out P
	 */
	public Map<String, String> getMapOutP() {
		Map<String, String> map = new HashMap<>();
		for (PortVariable m : output.keySet()) {
			if (output.get(m) instanceof MemoryVariable)
				map.put(m.getName().substring(1), "d_" + ((MemoryVariable) (output.get(m))).getName());
			if (output.get(m) instanceof PortVariable)
				map.put(m.getName().substring(1), "d" + ((PortVariable) output.get(m)).getName());
		}
		Set<Term> s = new HashSet<>(this.output.values());
		s.addAll(new HashSet<>(memory.values()));
		for (Term m : s) {
			if (m instanceof PortVariable)
				map.put(((PortVariable) m).getName().substring(1), "*");
		}
		this.mapOutP = map;
		return this.mapOutP;
	}

	/**
	 * Gets the map out M.
	 *
	 * @return the map out M
	 */
	public Map<String, String> getMapOutM() {
		Map<String, String> map = new HashMap<>();
		for (MemoryVariable m : memory.keySet()) {
			if (memory.get(m) instanceof MemoryVariable)
				map.put(m.getName().substring(1), "d_" + ((MemoryVariable) (memory.get(m))).getName());
			if (memory.get(m) instanceof PortVariable)
				map.put(m.getName().substring(1), "d" + ((PortVariable) memory.get(m)).getName());
		}

		Set<Term> s = new HashSet<>(this.output.values());
		s.addAll(new HashSet<>(memory.values()));
		for (Term m : s) {
			if (m instanceof MemoryVariable)
				map.put(((MemoryVariable) m).getName().substring(1), "*");
		}
		this.mapOutM = map;
		return this.mapOutM;
	}

	/**
	 * Gets the nb.
	 *
	 * @return the nb
	 */
	public int getNb() {
		return nb;
	}

	/**
	 * Gets the set of ports that participate in this transition.
	 * 
	 * @return set of ports that participate in this transition
	 */
	public Set<Port> getInterface() {
		Set<Port> ports = new HashSet<Port>(input);
		for (PortVariable x : output.keySet())
			ports.add(x.getPort());
		return ports;
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
		if (!(other instanceof Transition))
			return false;
		Transition rule = (Transition) other;
		return Objects.equals(this.guard, rule.getGuard()) && Objects.equals(this.output, rule.getOutput())
				&& Objects.equals(this.memory, rule.getMemory()) && Objects.equals(this.input, rule.getInput());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.guard, this.output, this.memory, this.input);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return input + " " + guard + " -> " + output + ", " + memory;
	}
}
