package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;

public final class Interface extends ArrayList<Variable> implements Evaluable<Interface> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4878686718124263911L;
	
	/**
	 * Constructs an interface out of a list of variables.
	 * @param vars	list of variables (each referring to a node or node range)
	 */
	public Interface(List<Variable> vars) {
		if (vars == null)
			throw new NullPointerException();
		for (Variable x : vars) {
			if (x == null)
				throw new NullPointerException();
			super.add(x);
		}
	}

	@Override
	public Interface evaluate(Map<VariableName, Expression> params) throws Exception {
		List<Variable> vars_p = new ArrayList<Variable>();
		for (Variable x : this) {
			Array x_p = x.evaluate(params);
			if (!(x_p instanceof Variable))
				x_p = x;
			vars_p.add((Variable)x_p);
		}
		return new Interface(vars_p);
	}
	
	@Override
	public String toString() {
		String s = "(";
		Iterator<Variable> var = this.iterator();
		while (var.hasNext())
			s += var.next() + (var.hasNext() ? "," : "");
		return s + ")";
	}
}
