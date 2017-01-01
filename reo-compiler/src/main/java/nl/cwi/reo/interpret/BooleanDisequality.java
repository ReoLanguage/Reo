package nl.cwi.reo.interpret;


public final class BooleanDisequality implements BooleanExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	public BooleanDisequality(IntegerExpression e1, IntegerExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public BooleanExpression evaluate(DefinitionList params) throws Exception {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.neq((IntegerValue)x1, (IntegerValue)x2);
		return new BooleanDisequality(x1, x2);
	}
}
