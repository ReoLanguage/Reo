package nl.cwi.reo.interpret.connectors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;

public class ReoConnectorTests {

	@Test
	public void join_OriginalHasNoTypes() {
		Port origin = new Port("origin", PortType.NONE, PrioType.NONE, new TypeTag(""), false);
		Port target = new Port("target", PortType.IN, PrioType.NONE, new TypeTag("int"), false);
		Port composed = target.join(origin);
		
		assertEquals(composed.getType(), PortType.IN);
		assertEquals(composed.getPrioType(), PrioType.NONE);
		assertEquals(composed.getTypeTag().toString(), "int");
	}
	
}
