package nl.cwi.reo.interpret.ports;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.cwi.reo.interpret.typetags.TypeTag;

// TODO: Auto-generated Javadoc
/**
 * The Class PortTest.
 */
public class PortTest {

	/**
	 * Join original has no types.
	 */
	@Test
	public void join_OriginalHasNoTypes() {
		Port origin = new Port("origin", PortType.NONE, PrioType.NONE, null, false);
		Port target = new Port("target", PortType.IN, PrioType.NONE, new TypeTag("int"), false);
		Port composed = target.join(origin);

		assertEquals(composed.getType(), PortType.IN);
		assertEquals(composed.getPrioType(), PrioType.NONE);
		assertEquals(composed.getTypeTag().toString(), "int");
	}

	/**
	 * Join inherit.
	 */
	@Test
	public void join_Inherit() {
		Port origin = new Port("origin", PortType.IN, PrioType.AMPERSANT, new TypeTag("string"), false);
		Port target = new Port("target", PortType.OUT, PrioType.NONE, new TypeTag("int"), false);
		Port composed = target.join(origin);

		assertEquals(composed.getType(), PortType.IN);
		assertEquals(composed.getPrioType(), PrioType.NONE);
		assertEquals(composed.getTypeTag().toString(), "string");
	}

}
