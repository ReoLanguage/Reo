package nl.cwi.reo.pr.misc;

import java.util.LinkedHashMap;

import nl.cwi.reo.pr.misc.PortFactory.Port;

public class Array extends LinkedHashMap<Integer, Port> implements PortOrArray {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return values().toString().replaceAll(" ", "");
	}
}
