package nl.cwi.reo.interpret;


public final class BooleanDisjunction implements BooleanExpression {
	
	private final BooleanExpression e1;
	private final BooleanExpression e2;
	
	public BooleanDisjunction(BooleanExpression e1, BooleanExpression e2) {
		if (e1 == null || e2 == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public BooleanExpression evaluate(DefinitionList params) throws Exception {
		BooleanExpression x1 = e1.evaluate(params);
		BooleanExpression x2 = e2.evaluate(params);
		if (x1 instanceof BooleanValue && x2 instanceof BooleanValue)
			return BooleanValue.disjunction((BooleanValue)x1, (BooleanValue)x2);
		return new BooleanDisjunction(x1, x2);
	}
}