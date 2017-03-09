package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.Component;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.terms.Tuple;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a relation over terms.
 */
public final class Relation implements PredicateExpression {

	/**
	 * Name of the relation.
	 */
	private final RelationSymbol symbol;

	/**
	 * List of arguments of this relation.
	 */
	private final List<TermExpression> arguments;

	/**
	 * Location of this relation symbol in the Reo source file.
	 */
	private final Location location;

	/**
	 * Constructs an application of a relation symbol to a list of arguments.
	 * 
	 * @param symbol
	 *            name of the function
	 * @param arguments
	 *            list of arguments
	 * @param token
	 *            location in source
	 */
	public Relation(RelationSymbol symbol, List<TermExpression> arguments, Location location) {
		this.symbol = symbol;
		this.arguments = arguments;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {

		List<Scope> scopes = new ArrayList<Scope>();

		List<Iterator<Term>> iters = new ArrayList<Iterator<Term>>();
		for (TermExpression arg : arguments) {
			List<Term> terms = arg.evaluate(s, m);
			if (terms == null)
				return null;
			iters.add(terms.iterator());
		}

		while (Tuple.hasNext(iters)) {
			List<Term> args = Tuple.next(iters);

			switch (symbol) {
			case EQ:
				if (args.size() == 2) {
					if (args.get(0) instanceof Identifier && args.get(1) instanceof Value) {
						scopes.add(s.extend((Identifier) args.get(0), (Value) args.get(1)));
					} else if (args.get(1) instanceof Identifier && args.get(0) instanceof Value) {
						scopes.add(s.extend((Identifier) args.get(1), (Value) args.get(0)));
					} else if (args.get(0) instanceof IntegerValue && args.get(0) instanceof IntegerValue
							&& ((IntegerValue) args.get(0)).getValue() == ((IntegerValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof DecimalValue && args.get(0) instanceof DecimalValue
							&& ((DecimalValue) args.get(0)).getValue() == ((DecimalValue) args.get(1)).getValue())
						return Arrays.asList(s);
				} else {
					m.add(location, "= takes 2 arguments");
				}
				return scopes;
			case GEQ:
				if (args.size() == 2) {
					if (args.get(0) instanceof Identifier || args.get(0) instanceof Identifier)
						m.add(location, ">= cannot be used to define a variable.");
					else if (args.get(0) instanceof IntegerValue && args.get(0) instanceof IntegerValue
							&& ((IntegerValue) args.get(0)).getValue() >= ((IntegerValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof DecimalValue && args.get(0) instanceof DecimalValue
							&& ((DecimalValue) args.get(0)).getValue() >= ((DecimalValue) args.get(1)).getValue())
						return Arrays.asList(s);
				} else {
					m.add(location, ">= takes 2 arguments");
				}
				return new ArrayList<Scope>();
			case GT:
				if (args.size() == 2) {
					if (args.get(0) instanceof Identifier || args.get(0) instanceof Identifier)
						m.add(location, "> cannot be used to define a variable.");
					else if (args.get(0) instanceof IntegerValue && args.get(0) instanceof IntegerValue
							&& ((IntegerValue) args.get(0)).getValue() > ((IntegerValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof DecimalValue && args.get(0) instanceof DecimalValue
							&& ((DecimalValue) args.get(0)).getValue() > ((DecimalValue) args.get(1)).getValue())
						return Arrays.asList(s);
				} else {
					m.add(location, "> takes 2 arguments");
				}
				return new ArrayList<Scope>();
			case LEQ:
				if (args.size() == 2) {
					if (args.get(0) instanceof Identifier || args.get(0) instanceof Identifier)
						m.add(location, "<= cannot be used to define a variable.");
					else if (args.get(0) instanceof IntegerValue && args.get(0) instanceof IntegerValue
							&& ((IntegerValue) args.get(0)).getValue() <= ((IntegerValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof DecimalValue && args.get(0) instanceof DecimalValue
							&& ((DecimalValue) args.get(0)).getValue() <= ((DecimalValue) args.get(1)).getValue())
						return Arrays.asList(s);
				} else {
					m.add(location, "<= takes 2 arguments");
				}
				return new ArrayList<Scope>();
			case LT:
				if (args.size() == 2) {
					if (args.get(0) instanceof Identifier || args.get(0) instanceof Identifier)
						m.add(location, "< cannot be used to define a variable.");
					else if (args.get(0) instanceof IntegerValue && args.get(0) instanceof IntegerValue
							&& ((IntegerValue) args.get(0)).getValue() < ((IntegerValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof DecimalValue && args.get(0) instanceof DecimalValue
							&& ((DecimalValue) args.get(0)).getValue() < ((DecimalValue) args.get(1)).getValue())
						return Arrays.asList(s);
				} else {
					m.add(location, "< takes 2 arguments");
				}
				return new ArrayList<Scope>();
			case NEQ:
				if (args.size() == 2) {
					if (args.get(0) instanceof Identifier || args.get(0) instanceof Identifier)
						m.add(location, "!= cannot be used to define a variable.");
					else if (args.get(0) instanceof BooleanValue && args.get(0) instanceof BooleanValue
							&& ((BooleanValue) args.get(0)).getValue() != ((BooleanValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof IntegerValue && args.get(0) instanceof IntegerValue
							&& ((IntegerValue) args.get(0)).getValue() != ((IntegerValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof DecimalValue && args.get(0) instanceof DecimalValue
							&& ((DecimalValue) args.get(0)).getValue() != ((DecimalValue) args.get(1)).getValue())
						return Arrays.asList(s);
					else if (args.get(0) instanceof StringValue && args.get(0) instanceof StringValue && !Objects
							.equals(((StringValue) args.get(0)).getValue(), ((StringValue) args.get(1)).getValue()))
						return Arrays.asList(s);
					else if (args.get(0) instanceof Component<?> && args.get(0) instanceof Component<?>
							&& !Objects.equals((Component<?>) args.get(0), ((Component<?>) args.get(1))))
						return Arrays.asList(s);
					else if (args.get(0) instanceof Instance<?> && args.get(0) instanceof Instance<?>
							&& !Objects.equals((Instance<?>) args.get(0), ((Instance<?>) args.get(1))))
						return Arrays.asList(s);
				} else {
					m.add(location, "!= takes 2 arguments");
				}
				return new ArrayList<Scope>();
			default:
				throw new IllegalArgumentException("Undefined operation " + symbol + ".");
			}
		}

		return new ArrayList<Scope>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		for (TermExpression t : arguments)
			vars.addAll(t.getVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return arguments.size() == 2 ? "" + arguments.get(0) + " " + symbol + " " + arguments.get(1) : "" + symbol + arguments;
	}
}
