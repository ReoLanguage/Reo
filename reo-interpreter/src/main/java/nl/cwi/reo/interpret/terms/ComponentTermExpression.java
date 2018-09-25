package nl.cwi.reo.interpret.terms;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.Component;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a component definition expression as a term.
 * 
 * @param 
 *            Reo semantics type
 */
public class ComponentTermExpression implements TermExpression {

	/**
	 * Component definition.
	 */
	private ComponentExpression component;

	/**
	 * Constructs a new component definition term.
	 *
	 * @param component
	 *            component definition
	 */
	public ComponentTermExpression(ComponentExpression component) {
		if (component == null)
			throw new NullPointerException();
		this.component = component;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Term> evaluate(Scope s, Monitor m) {
		Component comp = this.component.evaluate(s, m);
		if (comp == null)
			return null;
		return Arrays.asList(comp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		return component.getVariables();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return component.toString();
	}
}
