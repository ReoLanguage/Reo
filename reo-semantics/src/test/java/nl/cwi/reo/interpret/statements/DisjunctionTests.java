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

public class DisjunctionTests {

	@Test
	public void evaluate_OriginalHasNoTypes() {

		/*
		 * Tests if x:<1..3> || x=4 evaluates to [{x=1}, {x=2}, {x=3}, {x=4}]
		 */

		Scope s = new Scope();
		Monitor m = new Monitor();
		Location loc = new Location("myfile.treo", 1, 1);

		List<TermExpression> indices = new ArrayList<TermExpression>();

		VariableExpression varx = new VariableExpression("x", indices, loc);

		TermExpression t1 = new IntegerValue(1);
		TermExpression t3 = new IntegerValue(3);
		TermExpression t4 = new IntegerValue(4);
		TermExpression tx = new VariableTermExpression(varx);

		Range rng = new Range(t1, t3);

		ListExpression list = new ListExpression(Arrays.asList(rng));

		Identifier x = new Identifier("x");

		PredicateExpression P1 = new Membership(x, list);
		PredicateExpression P2 = new Relation(RelationSymbol.EQ, Arrays.asList(tx, t4), loc);

		Conjunction c = new Conjunction(Arrays.asList(P1, P2));

		List<Scope> scopes = c.evaluate(s, m);

		assertEquals(scopes.size(), 4);
		assertEquals(scopes.get(0).get(x), new IntegerValue(1));
		assertEquals(scopes.get(1).get(x), new IntegerValue(2));
		assertEquals(scopes.get(2).get(x), new IntegerValue(3));
		assertEquals(scopes.get(3).get(x), new IntegerValue(4));
	}

}
