package nl.cwi.reo.interpret;


public final class BooleanNegation implements BooleanExpression {
	
	private final BooleanExpression e;
	
	public BooleanNegation(BooleanExpression e) {
		if (e == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.e = e;
	}
	
	public BooleanExpression evaluate(DefinitionList params) throws Exception {
		BooleanExpression x = e.evaluate(params);
		if (x instanceof BooleanValue)
			return BooleanValue.negation((BooleanValue)x);
		return new BooleanNegation(x);
	}
}
