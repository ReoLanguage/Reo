package nl.cwi.reo.interpret.connectors;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.plain.Plain;

public class ReoConnectorTest {

	@Test
	public void flatten_CompositeConnector_bla() {

		/*
		 * flatten a connector { { fifo(a,b), fifo(b,c) } fifo(c,d) }
		 */

		Plain fifo = new Plain("fifo", "x", "y");
		Reference s = new Reference();

		Map<Port, Port> links_ab = new HashMap<Port, Port>();
		Map<Port, Port> links_bc = new HashMap<Port, Port>();
		Map<Port, Port> links_cd = new HashMap<Port, Port>();

		Port a_i = new Port("a", PortType.IN, PrioType.NONE, new TypeTag("int"), true);
		Port b_o = new Port("b", PortType.OUT, PrioType.NONE, new TypeTag("int"), true);

		Port b_i = new Port("b", PortType.IN, PrioType.NONE, new TypeTag("int"), true);
		Port c_o = new Port("c", PortType.OUT, PrioType.NONE, new TypeTag("int"), true);

		Port c_i = new Port("c", PortType.IN, PrioType.NONE, new TypeTag("int"), true);
		Port d_o = new Port("a", PortType.OUT, PrioType.NONE, new TypeTag("int"), true);

		links_ab.put(new Port("x"), a_i);
		links_ab.put(new Port("y"), b_o);

		links_bc.put(new Port("x"), b_i);
		links_bc.put(new Port("y"), c_o);

		links_cd.put(new Port("x"), c_i);
		links_cd.put(new Port("y"), d_o);

		ReoConnector<Plain> ab = new ReoConnectorAtom<Plain>(fifo, s, links_ab);
		ReoConnector<Plain> bc = new ReoConnectorAtom<Plain>(fifo, s, links_bc);
		ReoConnector<Plain> cd = new ReoConnectorAtom<Plain>(fifo, s, links_cd);

		ReoConnector<Plain> abc = new ReoConnectorComposite<Plain>("prod", Arrays.asList(ab, bc));
		ReoConnector<Plain> connector = new ReoConnectorComposite<Plain>("", Arrays.asList(abc, cd));

		ReoConnector<Plain> flat = connector.flatten().integrate();
		
		List<ReoConnectorAtom<Plain>> list = flat.getAtoms();

		Set<Port> ports_ab = new HashSet<Port>(Arrays.asList(a_i, b_o));
		Set<Port> ports_bc = new HashSet<Port>(Arrays.asList(b_i, c_o));
		Set<Port> ports_cd = new HashSet<Port>(Arrays.asList(c_i, d_o));

		assertEquals(list.get(0).getInterface(), ports_ab);
		assertEquals(list.get(1).getInterface(), ports_bc);
		assertEquals(list.get(2).getInterface(), ports_cd);
	}

}
