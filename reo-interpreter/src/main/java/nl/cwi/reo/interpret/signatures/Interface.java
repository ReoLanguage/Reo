package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.api.Expression;

public final class Interface extends ArrayList<VariableName> implements Expression {
	
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
	public Interface evaluate(Map<String, Expression> params) throws CompilationException {
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
