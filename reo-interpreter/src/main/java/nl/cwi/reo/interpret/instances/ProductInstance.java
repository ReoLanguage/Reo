package nl.cwi.reo.interpret.instances;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductInstance.
 *
 * @param <T>
 *            the generic type
 */
public final class ProductInstance implements InstanceExpression {

	/**
	 * Composition operator name.
	 */
	private final TermExpression operator;

	/**
	 * First instance.
	 */
	private final InstanceExpression first;

	/**
	 * Second instance.
	 */
	private final InstanceExpression second;

	/**
	 * Location of this instance in Reo source file.
	 */
	private final Location location;

	/**
	 * Constructs a new composition of instances.
	 *
	 * @param operator
	 *            composition operator
	 * @param first
	 *            first instance
	 * @param second
	 *            second instance
	 * @param location
	 *            the location
	 */
	public ProductInstance(TermExpression operator, InstanceExpression first, InstanceExpression second,
			Location location) {
		this.operator = operator;
		this.first = first;
		this.second = second;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance evaluate(Scope s, Monitor m) {

		List<Term> t = this.operator.evaluate(s, m);
		if (t == null || t.isEmpty() || !(t.get(0) instanceof StringValue)) {
			m.add(location, "Composition operator " + operator + " must be of type string.");
			return null;
		}

		String operator = ((StringValue) t.get(0)).getValue();
		Instance i1 = first.evaluate(s, m);
		Instance i2 = second.evaluate(s, m);

		if (i1 == null || i2 == null)
			return null;

		List<ReoConnector> components = Arrays.asList(i1.getConnector(), i2.getConnector());
		ReoConnector connector = new ReoConnectorComposite(null, operator, components);
		Set<Set<Identifier>> unifications = new HashSet<Set<Identifier>>(i1.getUnifications());
		unifications.addAll(i1.getUnifications());

		return new Instance(connector, unifications);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> union = first.getVariables();
		union.addAll(second.getVariables());
		return union;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + first + operator + second;
	}

}
