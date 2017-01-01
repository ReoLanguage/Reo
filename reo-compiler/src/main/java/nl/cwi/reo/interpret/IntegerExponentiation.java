package nl.cwi.reo.interpret;


public final class IntegerExponentiation implements IntegerExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	public IntegerExponentiation(IntegerExpression e1, IntegerExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public IntegerExpression evaluate(DefinitionList params) throws Exception {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.exponentiation((IntegerValue)x1, (IntegerValue)x2);
		return new IntegerExponentiation(x1, x2);
	}
}
