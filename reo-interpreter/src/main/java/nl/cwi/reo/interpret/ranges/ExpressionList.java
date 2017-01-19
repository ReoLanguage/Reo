package nl.cwi.reo.interpret.ranges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableName;

public class ExpressionList extends ArrayList<Expression> implements Range {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -2252873175064572188L;
	
	public ExpressionList() { }
	
	public ExpressionList(List<Expression> entries) {
		if (entries == null)
			throw new NullPointerException();
		for (Expression e : entries) {
			if (e == null)
				throw new NullPointerException();
			super.add(e);
		}
	}

	@Override
	public ExpressionList evaluate(Map<VariableName, Expression> params)
			throws CompilationException {
		List<Expression> entries = new ArrayList<Expression>();
		for (Expression e : this)
			entries.add(e.evaluate(params));
		return new ExpressionList(entries);
	}
	
	@Override
	public String toString() {
		String s = "<";
		Iterator<Expression> expr = this.iterator();
		while (expr.hasNext())
			s += expr.next() + (expr.hasNext() ? ", " : "" );
		return s + ">";
	}
}
