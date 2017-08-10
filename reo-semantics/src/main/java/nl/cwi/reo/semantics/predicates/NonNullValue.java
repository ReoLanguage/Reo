package nl.cwi.reo.semantics.predicates;

import java.util.Map;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

/**
 * A constant that represents presence of an arbitrary non-null data item.
 * 
 * <p>
 * Although it is formally impossible to represent the presence of an arbitrary
 * non-null datum by a single datum, we can think of this term as a placeholder
 * for an arbitrary non-null datum.
 */
public class NonNullValue extends Function {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean isnonnull = true;

	/**
	 * Constructs an null value.
	 */
	public NonNullValue() {
		super("!*", "nonnull", null, false, new TypeTag(""));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		return new NonNullValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		return new NonNullValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term evaluate(Scope s, Monitor m) {
		return new NonNullValue();
	}

}
