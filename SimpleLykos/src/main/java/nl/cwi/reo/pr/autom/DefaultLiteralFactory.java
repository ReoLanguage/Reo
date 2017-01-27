package nl.cwi.reo.pr.autom;

import nl.cwi.reo.pr.autom.LiteralSpec.EqualitySpec;
import nl.cwi.reo.pr.autom.LiteralSpec.RelationSpec;

public class DefaultLiteralFactory extends LiteralFactory {

	//
	// CONSTRUCTORS
	//

	public DefaultLiteralFactory(DefaultTermFactory termFactory) {
		super(termFactory);
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Literal newLiteral(int id, EqualitySpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Equality(id, spec);
	}

	@Override
	protected Literal newLiteral(int id, RelationSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Relation(id, spec);
	}
}
