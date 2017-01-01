package nl.cwi.reo.interpret;


public final class IntegerUnaryMinus implements IntegerExpression {

	private final IntegerExpression e;
	
	public IntegerUnaryMinus(IntegerExpression e) {
		this.e = e;
	}

	@Override
	public IntegerExpression evaluate(DefinitionList params) throws Exception {
		IntegerExpression x = e.evaluate(params);
		if (x instanceof IntegerValue)
			return IntegerValue.unarymin((IntegerValue)x);
		return new IntegerUnaryMinus(x);
	}
}
