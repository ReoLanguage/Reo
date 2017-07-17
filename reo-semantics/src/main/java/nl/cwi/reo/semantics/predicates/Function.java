package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * A function applied to a list of terms.
 */
public class Function implements Term {

	/**
	 * Flag for string template.
	 */
	public static final boolean function = true;

	/**
	 * Name of this function.
	 */
	private final String name;

	/**
	 * Value of this function or reference to its implementation.
	 */
	@Nullable
	private Object value;

	/**
	 * List of arguments of this function.
	 */
	@Nullable
	private final List<Term> args;

	/**
	 * Constructs a new function from a name, a value, and a list of arguments.
	 *
	 * @param name
	 *            name of the function
	 * @param value
	 *            value of this function
	 * @param args
	 *            list of arguments
	 */
	public Function(String name, @Nullable Object value, @Nullable List<Term> args) {
		this.name = name;
		this.value = value;
		this.args = args;
	}

	/**
	 * Gets the name of this function.
	 * 
	 * @return name of this function.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the value of this function.
	 * 
	 * @return value of this function.
	 */
	@Nullable
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the list of arguments of this function.
	 * 
	 * @return list of arguments of this function.
	 */
	@Nullable
	public List<Term> getArgs() {
		return args;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = name;
		if (args != null && !args.isEmpty()) {
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
	public boolean hadOutputs() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		List<Term> list = new ArrayList<Term>();
		if (args != null)
			for (Term s : args)
				list.add(s.rename(links));
		return new Function(name, value, list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		List<Term> list = new ArrayList<Term>();
		if (args != null) {
			for (Term s : args)
				list.add(s.substitute(t, x));
		}
		return new Function(name, value, list);
	}

	/**
	 * {@inheritDoc}
	 */
	public Term evaluate(Scope s, Monitor m) {
		String valueEval = "";
		if (value instanceof String) {
			Value v = s.get(new Identifier((String) value));
			if (v != null) {
				valueEval = v.toString();
			} else {
				m.add("Cannot evaluate this function");
			}
		} else {
			m.add("Cannot evaluate this function");
		}

		if (valueEval.substring(0, 1).equals("\""))
			valueEval = valueEval.substring(1, valueEval.length());
		if (valueEval.substring(valueEval.length() - 1, valueEval.length()).equals("\""))
			valueEval = valueEval.substring(0, valueEval.length() - 1);

		return new Function(name, valueEval, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		if (args != null) {
			for (Term t : args) {
				if (t != null) {
					Set<Variable> freevars = t.getFreeVariables();
					if (freevars != null)
						vars.addAll(freevars);
				}
			}
		}
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public TypeTag getTypeTag() {
		// TODO infer type tags of functions.
		return null;
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
		if (!(other instanceof Function))
			return false;
		Function func = (Function) other;
		return (Objects.equals(this.name, func.name) && Objects.equals(this.value, func.value))
				&& Objects.equals(this.args, func.args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.value, this.args);
	}

}
