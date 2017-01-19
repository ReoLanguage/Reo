package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;

public final class InterfaceExpression extends ArrayList<Variable> implements Range {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4878686718124263911L;
	
	/**
	 * Token
	 */
	private final Token token;
	
	/**
	 * Constructs an interface out of a list of variables.
	 * @param vars	list of variables (each referring to a node or node range)
	 */
	public InterfaceExpression(List<Variable> vars, Token token) {
		if (vars == null || token == null)
			throw new NullPointerException();
		for (Variable x : vars) {
			if (x == null)
				throw new NullPointerException();
			super.add(x);
		}
		this.token = token;
	}

	@Override
	public Range evaluate(Map<VariableName, Expression> params) throws CompilationException {
		List<VariableName> list_p = new ArrayList<VariableName>();
		for (Variable x : this) {
			Range r = x.evaluate(params);	
			if (r instanceof VariableNameList) {
				for (VariableName v : ((VariableNameList)r).getList())
					list_p.add(v);
			} else if (r instanceof VariableName) {
				list_p.add((VariableName)r);				
			} else if (r instanceof Variable) {
				return this;
			} else {
				throw new CompilationException(x.getToken(), "Node variable " + x + " cannot be assigned to " + r);
			}
		}
		return new VariableNameList(list_p, token);
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
