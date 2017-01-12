package nl.cwi.reo.interpret.arrays;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.variables.VariableName;

public class ArrayRange extends ArrayList<Array> implements Array {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -2252873175064572188L;
	
	public ArrayRange() { }
	
	public ArrayRange(List<Array> entries) {
		if (entries == null)
			throw new NullPointerException();
		for (Array e : entries) {
			if (e == null)
				throw new NullPointerException();
			super.add(e);
		}
	}

	@Override
	public Array evaluate(Map<VariableName, Expression> params)
			throws Exception {
		boolean isExpressionRange = true;
		List<Array> entries = new ArrayList<Array>();
		List<Expression> expressions = new ArrayList<Expression>();
		for (Array e : this) {
			Array e_p = e.evaluate(params);
			if (e_p instanceof ExpressionRange) {
				for (Expression x : (ExpressionRange)e_p) {
					entries.add(x);	
					expressions.add(x);			
				}
			} else if (e_p instanceof ArrayRange) {
				isExpressionRange = false;
				for (Array x : (ArrayRange)e_p) 
					entries.add(x);						
			} else {
				entries.add(e_p);
				if (e_p instanceof Expression) {
					expressions.add((Expression)e_p);
				} else {
					isExpressionRange = false;
				}
			}
		}
		if (isExpressionRange)
			return new ExpressionRange(expressions);
		return new ArrayRange(entries);
	}
	
	@Override
	public String toString() {
		String s = "<";
		Iterator<Array> expr = this.iterator();
		while (expr.hasNext())
			s += expr.next() + (expr.hasNext() ? ", " : "" );
		return s + ">";
	}
}
