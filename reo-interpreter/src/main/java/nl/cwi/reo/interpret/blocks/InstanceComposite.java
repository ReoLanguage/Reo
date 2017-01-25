package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.strings.StringExpression;
import nl.cwi.reo.semantics.api.Semantics;

public class InstanceComposite<T extends Semantics<T>> implements Instance<T> {
	
	private final StringExpression op;

	private final Instance<T> e1;
	
	private final Instance<T> e2;
	
	public InstanceComposite(StringExpression op, Instance<T> e1, Instance<T> e2) {
		this.op = op;
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public ReoBlock<T> evaluate(Map<String, Expression> params) throws CompilationException {
		StringExpression _op = op.evaluate(params);
		ReoBlock<T> _e1 = e1.evaluate(params);
		ReoBlock<T> _e2 = e2.evaluate(params);
//		if (_e1 instanceof IntegerValue && _e2 instanceof IntegerValue)
//			return IntegerValue.addition((IntegerValue)_e1, (IntegerValue)_e2);
//		return new IntegerAddition(_e1, _e2);
		return null;
	}
	
	@Override
	public String toString() {
		return "(" + e1 + op + e2 + ")";
	}
}
