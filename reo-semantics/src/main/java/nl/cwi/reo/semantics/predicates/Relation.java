package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Relation of a list of terms.
 */
public class Relation implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean relation = true;

	/**
	 * Name of this relation.
	 */
	private final String name;

	// /**
	// * Value of this relation or reference to its implementation.
	// */
	// @Nullable
	// private String value;

	/**
	 * List of arguments of this relation.
	 */
	@Nullable
	private final List<Term> args;

	/**
	 * Free variables in this formula.
	 */
	private final Set<Variable> freeVars;

	/**
	 * Constructs a new relation with a given name and a given list of
	 * arguments.
	 * 
	 * @param name
	 *            name of the relation
	 * @param args
	 *            list of arguments
	 */
	public Relation(String name, @Nullable List<Term> args) {
		this.name = name;
		this.args = args;

		Set<Variable> vars = new HashSet<Variable>();
		if (args != null)
			for (Term t : args)
				vars.addAll(t.getFreeVariables());
		this.freeVars = Collections.unmodifiableSet(vars);

	}

	/**
	 * Gets the name of this relation.
	 * 
	 * @return name of this relation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the list of arguments of this relation.
	 * 
	 * @return list of arguments of this relation.
	 */
	@Nullable
	public List<Term> getArgs() {
		return args;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return freeVars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Formula evaluate(Scope s, Monitor m) {

		// Evaluate the symbol
		String _name = name;
		Value v = s.get(new Identifier(name));
		if (v != null)
			_name = v.toString();

		// Evaluate the arguments
		List<Term> _args = null;
		if (args != null) {
			_args = new ArrayList<>();
			for (Term t : args) {
				Term u = t.evaluate(s, m);
				if (u == null)
					return null;
				_args.add(u);
			}
		}

		return new Relation(_name, _args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return new HashSet<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula DNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Map<Variable, Term> map) {
		if (args == null || Collections.disjoint(freeVars, map.keySet()))
			return this;
		List<Term> _args = new ArrayList<>();
		for (Term t : args)
			_args.add(t.substitute(map));
		return new Relation(this.name, _args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		return new HashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = name;
		if (args != null) {
			s += "(";
			for (Term t : args)
				s += ", " + t;
			s += ")";
		}
		return s;
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
		if (!(other instanceof Relation))
			return false;
		Relation p = (Relation) other;
		return Objects.equals(this.name, p.name) && Objects.equals(this.args, p.args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.args);
	}

}
