package nl.cwi.reo.automata;

public class DefaultConstraintFactory extends ConstraintFactory {

	//
	// CONSTRUCTORS
	//

	public DefaultConstraintFactory(DefaultLiteralFactory literalFactory) {
		super(literalFactory);
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Constraint newObject(int id, ConstraintSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Constraint(id, spec);
	}
}
