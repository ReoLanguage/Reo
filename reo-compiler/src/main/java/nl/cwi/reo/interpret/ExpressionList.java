package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ExpressionList implements Value {
	
	private final List<Expression> entries; 
	
	public ExpressionList() {
		this.entries = Collections.unmodifiableList(new ArrayList<Expression>());
	}
	
	public ExpressionList(List<Expression> entries) {
		if (entries == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.entries = Collections.unmodifiableList(entries);
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
