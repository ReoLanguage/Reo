package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public final class Interface extends ArrayList<VariableName> implements Range {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4878686718124263911L;
	
	/**
	 * Constructs an interface out of a list of variables.
	 * @param vars	list of variables (each referring to a node or node range)
	 */
	public Interface(List<VariableName> vars) {
		if (vars == null)
			throw new NullPointerException();
		for (VariableName x : vars) {
			if (x == null)
				throw new NullPointerException();
			super.add(x);
		}
	}

	@Override
	public Interface evaluate(Map<VariableName, Expression> params) throws Exception {
		return this;
	}
	
	@Override
	public String toString() {
		String s = "(";
		Iterator<VariableName> var = this.iterator();
		while (var.hasNext())
			s += var.next() + (var.hasNext() ? "," : "");
		return s + ")";
	}
}
