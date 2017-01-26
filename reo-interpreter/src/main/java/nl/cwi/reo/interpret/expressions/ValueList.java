package nl.cwi.reo.interpret.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.semantics.api.Expression;

public class ValueList extends ArrayList<ValueExpression> implements Expressions {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -2252873175064572188L;
	
	public ValueList() { }
	
	public ValueList(List<ValueExpression> entries) {
		if (entries == null)
			throw new NullPointerException();
		for (ValueExpression e : entries) {
			if (e == null)
				throw new NullPointerException();
			super.add(e);
		}
	}

	@Override
	public Expressions evaluate(Map<String, Expression> params) {
		List<ValueExpression> entries = new ArrayList<ValueExpression>();
		for (ValueExpression e : this)
			entries.add(e.evaluate(params));
		return new ValueList(entries);
	}
	
	@Override
	public String toString() {
		String s = "<";
		Iterator<ValueExpression> expr = this.iterator();
		while (expr.hasNext())
			s += expr.next() + (expr.hasNext() ? ", " : "" );
		return s + ">";
	}
}
