package nl.cwi.reo.interpret.statements;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.terms.Range;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.terms.VariableTermExpression;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ConjunctionTests.
 */
public class ConjunctionTests {

	/**
	 * Evaluate original has no types.
	 */
	@Test
	public void evaluate_OriginalHasNoTypes() {

		/*
		 * Tests if x:<1..k> && k=3 evaluates to [{x=1, k=3}, {x=2, k=3}, {x=3,
		 * k=3}]
		 */

		Scope s = new Scope();
		Monitor m = new Monitor();
		Location loc = new Location("myfile.treo", 1, 1);

		List<TermExpression> indices = new ArrayList<TermExpression>();

		VariableExpression vark = new VariableExpression("k", indices, loc);

		TermExpression t1 = new IntegerValue(1);
		TermExpression t3 = new IntegerValue(3);
		TermExpression k = new VariableTermExpression(vark);

		Range rng = new Range(t1, t3);

		ListExpression list = new ListExpression(Arrays.asList(rng));

		Identifier x = new Identifier("x");

		PredicateExpression P1 = new Membership(x, list);
		PredicateExpression P2 = new Relation(RelationSymbol.EQ, Arrays.asList(k, t3), loc);

		Conjunction c = new Conjunction(Arrays.asList(P1, P2));

		List<Scope> scopes = c.evaluate(s, m);

		assertEquals(scopes.size(), 3);
		assertEquals(scopes.get(0).get(x), new IntegerValue(1));
		assertEquals(scopes.get(1).get(x), new IntegerValue(2));
		assertEquals(scopes.get(2).get(x), new IntegerValue(3));
	}

	/**
	 * Evaluate predicate.
	 */
	@Test
	public void evaluate_Predicate() {

		/*
		 * Tests if x:<1..k> && k=3 evaluates to [{x=1, k=3}, {x=2, k=3}, {x=3,
		 * k=3}]
		 */

		Scope s = new Scope();
		Monitor m = new Monitor();
		Location loc = new Location("myfile.treo", 1, 1);

		List<TermExpression> indices = new ArrayList<TermExpression>();

		VariableExpression vark = new VariableExpression("k", indices, loc);

		TermExpression t1 = new IntegerValue(1);
		TermExpression t3 = new IntegerValue(3);
		TermExpression k = new VariableTermExpression(vark);

		Range rng = new Range(t1, k);

		ListExpression list = new ListExpression(Arrays.asList(rng));

		Identifier x = new Identifier("x");

		PredicateExpression P1 = new Membership(x, list);
		PredicateExpression P2 = new Relation(RelationSymbol.EQ, Arrays.asList(k, t3), loc);

		Conjunction c = new Conjunction(Arrays.asList(P1, P2));

		List<Scope> scopes = c.evaluate(s, m);

		assertEquals(scopes.size(), 3);
		assertEquals(scopes.get(0).get(x), new IntegerValue(1));
		assertEquals(scopes.get(1).get(x), new IntegerValue(2));
		assertEquals(scopes.get(2).get(x), new IntegerValue(3));
	}
}
