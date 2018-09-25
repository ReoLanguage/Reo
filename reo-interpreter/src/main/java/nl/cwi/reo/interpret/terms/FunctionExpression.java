package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * An application of a function symbol to a list of arguments.
 */
public final class FunctionExpression implements TermExpression {

	/**
	 * Name of the function.
	 */
	private final FunctionSymbol symbol;

	/**
	 * List of arguments of this function.
	 */
	private final List<TermExpression> arguments;

	/**
	 * Location of this function application in the Reo source file.
	 */
	private final Location location;

	/**
	 * Constructs an application of a function symbol to a list of arguments.
	 * 
	 * @param symbol
	 *            name of the function
	 * @param arguments
	 *            list of arguments
	 * @param location
	 *            location of this function in Reo source file
	 */
	public FunctionExpression(FunctionSymbol symbol, List<TermExpression> arguments, Location location) {
		this.symbol = symbol;
		this.arguments = arguments;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Term> evaluate(Scope s, Monitor m) {

		List<Term> list = new ArrayList<Term>();

		List<Iterator<Term>> iters = new ArrayList<Iterator<Term>>();
		for (TermExpression arg : arguments) {
			List<Term> terms = arg.evaluate(s, new Monitor());
			if (terms == null)
				return null;
			iters.add(terms.iterator());
		}

		while (Tuple.hasNext(iters)) {
			List<Term> args = Tuple.next(iters);
			switch (symbol) {
			case ADD:
				if (arguments.size() == 2) {
					if (args.get(0) instanceof IntegerValue && args.get(1) instanceof IntegerValue) {
						list.add(IntegerValue.add((IntegerValue) args.get(0), (IntegerValue) args.get(1)));
					} else if (args.get(0) instanceof StringValue && args.get(1) instanceof StringValue) {
						list.add(StringValue.concat((StringValue) args.get(0), (StringValue) args.get(1)));
					} else if (args.get(0) instanceof DecimalValue && args.get(1) instanceof DecimalValue) {
						list.add(DecimalValue.add((DecimalValue) args.get(0), (DecimalValue) args.get(1)));
					} else {
						m.add(location, "Undefined addition of " + args.get(0) + " and " + args.get(1) + ".");
						return null;
					}
				} else {
					m.add(location, "Addition + takes 2 arguments.");
				}
				break;
			case DIV:
				if (arguments.size() == 2) {
					if (args.get(0) instanceof IntegerValue && args.get(1) instanceof IntegerValue) {
						IntegerValue x = IntegerValue.div((IntegerValue) args.get(0), (IntegerValue) args.get(1));
						if (x != null) {
							list.add(x);
						} else {
							m.add(location, "Cannot divide by zero.");
							return null;
						}
					} else if (args.get(0) instanceof DecimalValue && args.get(1) instanceof DecimalValue) {
						DecimalValue x = DecimalValue.div((DecimalValue) args.get(0), (DecimalValue) args.get(1));
						if (x != null) {
							list.add(x);
						} else {
							m.add(location, "Cannot divide by zero.");
							return null;
						}
					} else {
						m.add(location, "Undefined division of " + args.get(0) + " and " + args.get(1) + ".");
						return null;
					}
				} else {
					m.add(location, "Division / takes 2 arguments.");
				}
				break;
			case MIN:
				if (arguments.size() == 1) {
					if (args.get(0) instanceof IntegerValue) {
						list.add(IntegerValue.min((IntegerValue) args.get(0)));
					} else if (args.get(0) instanceof DecimalValue) {
						list.add(DecimalValue.min((DecimalValue) args.get(0)));
					} else {
						m.add(location, "Undefined inversion of " + args.get(0) + ".");
						return null;
					}
				} else if (arguments.size() == 2) {
					if (args.get(0) instanceof IntegerValue && args.get(1) instanceof IntegerValue) {
						list.add(IntegerValue.min((IntegerValue) args.get(0), (IntegerValue) args.get(1)));
					} else if (args.get(0) instanceof DecimalValue && args.get(1) instanceof DecimalValue) {
						list.add(DecimalValue.min((DecimalValue) args.get(0), (DecimalValue) args.get(1)));
					} else {
						m.add(location, "Undefined subtraction of " + args.get(0) + " and " + args.get(1) + ".");
						return null;
					}
				} else {
					m.add(location, "Minus - takes 1 or 2 arguments.");
				}
				break;
			case MOD:
				if (arguments.size() == 2) {
					if (args.get(0) instanceof IntegerValue && args.get(1) instanceof IntegerValue) {
						IntegerValue x = IntegerValue.mod((IntegerValue) args.get(0), (IntegerValue) args.get(1));
						if (x != null) {
							list.add(x);
						} else {
							m.add(location, "Modulus cannot be zero.");
							return null;
						}
					} else {
						m.add(location, "Undefined division of " + args.get(0) + " and " + args.get(1) + ".");
						return null;
					}
				} else {
					m.add(location, "Division / takes 2 arguments.");
				}
				break;
			case MUL:
				if (arguments.size() == 2) {
					if (args.get(0) instanceof IntegerValue && args.get(1) instanceof IntegerValue) {
						list.add(IntegerValue.mul((IntegerValue) args.get(0), (IntegerValue) args.get(1)));
					} else if (args.get(0) instanceof DecimalValue && args.get(1) instanceof DecimalValue) {
						list.add(DecimalValue.mul((DecimalValue) args.get(0), (DecimalValue) args.get(1)));
					} else {
						m.add(location, "Undefined multiplication of " + args.get(0) + " and " + args.get(1) + ".");
						return null;
					}
				} else {
					m.add(location, "Multiplication * takes 2 arguments.");
				}
				break;
			case POW:
				if (arguments.size() == 2) {
					if (args.get(0) instanceof IntegerValue && args.get(1) instanceof IntegerValue) {
						list.add(IntegerValue.exp((IntegerValue) args.get(0), (IntegerValue) args.get(1)));
					} else if (args.get(0) instanceof DecimalValue && args.get(1) instanceof DecimalValue) {
						list.add(DecimalValue.exp((DecimalValue) args.get(0), (DecimalValue) args.get(1)));
					} else {
						m.add(location, "Undefined exponentiation of " + args.get(0) + " and " + args.get(1) + ".");
						return null;
					}
				} else {
					m.add(location, "Exponentiation ^ takes 2 arguments.");
				}
				break;
			default:
				throw new IllegalArgumentException("Undefined operation " + symbol + ".");
			}
		}

		return list;
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
		return "" + symbol + arguments;
	}
}
