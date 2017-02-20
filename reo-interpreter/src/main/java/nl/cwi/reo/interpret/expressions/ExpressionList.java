package nl.cwi.reo.interpret.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.oldstuff.Expression;

public class ExpressionList extends ArrayList<Expression> implements Expressions {
	
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
	public Expressions evaluate(Map<String, Expression> params) {
		boolean isValueList = true;
		List<Expression> entries = new ArrayList<Expression>();
		List<ValueExpression> values = new ArrayList<ValueExpression>();
		for (Expression e : this) {
			Expression e_p = e.evaluate(params);
			entries.add(e_p);
			if (e_p instanceof ValueExpression) {
				values.add((ValueExpression)e_p);
			} else {
				isValueList = false;
			}
		}
		if (isValueList) 
			return new ValueList(values);
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
