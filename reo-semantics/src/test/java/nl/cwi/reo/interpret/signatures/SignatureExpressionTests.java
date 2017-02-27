package nl.cwi.reo.interpret.signatures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.terms.Range;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.terms.VariableTermExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.NodeExpression;
import nl.cwi.reo.interpret.variables.ParameterExpression;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

public class SignatureExpressionTests {

	@Test
	public void evaluate_NoParametersAndExplicitNodes_PortsInheritType() {

		/*
		 * Evaluation of signature <>(a?int, b!bool, c:string) in <>(x, y, z).
		 */

		Location loc = new Location("myfile.treo", 1, 1);
		Monitor m = new Monitor();

		List<ParameterExpression> params = new ArrayList<ParameterExpression>();
		List<NodeExpression> nodes = new ArrayList<NodeExpression>();

		List<TermExpression> noindices = new ArrayList<TermExpression>();

		VariableExpression a = new VariableExpression("a", noindices, loc);
		VariableExpression b = new VariableExpression("b", noindices, loc);
		VariableExpression c = new VariableExpression("c", noindices, loc);

		nodes.add(new NodeExpression(a, PortType.IN, new TypeTag("int")));
		nodes.add(new NodeExpression(b, PortType.OUT, new TypeTag("bool")));
		nodes.add(new NodeExpression(c, PortType.NONE, new TypeTag("string")));

		SignatureExpression signature = new SignatureExpression(params, nodes, loc);

		List<Term> values = new ArrayList<Term>();
		List<Port> ports = Arrays.asList(new Port("x"), new Port("y"), new Port("z"));

		Signature sign = signature.evaluate(values, ports, m);

		Port x = new Port("x", PortType.IN, PrioType.NONE, new TypeTag("int"), false);
		Port y = new Port("y", PortType.OUT, PrioType.NONE, new TypeTag("bool"), false);
		Port z = new Port("z", PortType.NONE, PrioType.NONE, new TypeTag("string"), false);

		assertTrue(m.getMessages().isEmpty());
		assertTrue(sign.getAssignments().isEmpty());
		assertEquals(sign.getInterface().get(new Port("a")), x);
		assertEquals(sign.getInterface().get(new Port("b")), y);
		assertEquals(sign.getInterface().get(new Port("c")), z);
	}

	@Test
	public void evaluate_ParameterWithIndices_Error() {

		/*
		 * Evaluation of signature <x[k]:int>() in <0>().
		 */

		Location loc = new Location("myfile.treo", 1, 1);
		Monitor m = new Monitor();

		List<TermExpression> noindices = new ArrayList<TermExpression>();
		VariableExpression k = new VariableExpression("k", noindices, loc);
		TermExpression tk = new VariableTermExpression(k);

		List<TermExpression> indices_x = Arrays.asList(tk);

		VariableExpression x = new VariableExpression("x", indices_x, loc);

		ParameterExpression px = new ParameterExpression(x, new TypeTag("int"));

		List<ParameterExpression> params = Arrays.asList(px);
		List<NodeExpression> nodes = new ArrayList<NodeExpression>();

		SignatureExpression signature = new SignatureExpression(params, nodes, loc);

		List<Term> values = Arrays.asList(new IntegerValue(1));
		List<Port> ports = new ArrayList<Port>();

		signature.evaluate(values, ports, m);

		assertTrue(!m.getMessages().isEmpty());
	}

	@Test
	public void evaluate_NoParametersAndNodeRanges() {

		/*
		 * Evaluation of signature <>(a[1..k]?int) in <>(x, y, z).
		 */

		Location loc = new Location("myfile.treo", 1, 1);
		Monitor m = new Monitor();

		List<ParameterExpression> params = new ArrayList<ParameterExpression>();
		List<NodeExpression> nodes = new ArrayList<NodeExpression>();

		List<TermExpression> noindices = new ArrayList<TermExpression>();
		VariableExpression k = new VariableExpression("k", noindices, loc);

		TermExpression l = new IntegerValue(1);
		TermExpression u = new VariableTermExpression(k);
		List<TermExpression> indices = Arrays.asList(new Range(l, u));
		VariableExpression a = new VariableExpression("a", indices, loc);

		nodes.add(new NodeExpression(a, PortType.IN, new TypeTag("int")));

		SignatureExpression signature = new SignatureExpression(params, nodes, loc);

		List<Term> values = new ArrayList<Term>();
		List<Port> ports = Arrays.asList(new Port("x"), new Port("y"), new Port("z"));

		Signature sign = signature.evaluate(values, ports, m);

		Port x = new Port("x", PortType.IN, PrioType.NONE, new TypeTag("int"), false);
		Port y = new Port("y", PortType.IN, PrioType.NONE, new TypeTag("int"), false);
		Port z = new Port("z", PortType.IN, PrioType.NONE, new TypeTag("int"), false);

		assertTrue(m.getMessages().isEmpty());
		assertEquals(sign.getAssignments().get(new Identifier("k")), new IntegerValue(3));
		assertEquals(sign.getInterface().get(new Port("a[1]")), x);
		assertEquals(sign.getInterface().get(new Port("a[2]")), y);
		assertEquals(sign.getInterface().get(new Port("a[3]")), z);

	}
}