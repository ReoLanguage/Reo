package nl.cwi.reo.interpret.predicates;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.TermsExpression;
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
	private final List<TermsExpression> arguments;
	
	/**
	 * Location of this function application in the Reo source file.
	 */
	private final Location location;
	
	/**
	 * Constructs an application of a relation symbol to a list of arguments.
	 * @param symbol		name of the function
	 * @param arguments		list of arguments
	 * @param token			location in source
	 */
	public Relation(RelationSymbol symbol, List<TermsExpression> arguments, Location location) {
		this.symbol = symbol;
		this.arguments = arguments;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
//		
//		/*
//		 * TODO implement this evaluation. The implementation of corresponds with 
//		 * the evaluation of a StatementList in the previous version. The difference
//		 * is that here we have subformulas instead of statements.
//		 * 
//		 * It is not completely clear how this can be done, but you may have some ideas here.
//		 */
//		
//		Object[] d = new Object[arguments.size()];
//		for (int i = 0; i < arguments.size(); i++) {
//			Term t = arguments.get(i).evaluate(params);
//			if (t instanceof Value) {
//				d[i] = ((Value)t).getValue();
//			} else {
//				return new ArrayList<Map<String, Expression>>();
//			}
//		}
//		
//		switch (symbol) {
//		case EQ:
//			/*
//			 * This corresponds with nl.cwi.reo.interpret.blocks.Definition
//			 */
//			break;
//		case GEQ:
//			/*
//			 * Here, just validate that this relation is satisfied. If not all
//			 * arguments evaluate to a datum, raise an error.
//			 */
//			break;
//		case GT:
//			break;
//		case LEQ:
//			break;
//		case LT:
//			break;
//		case NEQ:
//			break;
//		default:
//			throw new IllegalArgumentException("Undefined operation " + symbol + ".");
//		}
//
//		return new ArrayList<Map<String, Expression>>();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + symbol + arguments;
	}
}
