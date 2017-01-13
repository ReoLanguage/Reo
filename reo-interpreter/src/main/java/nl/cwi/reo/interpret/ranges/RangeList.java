package nl.cwi.reo.interpret.ranges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.variables.VariableName;

public class RangeList extends ArrayList<Range> implements Range {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -2252873175064572188L;
	
	public RangeList() { }
	
	public RangeList(List<Range> entries) {
		if (entries == null)
			throw new NullPointerException();
		for (Range e : entries) {
			if (e == null)
				throw new NullPointerException();
			super.add(e);
		}
	}

	@Override
	public Range evaluate(Map<VariableName, Expression> params)
			throws Exception {
		boolean isExpressionRange = true;
		List<Range> entries = new ArrayList<Range>();
		List<Expression> expressions = new ArrayList<Expression>();
		for (Range e : this) {
			Range e_p = e.evaluate(params);
			if (e_p instanceof ExpressionList) {
				for (Expression x : (ExpressionList)e_p) {
					entries.add(x);	
					expressions.add(x);			
				}
			} else if (e_p instanceof RangeList) {
				isExpressionRange = false;
				for (Range x : (RangeList)e_p) 
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
			return new ExpressionList(expressions);
		return new RangeList(entries);
	}
	
	@Override
	public String toString() {
		String s = "<";
		Iterator<Range> expr = this.iterator();
		while (expr.hasNext())
			s += expr.next() + (expr.hasNext() ? ", " : "" );
		return s + ">";
	}
}
