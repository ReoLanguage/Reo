package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public class ExpressionList implements Value {
	
	private List<Expression> entries; 
	
	public ExpressionList(List<Expression> entries) {
		this.entries = entries;
	}
	
	public List<Expression> getList() {
		return entries;
	}

	@Override
	public ExpressionList evaluate(DefinitionList params)
			throws Exception {
		List<Expression> entries = new ArrayList<Expression>();
		for (Expression e : this.entries)
			entries.add(e.evaluate(params));
		return new ExpressionList(entries);
	}
}
